/*
 * To the extent possible under law, the ImageJ developers have waived
 * all copyright and related or neighboring rights to this tutorial code.
 *
 * See the CC0 1.0 Universal license for details:
 *     http://creativecommons.org/publicdomain/zero/1.0/
 */

package com.mycompany.imagej;

import java.io.File;
import java.io.IOException;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.RealType;

/**
 * This example illustrates how to create an ImageJ {@link Command} plugin.
 * <p>
 * The code here is a simple Gaussian blur using ImageJ Ops.
 * </p>
 * <p>
 * You should replace the parameter fields with your own inputs and outputs, and
 * replace the {@link run} method implementation with your own logic.
 * </p>
 */
@Plugin(type = Command.class, menuPath = "Plugins>MSE")
public class MeanSquareError<T extends RealType<T>> implements Command {
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

	@Override
	public void run() {
		Dataset dataset1, dataset2;
		try {
			dataset1 = datasetIOService.open(file1.getAbsolutePath());
		} catch (final IOException e) {
			// log.error(e);
			return;
		}

		try {
			dataset2 = datasetIOService.open(file2.getAbsolutePath());
		} catch (final IOException e) {
			// log.error(e);
			return;
		}

		ImagePlus image1 = convertDataset(dataset1);
		ImagePlus image2 = convertDataset(dataset2);

		double sum = mse(image1.getProcessor(), image2.getProcessor());

		IJ.showMessage("MSE = " + sum);
	}

	private double mse(ImageProcessor imageP1, ImageProcessor imageP2) {
		int sum = 0;
		double mse = 0;
		for (int y = 0; y < imageP1.getHeight(); y++) {
			for (int x = 0; x < imageP1.getWidth(); x++) {
				int p1 = imageP1.getPixel(y, x);
				int p2 = imageP2.getPixel(y, x);
				int err = p2 - p1;
				sum += (err * err);
			}
		}

		return mse = (double) sum / (imageP1.getHeight() * imageP1.getWidth());
	}

	private ImagePlus convertDataset(Dataset dataset) {
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
		// ij.ui().showUI();

		// invoke the plugin
		ij.command().run(MeanSquareError.class, true);
	}

}
