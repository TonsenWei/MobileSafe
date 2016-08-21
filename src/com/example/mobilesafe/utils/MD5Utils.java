package com.example.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	public static String MD5Encode(String password) {
		try {
			MessageDigest instance = MessageDigest.getInstance("MD5");// 获取MD5算法对象
			byte[] digest = instance.digest(password.getBytes());// 对字符串加密,返回字节数组
			StringBuffer sb = new StringBuffer();
			for (byte b : digest) {
				int i = b & 0xff; // 获取字节的低八位
				String hexStr = Integer.toHexString(i);// 将整数转换为16进制
				// System.out.println(hexStr);

				if (hexStr.length() < 2) {
					hexStr = "0" + hexStr;// 如果是1位的话,补0
				}

				sb.append(hexStr);
			}
//			System.out.println("md5:" + sb);
//			System.out.println("md5 length:" + sb.length());// md5都是32位
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
}
