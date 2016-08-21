package com.example.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	public static String stream2StrUtil(InputStream in) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int len = 0;
		byte[] buffer = new byte[1024];
		
		while ((len = in.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		String strResult = bos.toString();
		in.close();
		bos.close();
		return strResult;
	}
}
