/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.models;

/**
 *
 * @author sk
 */
public enum ViewerMode {

	SINGLE("1") {

			@Override
			public String fxmlPath() {
				return "/views/imageWindow.fxml";
			}

		},
	SEPARATE("2") {

			@Override
			public String fxmlPath() {
				return "/views/separatedImageWindow.fxml";
			}

		};

	public static ViewerMode defaultMode() {
		return SINGLE;
	}

	public static ViewerMode modeOfId(String id) {
		for (ViewerMode mode : ViewerMode.values()) {
			if (mode.id.equals(id)) {
				return mode;
			}
		}
		return null;
	}

	private final String id;

	private ViewerMode(String id) {
		this.id = id;
	}

	public abstract String fxmlPath();

	public String getId() {
		return id;
	}

}
