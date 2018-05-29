package com.binpo.plugin.cloud.qcloud;

public class QCloud {
	/**
	 * COS 支持多园区存储，目前开放了华南、华北、华东三个地区
	 * @author zhang
	 *
	 */
	public enum QCloudRegion{
		/**
		 * 华南
		 */
		gz,
		/**
		 * 华北
		 */
		tj,
		/**
		 * 华中
		 */
		sh
	}
	
	public interface CCloudCode{
		public int success = 0;
		
	}
	
	
}
