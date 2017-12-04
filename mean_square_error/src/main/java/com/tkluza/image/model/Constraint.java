package com.tkluza.image.model;

import ij.ImagePlus;
import ij.process.ImageProcessor;
import net.imagej.ops.OpCandidate.StatusCode;

public class Constraint {

	public enum ImageAlgorithm {
	    MSE(1), PSNR(2), SSIM(4), MSSIM(8);
	    private int value;

	    private ImageAlgorithm(int value) {
	            this.value = value;
	    }
	};  
	
	public enum Mode {
	    DEFAULT(1), OTHER(2);
	    private int value;

	    private Mode(int value) {
	            this.value = value;
	    }
	}; 
	
	public static ImageProcessor toImageProcessorConverter(ImagePlus image) {
		return image.getProcessor();
	}
}


