/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.tkluza.image.view;

import java.awt.Checkbox;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.vfs2.impl.VirtualFileProvider;
import org.jruby.RubyBoolean.True;
import org.netlib.util.booleanW;
import org.python.antlr.PythonParser.return_stmt_return;
import org.renjin.primitives.special.ReturnException;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import com.google.common.base.Predicate;
import com.tkluza.image.algorithms.MeanSquareError;
import com.tkluza.image.algorithms.MeanStructuralSimilarity;
import com.tkluza.image.algorithms.PeakSignalNoiseRatio;
import com.tkluza.image.algorithms.StructuralSimilarity;
import com.tkluza.image.model.Constraint;
import com.tkluza.image.model.Constraint.ImageAlgorithm;
import com.tkluza.image.model.Constraint.Mode;

import edu.mines.jtk.opt.VectContainer;
import groovyjarjarantlr.collections.List;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.algorithm.Algorithm;
import net.imglib2.type.numeric.RealType;
import scala.collection.mutable.ArrayLike;

/**
 * 
 * @author Tomasz Kluza
 *
 */
@Plugin(type = Command.class, menuPath = "Plugins>ImageQualityAlgorithms")
public class ImageQuality<T extends RealType<T>> implements Command {
	//
	// Feel free to add more parameters here...
	//
	@Parameter(label = "dataset 1")
	private File file1;

	@Parameter(label = "dataset 2")
	private File file2;

	@Parameter(label = "Result 1", type = ItemIO.OUTPUT)
	private Dataset result1;

	@Parameter(label = "Result 2", type = ItemIO.OUTPUT)
	private Dataset result2;

	@Parameter
	private DatasetIOService datasetIOService;

	@Parameter
	private UIService uiService;

	@Parameter
	private OpService opService;

	@Parameter
	private ConvertService convertService;
	
	private GenericDialog dialog;	
	private ImagePlus image1, image2;
	private ImageProcessor imageProcessor1, imageProcessor2;
	private String[] algorithmNames = {"MSE", "PSNR", "SSIM", "MSSIM"};
	private boolean[] defaultState = {true, true, true, true};
	private ArrayList<com.tkluza.image.model.ImageAlgorithm> results = new ArrayList<>();

	@Override
	public void run() {
		loadImages();
		initDialog();
		getChosenAlgorithms();
		showResults();
	}

	private void initDialog() {
		dialog = new GenericDialog("Image quality algorithms:");
		dialog.addCheckboxGroup(algorithmNames.length, 2, algorithmNames, defaultState);
		dialog.showDialog();
	}
	
	private void getChosenAlgorithms() {
		int answers = 0;
		ArrayList<Checkbox> checkboxes =  new ArrayList<>(dialog.getCheckboxes());
		for (int i = 0; i < checkboxes.size(); i++) {
			Checkbox cbox = checkboxes.get(i);
			if (cbox.getState()) {
				getAlgorithm(i);
			}
		}
	}
	
	private void getAlgorithm(int index) {
		switch (index) {
		case 0:
			MeanSquareError msError = new MeanSquareError("MSE");
			msError.evaluate(imageProcessor1, imageProcessor2);
			results.add(msError);
			break;
		case 1:
			PeakSignalNoiseRatio psnr = new PeakSignalNoiseRatio("PSNR");
			psnr.evaluate(imageProcessor1, imageProcessor2);
			results.add(psnr);
			break;
		case 2:
			StructuralSimilarity ssim = new StructuralSimilarity("SSIM", image1, image2, Mode.DEFAULT);
			ssim.invoke();
			results.add(ssim);
			break;	
		case 3:
			MeanStructuralSimilarity mssim = new MeanStructuralSimilarity("MSSIM", image1, image2, Mode.DEFAULT);
			mssim.invoke();
			results.add(mssim);
			break;	
		}
	}
	
	private void  showResults() {
		GenericDialog result = new GenericDialog ("RESULTS of Image Algorithms");
		for (com.tkluza.image.model.ImageAlgorithm image : results) {
			result.addNumericField(image.getAlgorithmName(), image.getAlgorithmResult(), 10, 15, "");
		}
		
		result.showDialog();
	}
	
	private void loadImages() {
		try {
			result1 = datasetIOService.open(file1.getAbsolutePath());
		} catch (final IOException e) {
			// log.error(e);
			return;
		}

		try {
			result2 = datasetIOService.open(file2.getAbsolutePath());
		} catch (final IOException e) {
			// log.error(e);
			return;
		}
		
		convert();
	}
	
	private void convert() {
		convertResultToImagePlus();
		convertImagePlusToImageProcessor();
	}
	
	private void convertResultToImagePlus() {
		image1 = convertDataSetToImagePlus(result1);
		image2 = convertDataSetToImagePlus(result2);
	}
	
	private void convertImagePlusToImageProcessor() {
		imageProcessor1 = image1.getProcessor();
		imageProcessor2 = image2.getProcessor();
	}
	
	private ImagePlus convertDataSetToImagePlus(Dataset dataset) {
		return convertService.convert(dataset, ImagePlus.class);
	}
	
	
	/**
	 * This main function serves for development purposes. It allows you to run the
	 * plugin immediately out of your integrated development environment (IDE).
	 *
	 * @param args
	 *            whatever, it's ignored
	 * @throws Exception
	 */
	public static void main(final String... args) throws Exception {
		// create the ImageJ application context with all available services
		// Debug.run("GaussFiltering", "plugin parameters");
		final ImageJ ij = new ImageJ();
		ij.ui().showUI();

		// invoke the plugin
		ij.command().run(ImageQuality.class, true);
	}

}
