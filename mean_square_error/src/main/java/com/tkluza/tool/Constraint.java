package com.tkluza.tool;

import java.util.Arrays;

public class Constraint {

	public static final String SOURCE_FOLDER = "D:\\Praca magisterska\\Badania\\Final";
	public static final String EXCEL_FILE_SOURCE_PATH = "D:\\Praca magisterska\\Badania\\results.xls";

	public enum Mode {
		DEFAULT(1), OTHER(2);
		private int value;

		private Mode(int value) {
			this.value = value;
		}
	};

	public enum Scenario {
		UNDEFINED(0), OBJECT(1), ARCHITECTURE(2), MACRO(3), MOVE(4), INDOOR_MACRO(5), INDOOR(6), LOW_LIGHT(7), NIGHT(8);

		private final int scenarioNumber;
		private static final Scenario[] scenarios = values();

		Scenario(int scenarioNumber) {
			this.scenarioNumber = scenarioNumber;
		}

		public static Scenario getScenario(int number) {
			return Arrays.stream(scenarios).filter(x -> x.scenarioNumber == number).findFirst().orElse(UNDEFINED);
		}

		public int getScenarioNumber() {
			return scenarioNumber;
		}
	}

	public enum Device {
		UNDEFINED(""), SAMSUNG_S8("Samsung S8"), XIAOMI_MI5("Xiaomi mi5"), IPHONE_5("iPhone 5"), IPHONE_7(
				"iPhone 7"), NIKON("Nikon");

		private final String deviceName;
		private static final Device[] devices = values();

		Device(String deviceName) {
			this.deviceName = deviceName;
		}

		public static Device getDevice(String deviceName) {
			return Arrays.stream(devices).filter(x -> x.deviceName.equalsIgnoreCase(deviceName)).findFirst()
					.orElse(UNDEFINED);
		}

		public String getDeviceName() {
			return deviceName;
		}
	}

	public enum QualityAlgorithm {
		UNDEFINED(-1), MSE(0), PSNR(1), SSIM(2), MSSSIM(3);

		private final int algorithmNumber;
		private static final QualityAlgorithm[] algorithms = values();

		QualityAlgorithm(int algorithmNumber) {
			this.algorithmNumber = algorithmNumber;
		}

		public static QualityAlgorithm getAlgorithm(int number) {
			return Arrays.stream(algorithms).filter(x -> x.algorithmNumber == number).findFirst().orElse(UNDEFINED);
		}

		public int getAlgorithmNumber() {
			return algorithmNumber;
		}
	}

	static final String[] imageExtensions = new String[] { "png", "jpg" };
}
