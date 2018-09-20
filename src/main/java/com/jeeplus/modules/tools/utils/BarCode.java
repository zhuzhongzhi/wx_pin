package com.jeeplus.modules.tools.utils;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

public class BarCode {

	/** 条形码编码 **/
	public static void encode(String contents, int width, int height, String imgPath) {
		int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;
		codeWidth = Math.max(codeWidth, width);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, codeWidth, height,
					null);
			File ff = new File(imgPath);
			MatrixToImageWriter.writeToFile(bitMatrix, "png", ff);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 条形码解码 **/
	public static String decode(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			result = new MultiFormatReader().decode(bitmap, null);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {

		/** 编码 **/
		/*
		 * String imgPath = "D:\\code\\vatim99.png"; // 益达无糖口香糖的条形码 String contents =
		 * "19100100615674290221";
		 * 
		 * int width = 350, height = 100; BarCode barCode = new BarCode();
		 * barCode.encode(contents, width, height, imgPath);
		 */

		// ** 解码 **//
		/*
		 * String decodeContent = barCode.decode(imgPath);
		 * System.out.println("解码内容如下："); System.out.println(decodeContent);
		 */
	}
}