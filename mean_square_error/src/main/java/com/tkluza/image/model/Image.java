package com.tkluza.image.model;

import java.io.File;

import com.tkluza.tool.Constraint.Device;
import com.tkluza.tool.Constraint.Scenario;

public class Image {

	private File image;
	private Scenario scenarioName;
	private Device device;

	public Image(File image, Scenario scenarioName, Device device) {
		this.image = image;
		this.scenarioName = scenarioName;
		this.device = device;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public Scenario getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(Scenario scenarioName) {
		this.scenarioName = scenarioName;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
