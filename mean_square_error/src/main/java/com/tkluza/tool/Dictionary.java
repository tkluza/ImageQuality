package com.tkluza.tool;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import com.tkluza.image.model.Image;
import com.tkluza.tool.Constraint.Device;
import com.tkluza.tool.Constraint.Scenario;

public class Dictionary extends HashMap<Device, List<Image>> {

	public Dictionary() {
	}

	public void init() {
		loadImagesFromFolder();
	}

	@Override
	public int size() {
		int size = 0;
		for (Map.Entry<Device, List<Image>> entry : this.entrySet()) {
			size += entry.getValue().size();
		}
		return size;
	}

	private void loadImagesFromFolder() {
		try {
			Files.list(Paths.get(Constraint.SOURCE_FOLDER)).forEach(x -> addToDictionary(x));
		} catch (Exception e) {
		}
	}

	private void addToDictionary(Path path) {
		File file = path.toFile();
		if (file.isDirectory()) {
			addParentToDirectory(file);
		} else if (file.isFile()) {
			addFileToDirectory(file);
		}
	}

	private void addParentToDirectory(File file) {
		FilenameFilter filter = ((dir, name) -> {
			return dir.isDirectory() ? Arrays.stream(Constraint.imageExtensions)
					.anyMatch(x -> name.toLowerCase().endsWith(x.toLowerCase())) : true;
		});
		File[] files = file.listFiles(filter);
		Device device = Device.getDevice(file.getName());
		if (!this.containsKey((device)))
			this.put(device, new ArrayList<Image>());
		Arrays.stream(files).forEach(image -> addFileToDirectory(image));
	}

	private void addFileToDirectory(File file) {
		try {
			int name = Integer.parseInt(FilenameUtils.removeExtension(file.getName()));
			Image image = new Image(file, Scenario.getScenario(name), Device.getDevice(file.getParentFile().getName()));
			if (this.containsKey(image.getDevice()))
				this.get(image.getDevice()).add(image);

		} catch (Exception e) {
			System.out.println("Error while adding image");
		}
	}
}