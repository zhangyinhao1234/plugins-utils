package org.cloud.plugin.aws;

import org.junit.Test;

public class LatLngTest {
	/**
	 * 定义一些常量
	 */
	private final double x_PI = 3.14159265358979324 * 3000.0 / 180.0;
	private final double PI = 3.1415926535897932384626;
	private final double a = 6378245.0;
	private final double ee = 0.00669342162296594323;

	
	@Test
	public void test1() {
		//116.410439,39.907861 // WGS84
		//116.41668039392368,39.909263645790034 GCj02
		//116.41668039392368,39.909263645790034
		
		double[] wgs84togcj02 = wgs84togcj02(116.410439, 39.907861);
		System.out.println(wgs84togcj02[0]+","+wgs84togcj02[1]);
	}
	
	
	
	
	/**
	 * WGS84转GCj02
	 * 
	 * @param wgs_lon
	 * @param wgs_lat
	 * @returns {*[]}
	 */
	public double[] wgs84togcj02(double wgs_lon, double wgs_lat) {
		if (out_of_china(wgs_lon, wgs_lat)) {
			return new double[] { wgs_lon, wgs_lat };
		} else {
			double dlat = transformlat(wgs_lon - 105.0, wgs_lat - 35.0);
			double dlon = transformlon(wgs_lon - 105.0, wgs_lat - 35.0);
			double radlat = wgs_lat / 180.0 * PI;
			double magic = Math.sin(radlat);
			magic = 1 - ee * magic * magic;
			double sqrtmagic = Math.sqrt(magic);
			dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PI);
			dlon = (dlon * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PI);
			double mglat = wgs_lat + dlat;
			double mglon = wgs_lon + dlon;
			return new double[] { mglon, mglat };
		}
	}

	/**
	 * 判断是否中国境内
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	private boolean out_of_china(double lon, double lat) {
		// 纬度3.86~53.55,经度73.66~135.05
		return !(lon > 73.66 && lon < 135.05 && lat > 3.86 && lat < 53.55);
	}

	/**
	 * 经度转换
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	private double transformlon(double lon, double lat) {
		double ret = 300.0 + lon + 2.0 * lat + 0.1 * lon * lon + 0.1 * lon * lat + 0.1 * Math.sqrt(Math.abs(lon));
		ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lon * PI) + 40.0 * Math.sin(lon / 3.0 * PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(lon / 12.0 * PI) + 300.0 * Math.sin(lon / 30.0 * PI)) * 2.0 / 3.0;
		return ret;
	}

	/**
	 * 纬度转换
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	private double transformlat(double lon, double lat) {
		double ret = -100.0 + 2.0 * lon + 3.0 * lat + 0.2 * lat * lat + 0.1 * lon * lat
				+ 0.2 * Math.sqrt(Math.abs(lon));
		ret += (20.0 * Math.sin(6.0 * lon * PI) + 20.0 * Math.sin(2.0 * lon * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}
}
