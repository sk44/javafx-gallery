/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sk
 */
public enum ViewerMode {

	SINGLE("1", "Single View") {

			@Override
			public String fxmlPath() {
				return "/views/imageWindow.fxml";
			}

		},
	SEPARATE("2", "Separated View") {

			@Override
			public String fxmlPath() {
				return "/views/separatedImageWindow.fxml";
			}

		};

	public static ViewerMode defaultMode() {
		return SINGLE;
	}

	public static List<String> namesOfAllMode() {
		List<String> results = new ArrayList<>(ViewerMode.values().length);
		for (ViewerMode mode : ViewerMode.values()) {
			results.add(mode.getName());
		}
		return results;
	}

	public static ViewerMode modeOfId(String id) {
		for (ViewerMode mode : ViewerMode.values()) {
			if (mode.id.equals(id)) {
				return mode;
			}
		}
		return null;
	}

	public static ViewerMode modeOfName(String name) {
		for (ViewerMode mode : ViewerMode.values()) {
			if (mode.getName().equals(name)) {
				return mode;
			}
		}
		return null;
	}

	private final String id;
	private final String name;

	private ViewerMode(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public abstract String fxmlPath();

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
