package com.ncs.image.phash.jdk;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
/**
 * image processing
 * @author xiaoxu
 *
 */
public class ImageDigital {


	/**
	 * ƽ���Ҷ�ֵ
	 *
	 * @param pix
	 * @param w
	 * @param h
	 * @return
	 */
	public static double averageGray(int[] pix, int w, int h) {
		double sum = 0;
		ColorModel cm = ColorModel.getRGBdefault();
		for (int j = 0; j < h; j++)
			for (int i = 0; i < w; i++)
				sum = sum + cm.getRed(pix[i + j * w]);
		double av = sum / (w * h);
		return av;
	}

	/**
	 * ��ͼƬת���ɺڰ׻Ҷ�ͼƬ
	 * @param srcPath
	 * @param destPath
	 */
	public static void grayImage(String srcPath, String destPath) {
		OutputStream output = null;
		try {
			// read image
			BufferedImage img = ImageIO.read(new File(srcPath));
			int imageType = img.getType();
			int w = img.getWidth();
			int h = img.getHeight();

			// rgb������,�������أ���һά�����ʾ��λͼ����������
			int[] rgbArray = new int[w*h];
			// newArray ���洦��������
			int[] newArray = new int[w*h];
			img.getRGB(0, 0, w, h, rgbArray, 0, w);
			rgbArray = grayImage(rgbArray, w, h);
			//System.out.println("�м����ص��rgb��" + c + " " + c.getRGB());
			int gray, rgb;
			ColorModel cm = ColorModel.getRGBdefault();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					/*c = new Color(rgbArray[i * w + j]);
					// ��ɫͼ��ת�����޲�ɫ�ĻҶ�ͼ��Y=0.299*R + 0.578*G + 0.114*B
					int gray = (int) (0.3 * c.getRed() + 0.58 * c.getGreen() + 0.12 * c
							.getBlue());*/
					gray = rgbArray[i*w + j];

					rgb = 255<<24 | gray<<16 | gray<<8 | gray;
					newArray[i * w + j] = cm.getRGB(rgb); // ��ɫ�Ҷ�ͼ��
				}
			}
			// create and save to bmp
			File out = new File(destPath);
			if (!out.exists())
				out.createNewFile();
			output = new FileOutputStream(out);
			BufferedImage imgOut = new BufferedImage(w, h,
					BufferedImage.TYPE_3BYTE_BGR);
			imgOut.setRGB(0, 0, w, h, newArray, 0, w);
			ImageIO.write(imgOut, "jpg", output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
				}
		}
	}
	/**
	 *  ��ͼƬת���ɺڰ׻Ҷ�ͼƬ
	 * @param pix ����ͼƬ����
	 * @param iw ��ά���ؾ���Ŀ�
	 * @param ih ��ά���ؾ���ĸ�
	 * @return �Ҷ�ͼ�����
	 */
	public static int[] grayImage(int pix[], int w, int h) {
		//int[] newPix = new int[w*h];
		ColorModel cm = ColorModel.getRGBdefault();
		for(int i=0; i<h; i++) {
			for(int j=0; j<w; j++) {
				//0.3 * c.getRed() + 0.58 * c.getGreen() + 0.12 * c.getBlue()				
				pix[i*w + j] = (int) (0.3*cm.getRed(pix[i*w + j]) + 0.58*cm.getGreen(pix[i*w + j]) + 0.12*cm.getBlue(pix[i*w + j]) );
			}
		}
		return pix;
	}

	/**
	 * ��ȡͼƬ
	 *
	 * @param srcPath
	 *            ͼƬ�Ĵ洢λ��
	 * @return ����ͼƬ��BufferedImage����
	 */
	public static BufferedImage readImg(String srcPath) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(srcPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}



	/**
	 * ͼ��ϸ��
	 *
	 * @param pix
	 * @param w
	 * @param h
	 * @return
	 */
	public static int[] thinning(int pix[], int w, int h) {
		int[] tempPixs = new int[9];
		int count = 0;
		ColorModel cm = ColorModel.getRGBdefault();
		int[] newpix = new int[w * h];
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (x != 0 && x != w - 1 && y != 0 && y != h - 1) {
					count = 0;
					tempPixs[0] = cm.getRed(pix[x + (y) * w]);
					tempPixs[1] = cm.getRed(pix[x + 1 + (y) * w]);
					tempPixs[2] = cm.getRed(pix[x + 1 + (y - 1) * w]);
					tempPixs[3] = cm.getRed(pix[x + (y - 1) * w]);
					tempPixs[4] = cm.getRed(pix[x - 1 + (y - 1) * w]);
					tempPixs[5] = cm.getRed(pix[x - 1 + (y) * w]);
					tempPixs[6] = cm.getRed(pix[x - 1 + (y + 1) * w]);
					tempPixs[7] = cm.getRed(pix[x + (y + 1) * w]);
					tempPixs[8] = cm.getRed(pix[x + 1 + (y + 1) * w]);
					for (int k = 0; k < tempPixs.length; k++) {
						if (tempPixs[k] == 0)
							count++;
					}
					// 0�Ǻ�ɫ��255�ǰ�ɫ
					if (count >= 2
							&& count <= 6
							&& tempPixs[0] == 0
							&& (tempPixs[1] * tempPixs[3] * tempPixs[7] == 0 || tempPixs[7] != 0)
							&& (tempPixs[3] * tempPixs[5] * tempPixs[7] == 0 || tempPixs[5] != 0)) {
						newpix[x + y * w] = 255 << 24 | 0 << 16 | 0 << 8 | 0;
					} else {
						newpix[x + y * w] = 255 << 24 | 255 << 16 | 255 << 8
								| 255;
					}
				} else {
					newpix[x + y * w] = 255 << 24 | 255 << 16 | 255 << 8 | 255;
				}
			}
		}
		return newpix;
	}

	/**
	 * ͼ��ϸ��
	 *
	 * @param srcPath
	 * @param destPath
	 * @param format
	 */
	public static void thinning(String srcPath, String destPath, String format) {
		BufferedImage img = readImg(srcPath);
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pix = new int[w * h];
		img.getRGB(0, 0, w, h, pix, 0, w);
		int newpix[] = thinning(pix, w, h);
		img.setRGB(0, 0, w, h, newpix, 0, w);
		writeImg(img, format, destPath);
	}

	/**
	 * ��ͼƬд�����
	 *
	 * @param img
	 *            ͼ���BufferedImage����
	 * @param formatName
	 *            �洢���ļ���ʽ
	 * @param destPath
	 *            ͼ��Ҫ����Ĵ洢λ��
	 */
	public static void writeImg(BufferedImage img, String formatName,
								String destPath) {
		OutputStream out = null;
		try {
			// int imgType = img.getType();
			// System.out.println("w:" + img.getWidth() + "  h:" +
			// img.getHeight());
			out = new FileOutputStream(destPath);
			ImageIO.write(img, formatName, out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*public static void main(String[] args) {
	BufferedImage img = ImageDigital.readImg("C:\\Users\\Administrator\\Desktop\\blog\\person.jpg");
	BufferedImage iImg = AmplificatingShrinking.shrink(img, 8, 8);
	try {
		ImageIO.write(iImg, "jpg", new File("C:\\Users\\Administrator\\Desktop\\blog\\personSmall.jpg"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	BufferedImage img = readImg("F:\\image processing\\rabbit.jpg");
	//BufferedImage dImg = shrink(img, 100,100);
	//writeImg(dImg, "jpg", "F:\\image processing\\rabbit_ss.jpg");
	// analyseRGB();
	 grayImage("F:\\image processing\\rabbit_ss.jpg",
	 "F:\\image processing\\rabbit_ss_gray.jpg");
	// sampleImage("F:\\image processing\\baboom_gray.jpg",
	// "F:\\image processing\\baboom.jpg_gray16.jpg", 16);
	// quantize("F:\\image processing\\baboom_gray.jpg",
	// "F:\\image processing\\baboom_quantize8.jpg",8);

	// isometryFilex("F:\\image processing\\ͼ1.jpg",
	// "F:\\image processing\\ͼ2.jpg", "jpg", 400, 400);
	// isometryFilex("F:\\image processing\\rabbit2.jpg",
	// "F:\\image processing\\rabbit5.jpg", "jpg", 6.0f, 5.0f);
	// biCubicInterpolationScale("F:\\image processing\\rabbit2.jpg",
	// "F:\\image processing\\rabbit6.jpg", "jpg", 6.0f, 5.0f);

	// snnFiltering("F:\\image processing\\ȥ��\\CC000038_gray.jpg",
	// "F:\\image processing\\ȥ��\\CC000038_gray2.jpg", "jpg");

	// thinning("F:\\image processing\\ͼ2_grayTh2.jpg",
	// "F:\\image processing\\ͼ2_grayThxx.jpg", "jpg");
	// thinning("F:\\image processing\\test.jpg",
	// "F:\\image processing\\test2.jpg", "jpg");
	
	 * thinning("F:\\image processing\\test2.jpg",
	 * "F:\\image processing\\test3.jpg", "jpg");
	 * thinning("F:\\image processing\\test3.jpg",
	 * "F:\\image processing\\test4.jpg", "jpg");
	 * thinning("F:\\image processing\\test4.jpg",
	 * "F:\\image processing\\test5.jpg", "jpg");
	 * thinning("F:\\image processing\\test5.jpg",
	 * "F:\\image processing\\test6.jpg", "jpg");
	 

	// corrode("F:\\image processing\\ͼ2_grayTh2.jpg",
	// "F:\\image processing\\ͼ2_grayTh2xx0.jpg", "jpg");
	corrode("F:\\image processing\\testt1.jpg",
			"F:\\image processing\\testt2.jpg", "jpg");
	// corrode("F:\\image processing\\navelorangeL0Th2.jpg",
	// "F:\\image processing\\navelorangeL0Thxx.jpg", "jpg");
	// System.out.println(0|1);

	// function("F:\\image processing\\test.jpg");
}*/

}
