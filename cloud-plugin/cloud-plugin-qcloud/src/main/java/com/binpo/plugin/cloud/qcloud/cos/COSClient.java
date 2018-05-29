package com.binpo.plugin.cloud.qcloud.cos;

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
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.binpo.plugin.cloud.ISecretKey;
import com.binpo.plugin.cloud.qcloud.QCloud.QCloudRegion;
import com.binpo.plugin.cloud.storage.ObjClient;
import com.binpo.plugin.cloud.storage.ObjectInfo;
import com.binpo.plugin.cloud.storage.ObjectUploadResult;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.meta.InsertOnly;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.GetFileLocalRequest;
import com.qcloud.cos.request.StatFileRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
/**
 * 
 * 腾讯云的对象存储实现
 *
 * @author zhang 2017年6月14日 下午12:59:57
 */
public class COSClient implements ObjClient {
    /**
     * 默认的地区
     */
    private String Region = "sh";// 华南
    /**
     * 默认的
     */
    private String bucketName = "";

    private com.qcloud.cos.COSClient cosClient;

    private long appId;

    private ISecretKey key ;

    private Log logger = LogFactory.getLog(getClass());
    
    private String index="/img/upload";

    private final int successCode = 0;
    
    
    public COSClient(ISecretKey key) {
    	this.key = key;
        createcosClient();
    }
    public COSClient(QCloudRegion Region, String bucketName, long appId,ISecretKey key) {
        this.appId = appId;
        this.Region = Region.toString();
        this.bucketName = bucketName;
        this.key = key;
        createcosClient();
    }

    public COSClient(QCloudRegion Region, String bucketName, long appId) {
        this.appId = appId;
        this.Region = Region.toString();
        this.bucketName = bucketName;
        createcosClient();
    }


    private void createcosClient() {
        // 初始化秘钥信息
        Credentials cred = new Credentials(appId, key.getSecretId(), key.getSecretKey());
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        // 设置bucket所在的区域，比如广州(gz), 天津(tj)
        clientConfig.setRegion(Region);

        // 初始化cosClient
        cosClient = new com.qcloud.cos.COSClient(clientConfig, cred);
    }

    public void close() {
        // 关闭释放资源
        cosClient.shutdown();
    }

    @Override
    public ObjectUploadResult uploadObject(String cloudPath, String localPath) {
        cloudPath = formatPathForFile(cloudPath);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cloudPath, localPath);
        uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);// 覆盖
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.debug(uploadFileRet);
        close();
        return objectUploadResult(cloudPath, uploadFileRet);
    }

    private ObjectUploadResult objectUploadResult(String cloudPath, String ret) {
        Map parseObject = JSON.parseObject(ret, Map.class);
        int code = (int) parseObject.get("code");
        if (code == successCode) {
            Map object = (Map) parseObject.get("data");
            String source_url = (String) object.get("source_url");
            ObjectUploadResult info = new ObjectUploadResult();
            info.setUrl(source_url);
            info.setBucketName(this.bucketName);
            info.setCloudPath(cloudPath);
            info.setResultStr(ret);
            return info;
        }
        return new ObjectUploadResult();
    }

    @Override
    public ObjectUploadResult uploadObject(String cloudPath, File file) throws FileNotFoundException,
            IOException {
        cloudPath = formatPathForFile(cloudPath);
        return uploadObject(cloudPath, new FileInputStream(file));
    }
    
    private static File inToFile(InputStream in,String ext) throws IOException {
        File createTempFile = File.createTempFile(UUID.randomUUID().toString(), "."+ext);
        FileOutputStream fos = new FileOutputStream(createTempFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        in.close();
        fos.close();
        return createTempFile;
    }
    

    @Override
    public ObjectUploadResult uploadObject(String cloudPath, InputStream in) throws IOException {
        cloudPath = formatPathForFile(cloudPath);
//        File inToFile = inToFile(in, cloudPath.substring(cloudPath.lastIndexOf(".")+1));
        byte[] byt = new byte[in.available()];
        in.read(byt);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cloudPath, byt);
//        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName,cloudPath,inToFile.getPath());
        uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);// 覆盖
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.debug(uploadFileRet);
        ObjectUploadResult objectUploadResult = objectUploadResult(cloudPath, uploadFileRet);
        close();
//        inToFile.delete();
        return objectUploadResult;
    }
    /**
     * 
     * 指定了名称的使用指定名称，未指定名称的使用uuid作为名称
     * 
     * @param cloudPath
     * @param ext
     * @return
     */
    private String formatPath(String cloudPath,String ext){
        if(!cloudPath.startsWith("/")){
            cloudPath = "/"+cloudPath;
        }
        if(cloudPath.contains("."+ext)){
            return this.index+cloudPath;
        }
        if(cloudPath.endsWith("/")){
            return this.index+cloudPath+UUID.randomUUID().toString()+"."+ext;
        }else{
            return this.index+cloudPath+"/"+UUID.randomUUID().toString()+"."+ext;
        }
    }
    
    private String formatPathForFile(String cloudPath){
        if(!cloudPath.startsWith("/")){
            cloudPath = "/"+cloudPath;
        }
        return cloudPath;
    }
    
    @Override
    public ObjectUploadResult uploadImage(String cloudPath, String localPath, String ext) throws IOException {
        cloudPath = formatPath(cloudPath, ext);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cloudPath, localPath);
        uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);// 覆盖
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.debug(uploadFileRet);
        File file = new File(localPath);
        ObjectUploadResult uploadSmallImage = uploadSmallImage(file, cloudPath, ext, uploadFileRet);
        this.close();
        return uploadSmallImage;
    }

    @Override
    public ObjectUploadResult uploadImage(String cloudPath, File file, String ext)
            throws FileNotFoundException, IOException {
        cloudPath = formatPath(cloudPath, ext);
        FileInputStream in = new FileInputStream(file);
        byte[] byt = new byte[in.available()];
        in.read(byt);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cloudPath, byt);
        uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);// 覆盖
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.debug(uploadFileRet);
        ObjectUploadResult ret = uploadSmallImage(file, cloudPath, ext, uploadFileRet);
        close();
        return ret;
    }

    @Override
    public ObjectUploadResult uploadImage(String cloudPath, InputStream in, String ext) throws IOException {
        cloudPath = formatPath(cloudPath, ext);
        byte[] byt = new byte[in.available()];
        in.read(byt);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cloudPath, byt);
        uploadFileRequest.setInsertOnly(InsertOnly.OVER_WRITE);// 覆盖
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.debug(uploadFileRet);
        File file = parseInToFile(in, ext, byt);
        file.deleteOnExit();
        ObjectUploadResult uploadSmallImage = uploadSmallImage(file, cloudPath, ext, uploadFileRet);
        close();
        return uploadSmallImage;

    }

    @Override
    public InputStream getObject(String cloudPath) throws Exception {
        GetFileLocalRequest getFileLocalRequest = new GetFileLocalRequest(bucketName, cloudPath, null);
        getFileLocalRequest.setUseCDN(false);// 是否使用cdn加速
        InputStream fileInputStream = cosClient.getFileInputStream(getFileLocalRequest);
        close();
        return fileInputStream;
        
    }

    @Override
    public ObjectInfo getObjectInfo(String cloudPath) {
        StatFileRequest statFileRequest = new StatFileRequest(bucketName, cloudPath);
        String statFileRet = cosClient.statFile(statFileRequest);
        logger.debug(statFileRet);
        Map parseObject = JSON.parseObject(statFileRet, Map.class);
        Map object = (Map) parseObject.get("data");
        ObjectInfo info = new ObjectInfo();
        int fileSize = (int) object.get("filesize");
        info.setFileSize(fileSize);
        String source_url = (String) object.get("source_url");
        info.setSource_url(source_url);
        close();
        return info;
    }

    @Override
    public void deleteObject(String cloudPath) {
        DelFileRequest delFileRequest = new DelFileRequest(bucketName, cloudPath);
        String delFileRet = cosClient.delFile(delFileRequest);
        logger.debug(delFileRet);
        close();
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
//            ImageIO.write(buffi, "PNG", targetImg);
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
    
    
    

    private ObjectUploadResult uploadSmallMiddleImage(File file, String cloudPath, int width, int height,
            String contentType) throws FileNotFoundException, IOException {
        File targetFile = null;
        try {
            targetFile = File.createTempFile(UUID.randomUUID().toString(), "." + contentType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ObjectUploadResult();
        }
        if (targetFile != null) {
            File imageScale = ImageScale(file, targetFile, width, height);
            if (imageScale == null)
                return new ObjectUploadResult();
            ObjectUploadResult uploadObject = uploadObject(cloudPath, imageScale);
            targetFile.deleteOnExit();
            imageScale.deleteOnExit();
            return uploadObject;
        }
        return new ObjectUploadResult();
    }

    private File parseInToFile(InputStream in, String ext, byte[] bytes) throws IOException {
        File file = null;
        FileOutputStream fileOutStream = null;
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            outStream.write(bytes);
            in.close();
            byte[] data = outStream.toByteArray();
            file = File.createTempFile(UUID.randomUUID().toString(), "." + ext.toString());
            fileOutStream = new FileOutputStream(file);
            fileOutStream.write(data);
            fileOutStream.flush();
            fileOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutStream != null) {
                fileOutStream.flush();
                fileOutStream.close();
            }
        }
        return file;
    }

    private ObjectUploadResult uploadSmallImage(File file, String cloudPath, String ext, String ret)
            throws FileNotFoundException, IOException {
        if (file.exists()) {
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            ObjectUploadResult uploadSmallMiddleImage = uploadSmallMiddleImage(file, cloudPath + "_small."
                    + ext.toString(), 160, 160, ext.toString());
            ObjectUploadResult uploadSmallMiddleImage2 = uploadSmallMiddleImage(file, cloudPath + "_middle."
                    + ext.toString(), 500, 500, ext.toString());
            return imageUploadResult(cloudPath, ret, width, height, uploadSmallMiddleImage.getUrl(),
                    uploadSmallMiddleImage2.getUrl());
        }
        return imageUploadResult(cloudPath, ret, 0, 0, null, null);
    }

    private ObjectUploadResult imageUploadResult(String cloudPath, String ret, int width, int height,
            String smallImagePath, String middleImagePath) {
        Map parseObject = JSON.parseObject(ret, Map.class);
        int code = (int) parseObject.get("code");
        if (code == successCode) {
            Map object = (Map) parseObject.get("data");
            String source_url = (String) object.get("source_url");
            ObjectUploadResult info = new ObjectUploadResult();
            info.setUrl(source_url);
            info.setBucketName(this.bucketName);
            info.setCloudPath(cloudPath);
            info.setResultStr(ret);
            info.setWidth(width);
            info.setHeight(height);
            info.setSmallImagePath(smallImagePath);
            info.setMiddleImagePath(middleImagePath);
            return info;
        }
        return new ObjectUploadResult();
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
        ObjectUploadResult uploadImage = uploadImage(cloudPath, file, extend);
        close();
        return uploadImage;
    }
    
    
    public static void main(String[] a) throws Exception{
        COSClient client = new COSClient(null);
        client.close();
        ObjectUploadResult uploadImage = client.uploadObject("file/test/dd.jpg", "C:/Users/zhang/Pictures/Screenshots/333.png");
        System.out.println(JSON.toJSONString(uploadImage));
        
//        COSClient client = new COSClient();
//        InputStream inStream = client.getObject("/dev/20160424153321.jpg");
//        File imageFile = null;
//        try {
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = inStream.read(buffer)) != -1) {
//                outStream.write(buffer, 0, len);
//            }
//            inStream.close();
//            byte[] data = outStream.toByteArray();
//            imageFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
////          imageFile.deleteOnExit();
//            FileOutputStream fileOutStream = new FileOutputStream(imageFile);
//            fileOutStream.write(data);
//            fileOutStream.flush();
//            fileOutStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            
//        }
        
    }
    

}
