package com.tkluza.image.model;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public abstract class ImageSSIMAlgorithm extends ImageAlgorithm {

	protected ImagePlus image1, image2;
	
	public ImageSSIMAlgorithm(String name, ImagePlus image1, ImagePlus image2) {
		algorithmName = name;
		this.image1 = image1;
		this.image2 = image2;
	}
	
	public void invoke() {
		showDialog();
		if (validation())
			actualAlgorithm();
	}

	@Override
	public void evaluate(ImageProcessor image1, ImageProcessor image2) {
		// TODO Auto-generated method stub
	}

	protected abstract void showDialog();
	
	protected abstract boolean validation();
	
	protected abstract void actualAlgorithm();

}
