package org.cloud.plugin.aws;

import java.io.IOException;
import java.util.UUID;

import org.junit.Test;

import net.coobird.thumbnailator.Thumbnails;

public class ThumbnailatorTest {

	@Test
	public void test1() throws IOException {
		Thumbnails.of("C:/Users/yinhao.zhang/Desktop/图片/微信图片_20181226125739.jpg")
        .size(2776*2, 1784*2)
        .toFile("C:/Users/yinhao.zhang/Desktop/图片/thumbnail"+UUID.randomUUID()+".jpg");
	}
	

	@Test
	public void test2() throws IOException {
		Thumbnails.of("C:/Users/yinhao.zhang/Desktop/图片/主KV.jpeg")
        .size(1824, 2786)
        .toFile("C:/Users/yinhao.zhang/Desktop/图片/thumbnail"+UUID.randomUUID()+".jpg");
	}
}
