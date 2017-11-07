package com.tkluza.image.model;

import org.mozilla.javascript.EvaluatorException;

import ij.process.ImageProcessor;

public interface IQualityAlgorithm {
	
	double evaluate(ImageProcessor image1, ImageProcessor image2);
}
