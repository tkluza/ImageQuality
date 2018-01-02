package com.tkluza.image.model;

import ij.process.ImageProcessor;

public interface IQualityAlgorithm {

	void evaluate(ImageProcessor image1, ImageProcessor image2);
}
