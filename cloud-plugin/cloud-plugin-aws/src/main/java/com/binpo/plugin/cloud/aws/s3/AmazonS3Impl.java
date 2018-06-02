package com.binpo.plugin.cloud.aws.s3;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.storage.ObjClient;
import com.binpo.plugin.cloud.storage.ObjectInfo;
import com.binpo.plugin.cloud.storage.ObjectUploadResult;

/**
 * 
 * AWS对象存储的实现
 *
 * @author zhang 2017年5月15日 下午6:26:31
 */
public class AmazonS3Impl implements ObjClient {
    /**
     * 默认的地区
     */
    private Region region = Region.getRegion(Regions.CN_NORTH_1);
    /**
     * 默认的
     */
    private String bucketName = "";
    private AmazonS3 s3;
    private ISecretKey key ;
    private String index = "/img/upload/";
    private String rootPath = "https://s3.cn-north-1.amazonaws.com.cn/" + this.bucketName + "/";

    private Log logger = LogFactory.getLog(getClass());

    public AmazonS3Impl(String bucketName,ISecretKey key) {
        this.key = key;
        this.bucketName = bucketName;
        rootPath = "https://s3.cn-north-1.amazonaws.com.cn/" + this.bucketName + "/";
        init();
    }



    private void init() {
        AWSCredentials credentials = new BasicAWSCredentials(key.getSecretId(), key.getSecretKey());
        AmazonS3Client amazonS3Client = new AmazonS3Client(credentials);
        amazonS3Client.setRegion(region);
        s3 = amazonS3Client;
    }

    @Override
    public ObjectUploadResult uploadObject(String cloudPath, String localPath) {
        String path = formatPath(cloudPath);
        s3.putObject(bucketName, path, new File(localPath));
        ObjectUploadResult objectUploadResult = new ObjectUploadResult(geturl(path), bucketName, path);
        if (logger.isDebugEnabled()) {
            logger.debug(JSON.toJSONString(objectUploadResult));
        }
        return objectUploadResult;
    }

    private String geturl(String path) {
        String url = rootPath + path;
        return url;
    }

    @Override
    public ObjectUploadResult uploadObject(String cloudPath, File file)
            throws FileNotFoundException, IOException {
        String path = formatPath(cloudPath);
        s3.putObject(bucketName, path, file);
        ObjectUploadResult objectUploadResult = new ObjectUploadResult(geturl(path), bucketName, path);
        if (logger.isDebugEnabled()) {
            logger.debug(JSON.toJSONString(objectUploadResult));
        }
        return objectUploadResult;
    }

    @Override
    public ObjectUploadResult uploadObject(String cloudPath, InputStream in) throws IOException {
        String path = formatPath(cloudPath);
        FileInputStream fis = (FileInputStream) in;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentLength(fis.getChannel().size());
            s3.putObject(bucketName, path, fis, objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ObjectUploadResult objectUploadResult = new ObjectUploadResult(geturl(path), bucketName, path);
        if (logger.isDebugEnabled()) {
            logger.debug(JSON.toJSONString(objectUploadResult));
        }
        return objectUploadResult;
    }

    @Override
    public ObjectUploadResult uploadImage(String cloudPath, String localPath, String extend)
            throws IOException {
        return uploadImage(index + formatPath(cloudPath, extend), new File(localPath), extend);
    }

    @Override
    public ObjectUploadResult uploadImage(String cloudPath, File file, String extend)
            throws FileNotFoundException, IOException {
        String path = formatPath(index + formatPath(cloudPath, extend));
        return uploadJpg(path, file, extend);
    }

    @Override
    public ObjectUploadResult uploadImage(String cloudPath, InputStream inStream, String extend)
            throws IOException {
        String path = formatPath(index + formatPath(cloudPath, extend));
        File imageFile = null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            byte[] data = outStream.toByteArray();
            imageFile = File.createTempFile(UUID.randomUUID().toString(), "." + extend.toString());
            imageFile.deleteOnExit();
            FileOutputStream fileOutStream = new FileOutputStream(imageFile);
            fileOutStream.write(data);
            fileOutStream.flush();
            fileOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

        return uploadJpg(path, imageFile, extend);
    }

    private ObjectUploadResult uploadJpg(String path, File file, String extend)
            throws FileNotFoundException, IOException {
        uploadObject(path, file);
        String middleImagePath = uploadSmallMiddleImage(file, path + "_small." + extend.toString(), 160, 160,
                extend);
        String smallImagePath = uploadSmallMiddleImage(file, path + "_middle." + extend.toString(), 500, 500,
                extend);
        ObjectUploadResult result = new ObjectUploadResult(geturl(path), this.bucketName, path);
        result.setMiddleImagePath(middleImagePath);
        result.setSmallImagePath(smallImagePath);
        result.setCloudPath(path);
        try {
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            result.setHeight(height);
            result.setWidth(width);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) {
            logger.debug(JSON.toJSONString(result));
        }
        file.delete();
        return result;
    }

    private String uploadSmallMiddleImage(File file, String path, int width, int height, String contentType)
            throws FileNotFoundException, IOException {
        File targetFile = null;
        try {
            targetFile = File.createTempFile(UUID.randomUUID().toString(), "." + contentType);
        } catch (IOException e) {
            logger.error(e);
            targetFile = file;
        }
        if (targetFile != null) {
            targetFile.deleteOnExit();
            File imageScale = ImageScale(file, targetFile, width, height);
            uploadObject(path, imageScale);
            targetFile.delete();
            return geturl(path);
        }
        return null;
    }

    private File ImageScale(File sourceImg, File targetImg, int width, int height) {
        return resizePng(sourceImg, targetImg, width, height, true);
//        try {
//            width = width * 2;
//            Image image = ImageIO.read(sourceImg);
//            int sorcewidth = image.getWidth(null);
//            int height2 = image.getHeight(null);
//            double d = (Double.valueOf(height2) / Double.valueOf(sorcewidth)) * Double.valueOf(width);
//            height = (int) d;
//            BufferedImage buffi = new BufferedImage(width / 2, height / 2, BufferedImage.TYPE_INT_RGB);
//            Graphics g = buffi.getGraphics();
//            g.drawImage(image, 0, 0, width / 2, height / 2, null);
//            g.dispose();
//            ImageIO.write(buffi, "JPG", targetImg);
//            return targetImg;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
    }
    /**
     * 
     * 图片压缩，解决png背景变黑的问题
     * 
     * @param fromFile 源文件
     * @param toFile 目标文件
     * @param outputWidth 输出宽度
     * @param outputHeight 输出高度
     * @param proportion 是否等比例压缩
     * @return
     */
    private File resizePng(File fromFile, File toFile, int outputWidth, int outputHeight,
            boolean proportion) {
        try {
            BufferedImage bi2 = ImageIO.read(fromFile);
            int newWidth;
            int newHeight;
            // 判断是否是等比缩放
            if (proportion) {
                // 为等比缩放计算输出的图片宽度及高度
                double rate1 = ((double) bi2.getWidth(null)) / (double) outputWidth + 0.1;
                double rate2 = ((double) bi2.getHeight(null)) / (double) outputHeight + 0.1;
                // 根据缩放比率大的进行缩放控制
                double rate = rate1 < rate2 ? rate1 : rate2;
                newWidth = (int) (((double) bi2.getWidth(null)) / rate);
                newHeight = (int) (((double) bi2.getHeight(null)) / rate);
            } else {
                newWidth = outputWidth; // 输出的图片宽度
                newHeight = outputHeight; // 输出的图片高度
            }
            BufferedImage to = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = to.createGraphics();
            to = g2d.getDeviceConfiguration().createCompatibleImage(newWidth, newHeight,
                    Transparency.TRANSLUCENT);
            g2d.dispose();
            g2d = to.createGraphics();
            @SuppressWarnings("static-access")
            Image from = bi2.getScaledInstance(newWidth, newHeight, bi2.SCALE_AREA_AVERAGING);
            g2d.drawImage(from, 0, 0, null);
            g2d.dispose();
            ImageIO.write(to, "png", toFile);
            return toFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public InputStream getObject(String cloudPath) throws Exception {
        String path = formatPath(cloudPath);
        return s3.getObject(this.bucketName, path).getObjectContent();
    }

    @Override
    public ObjectInfo getObjectInfo(String cloudPath) {
        String path = formatPath(cloudPath);
        String url = rootPath + path;
        return new ObjectInfo(this.bucketName, url);
    }

    @Override
    public void deleteObject(String cloudPath) {
        String path = formatPath(cloudPath);
        s3.deleteObject(bucketName, path);
    }

    @Override
    public void close() {
    }

    private String formatPath(String cloudPath) {
        if (cloudPath.indexOf('/') == 0) {
            cloudPath = cloudPath.substring(1);
        }
        return cloudPath;
    }

    /**
     * 
     * 指定了名称的使用指定名称，未指定名称的使用uuid作为名称
     * 
     * @param cloudPath
     * @param ext
     * @return
     */
    private String formatPath(String cloudPath, String ext) {
        if (cloudPath.indexOf('/') == 0) {
            cloudPath = cloudPath.substring(1);
        }
        if (cloudPath.contains("." + ext)) {
            return cloudPath;
        }
        return cloudPath + UUID.randomUUID().toString() + "." + ext;
    }

    @Override
    public ObjectUploadResult uploadBaes64Image(String cloudPath, String fileBase64, String extend)
            throws FileNotFoundException, IOException {
        File file = null;
        FileOutputStream fos = null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buff;
        try {
            buff = decoder.decodeBuffer(fileBase64);
            file = File.createTempFile(UUID.randomUUID().toString(), "." + extend.toString());
            fos = new FileOutputStream(file);
            fos.write(buff);
            fos.flush();
            fos.close();
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uploadImage(cloudPath, file, extend);
    }

    public void setKey(ISecretKey key) {
        this.key = key;
    }

}
