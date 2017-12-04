package com.tkluza.image.model;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;

import ij.process.ImageProcessor;

public abstract class ImageAlgorithm implements IQualityAlgorithm {

	protected String algorithmName;
	protected double algorithmResult;
	
	public ImageAlgorithm() {
		algorithmName = "";
		algorithmResult = 0;
	}
	
	public String getAlgorithmName() {
		return algorithmName;
	}
	
	public double getAlgorithmResult() {
		return algorithmResult;
	}

	public void setAlgorithmResult(double algorithmResult) {
		this.algorithmResult = algorithmResult;
	}

}
