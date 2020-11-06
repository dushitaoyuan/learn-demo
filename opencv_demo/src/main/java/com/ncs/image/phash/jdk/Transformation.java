package com.ncs.image.phash.jdk;

import com.ncs.image.phash.jdk.AmplificatingShrinking;

import java.awt.image.BufferedImage;

/**
 * 图像的变换
 * @author luoweifu
 *
 */
public class Transformation {
	/**
	 * 要进行DCT变换的图片的宽或高
	 */
	public static final int N = 256;

	/**
	 * 傅里叶变换
	 * @return
	 */
	public int[] FFT() {

		return null;
	}

	/**
	 * 离散余弦变换
	 * @param pix 原图像的数据矩阵
	 * @param n 原图像(n*n)的高或宽
	 * @return 变换后的矩阵数组
	 */
	public int[] DCT(int[] pix, int n) {
		double[][] iMatrix = new double[n][n];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				iMatrix[i][j] = (double)(pix[i*n + j]);
			}
		}
		double[][] quotient = coefficient(n);	//求系数矩阵
		double[][] quotientT = transposingMatrix(quotient, n);	//转置系数矩阵

		double[][] temp = new double[n][n];
		temp = matrixMultiply(quotient, iMatrix, n);
		iMatrix =  matrixMultiply(temp, quotientT, n);

		int newpix[] = new int[n*n];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				newpix[i*n + j] = (int)iMatrix[i][j];
			}
		}
		return newpix;
	}
	/**
	 * 矩阵转置
	 * @param matrix 原矩阵
	 * @param n 矩阵(n*n)的高或宽
	 * @return 转置后的矩阵
	 */
	private double[][]  transposingMatrix(double[][] matrix, int n) {
		double nMatrix[][] = new double[n][n];
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				nMatrix[i][j] = matrix[j][i];
			}
		}
		return nMatrix;
	}
	/**
	 * 求离散余弦变换的系数矩阵
	 * @param n n*n矩阵的大小
	 * @return 系数矩阵
	 */
	private double[][] coefficient(int n) {
		double[][] coeff = new double[n][n];
		double sqrt = 1.0/Math.sqrt(n);
		for(int i=0; i<n; i++) {
			coeff[0][i] = sqrt;
		}
		for(int i=1; i<n; i++) {
			for(int j=0; j<n; j++) {
				coeff[i][j] = Math.sqrt(2.0/n) * Math.cos(i*Math.PI*(j+0.5)/(double)n);
			}
		}
		return coeff;
	}
	/**
	 * 矩阵相乘
	 * @param A 矩阵A
	 * @param B 矩阵B
	 * @param n 矩阵的大小n*n
	 * @return 结果矩阵
	 */
	private double[][] matrixMultiply(double[][] A, double[][] B, int n) {
		double nMatrix[][] = new double[n][n];
		double t = 0.0;
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				t = 0;
				for(int k=0; k<n; k++) {
					t += A[i][k]*B[k][j];
				}
				nMatrix[i][j] = t;			}
		}
		return nMatrix;
	}
	/**
	 * DCT变换
	 * @param srcPath
	 * @param destPath
	 * @param format
	 */
	public void dctTrans(String srcPath, String destPath, String format) {
		BufferedImage img = ImageDigital.readImg(srcPath);
		int w = img.getWidth();
		int h = img.getHeight();
		int[] pix = new int[w * h];
		img.getRGB(0, 0, w, h, pix, 0, w);
		pix = AmplificatingShrinking.shrink(pix, w, h, N, N);
		int newpix[] = ImageDigital.grayImage(pix, N, N);
		/*DCT2 dct = new DCT2();
		int dctPix[] = dct.dctTrans(newpix, N, N, 256, 1);*/
		int dctPix[] = DCT(newpix, 256);
		for(int i=0; i<N*N; i++) {
			newpix[i] = 255<<24 | dctPix[i]<<16 |  dctPix[i]<<8 |  dctPix[i];
		}
		img.setRGB(0, 0, w, h, newpix, 0, w);
		ImageDigital.writeImg(img, format, destPath);
	}
	/*
	public static void main(String args[]) {
		Transformation tf = new Transformation();
		tf.dctTrans("F:\\image processing\\transformation\\boats.JPG", "F:\\image processing\\transformation\\boatsD.JPG", "jpg");
		
	}*/


}

