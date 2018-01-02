package com.tkluza.image.model;

import com.tkluza.tool.Constraint.QualityAlgorithm;

public abstract class ImageAlgorithm implements IQualityAlgorithm {

	protected QualityAlgorithm algorithmName;
	protected double algorithmResult;

	public ImageAlgorithm() {
		algorithmName = QualityAlgorithm.UNDEFINED;
		algorithmResult = 0;
	}

	public QualityAlgorithm getAlgorithmName() {
		return algorithmName;
	}

	public double getAlgorithmResult() {
		return algorithmResult;
	}

	public void setAlgorithmResult(double algorithmResult) {
		this.algorithmResult = algorithmResult;
	}

}
