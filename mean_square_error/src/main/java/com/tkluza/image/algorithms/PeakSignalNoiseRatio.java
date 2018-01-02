/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.tkluza.image.algorithms;

import com.tkluza.image.model.ImageAlgorithm;
import com.tkluza.tool.Constraint.QualityAlgorithm;

import ij.process.ImageProcessor;

/**
 * 
 * @author Tomasz Kluza
 *
 */
public class PeakSignalNoiseRatio extends ImageAlgorithm {

	public PeakSignalNoiseRatio() {
		super();
		algorithmName = QualityAlgorithm.PSNR;
		algorithmResult = 0;
	}

	private double calculatePSNR(double mse) {
		double psnr = 0;
		if (mse > 0) {
			psnr = 10 * Math.log10(255 * 255 / mse);
		} else
			psnr = 99;
		return psnr;
	}

	@Override
	public void evaluate(ImageProcessor image1, ImageProcessor image2) {
		double psnr = 0;
		MeanSquareError mse = new MeanSquareError();
		mse.evaluate(image1, image2);
		if (mse.getAlgorithmResult() > 0) {
			psnr = 10 * Math.log10(255 * 255 / mse.getAlgorithmResult());
		} else
			psnr = 99;

		algorithmResult = psnr;
	}

}
