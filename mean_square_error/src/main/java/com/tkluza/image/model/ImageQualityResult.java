package com.tkluza.image.model;

import com.tkluza.tool.Constraint.Device;
import com.tkluza.tool.Constraint.QualityAlgorithm;
import com.tkluza.tool.Constraint.Scenario;

public class ImageQualityResult {
	private Device referenceModel, testModel;
	private Scenario scenario;
	private QualityAlgorithm algorithm;
	private double algorithmResult;

	public ImageQualityResult(Device referenceModel, Device testModel, Scenario scenario) {
		super();
		this.referenceModel = referenceModel;
		this.testModel = testModel;
		this.scenario = scenario;
	}

	public ImageQualityResult(Device referenceModel, Device testModel, Scenario scenario, QualityAlgorithm algorithm,
			double algorithmResult) {
		this(referenceModel, testModel, scenario);
		this.referenceModel = referenceModel;
		this.testModel = testModel;
		this.scenario = scenario;
		this.algorithm = algorithm;
		this.algorithmResult = algorithmResult;
	}

	public void setAlgorithmAndResult(QualityAlgorithm algorithm, double result) {
		setAlgorithm(algorithm);
		setAlgorithmResult(result);
	}

	public Device getReferenceModel() {
		return referenceModel;
	}

	public Device getTestModel() {
		return testModel;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public QualityAlgorithm getAlgorithm() {
		return algorithm;
	}

	public double getAlgorithmResult() {
		return algorithmResult;
	}

	public void setReferenceModel(Device referenceModel) {
		this.referenceModel = referenceModel;
	}

	public void setTestModel(Device testModel) {
		this.testModel = testModel;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public void setAlgorithm(QualityAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public void setAlgorithmResult(double algorithmResult) {
		this.algorithmResult = algorithmResult;
	}

}
