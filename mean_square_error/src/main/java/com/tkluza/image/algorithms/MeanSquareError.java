package com.tkluza.image.algorithms;

import com.tkluza.image.model.IQualityAlgorithm;
import com.tkluza.image.model.ImageAlgorithm;

import ij.process.ImageProcessor;
import scala.reflect.internal.Trees.Super;

public class MeanSquareError extends ImageAlgorithm {

	public MeanSquareError() {
		super();
	}
	
	public MeanSquareError(String name) {
		super();
		algorithmName = name;
		algorithmResult = 0;
	}

	@Override
	public void evaluate(ImageProcessor image1, ImageProcessor image2) {
		int sum = 0;
		double mse = 0;
		for (int y = 0; y < image1.getHeight(); y++) {
			for (int x = 0; x < image1.getWidth(); x++) {
				int p1 = image1.getPixel(y, x);
				int p2 = image2.getPixel(y, x);
				int err = p2 - p1;
				sum += (err * err);
			}
		}

		algorithmResult = mse =  (double) sum / (image1.getHeight() * image1.getWidth());
	}

	public void get() {}
}
