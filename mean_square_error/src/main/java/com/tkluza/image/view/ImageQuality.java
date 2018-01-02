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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

import com.tkluza.image.algorithms.MeanSquareError;
import com.tkluza.image.algorithms.MeanStructuralSimilarity;
import com.tkluza.image.algorithms.PeakSignalNoiseRatio;
import com.tkluza.image.algorithms.StructuralSimilarity;
import com.tkluza.image.model.Image;
import com.tkluza.image.model.ImageQualityResult;
import com.tkluza.tool.Constraint.Device;
import com.tkluza.tool.Constraint.Mode;
import com.tkluza.tool.Constraint.QualityAlgorithm;
import com.tkluza.tool.Constraint.Scenario;
import com.tkluza.tool.Dictionary;
import com.tkluza.tool.ExcelTool;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.process.ImageProcessor;
import io.scif.services.DatasetIOService;
import net.imagej.Dataset;
import net.imagej.ImageJ;
import net.imagej.ops.OpService;
import net.imglib2.type.numeric.RealType;

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
	private String[] algorithmNames = { "MSE", "PSNR", "SSIM", "MSSSIM" };
	private boolean[] defaultState = { true, true, true, true };
	private ArrayList<com.tkluza.image.model.ImageQualityResult> results = new ArrayList<>();
	private ExcelTool excelTool;
	private Dictionary dictionary;

	private MeanSquareError mse;
	private PeakSignalNoiseRatio psnr;
	private StructuralSimilarity ssim;
	private MeanStructuralSimilarity mssim;
	private int algorithmCounter = 0;

	@Override
	public void run() {
		dictionary = new Dictionary();
		dictionary.init();
		initDialog();
		processImages();
		showResults();
		saveResults();
	}

	private void initDialog() {
		dialog = new GenericDialog("Image quality algorithms:");
		dialog.addCheckboxGroup(algorithmNames.length, 2, algorithmNames, defaultState);
		dialog.showDialog();
	}

	private void processImages() {
		for (Map.Entry<Device, List<Image>> entry : dictionary.entrySet()) {
			for (Map.Entry<Device, List<Image>> entry2 : dictionary.entrySet().stream()
					.filter(x -> x.getKey() != entry.getKey()).collect(Collectors.toSet())) {
				entry.getValue().stream()
						.forEach(img1 -> entry2.getValue().stream()
								.filter(img2 -> img1.getScenarioName() == img2.getScenarioName()).findFirst()
								.ifPresent(img2 -> processChosenImages(img1, img2)));
			}
		}
	}

	private void processImages2() {
		loadImages();
		getChosenAlgorithms(Device.UNDEFINED, Device.UNDEFINED, Scenario.UNDEFINED);
	}

	private void processChosenImages(Image img1, Image img2) {
		file1 = img1.getImage();
		file2 = img2.getImage();
		loadImages();
		algorithmCounter++;
		System.out.println(String.format("Algorithm counter: %s / %s", algorithmCounter, dictionary.size()));
		getChosenAlgorithms(img1.getDevice(), img2.getDevice(), img1.getScenarioName());
	}

	private void getChosenAlgorithms(Device device1, Device device2, Scenario scenario) {
		ArrayList<Checkbox> checkboxes = new ArrayList<>(dialog.getCheckboxes());
		for (int i = 0; i < checkboxes.size(); i++) {
			Checkbox cbox = checkboxes.get(i);
			if (cbox.getState()) {
				ImageQualityResult result = new ImageQualityResult(device1, device2, scenario);
				System.out.println("Image algorithm: " + i + " Devices: " + result.getReferenceModel() + ", "
						+ result.getTestModel() + " Scenario:" + result.getScenario());
				getAlgorithm(QualityAlgorithm.getAlgorithm(i), result);
			}
		}
	}

	private void getAlgorithm(QualityAlgorithm algorithm, ImageQualityResult qualityResult) {
		switch (algorithm) {
		case MSE:
			mse = new MeanSquareError();
			mse.evaluate(imageProcessor1, imageProcessor2);
			qualityResult.setAlgorithmAndResult(QualityAlgorithm.MSE, mse.getAlgorithmResult());
			break;
		case PSNR:
			psnr = new PeakSignalNoiseRatio();
			psnr.evaluate(imageProcessor1, imageProcessor2);
			qualityResult.setAlgorithmAndResult(QualityAlgorithm.PSNR, psnr.getAlgorithmResult());
			break;
		case SSIM:
			ssim = new StructuralSimilarity(image1, image2, Mode.DEFAULT);
			ssim.invoke();
			qualityResult.setAlgorithmAndResult(QualityAlgorithm.SSIM, ssim.getAlgorithmResult());
			break;
		case MSSSIM:
			mssim = new MeanStructuralSimilarity(image1, image2, Mode.DEFAULT);
			mssim.invoke();
			qualityResult.setAlgorithmAndResult(QualityAlgorithm.MSSSIM, mssim.getAlgorithmResult());
			break;
		default:
			System.out.println("Selected algorithm is not supported");
		}

		results.add(qualityResult);
	}

	private void showResults() {
		GenericDialog result = new GenericDialog("RESULTS of Image Algorithms");
		for (ImageQualityResult image : results) {
			result.addNumericField(image.getAlgorithm().toString(), image.getAlgorithmResult(), 10, 15, "");
		}

		result.showDialog();
	}

	private void saveResults() {
		excelTool = new ExcelTool();
		excelTool.init();
		ArrayList<ImageQualityResult> resultsLocal = new ArrayList<>();
		resultsLocal.add(new ImageQualityResult(Device.IPHONE_5, Device.IPHONE_7, Scenario.ARCHITECTURE,
				QualityAlgorithm.MSSSIM, 233));
		excelTool.writeResultList(results);
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
