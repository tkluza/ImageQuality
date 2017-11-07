package com.tkluza.image.model;

public class Constraint {

	public enum ImageAlgorithm {
	    MSE(1), PSNR(2), SSIM(4);
	    private int value;

	    private ImageAlgorithm(int value) {
	            this.value = value;
	    }
	};  
}


