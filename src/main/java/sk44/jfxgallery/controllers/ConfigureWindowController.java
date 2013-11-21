/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sk44.jfxgallery.models.Config;
import sk44.jfxgallery.models.ViewerMode;

/**
 *
 * @author sk
 */
public class ConfigureWindowController implements Initializable {

	@FXML
	private Pane rootPane;
	@FXML
	private TextField pathField;
	@FXML
	private TextField backgroundImageField;
	@FXML
	private ComboBox<String> viewerModeComboBox;
	private Pane parent;
	private FileChooser fileChooser;
	private DirectoryChooser directoryChooser;

	@FXML
	protected void handleBrowseAction(ActionEvent event) {
		File dir = directoryChooser.showDialog(null);
		if (dir != null) {
			pathField.setText(dir.getAbsolutePath());
		}
	}

	@FXML
	protected void handleBrowseImageFileAction(ActionEvent event) {
		File f = fileChooser.showOpenDialog(null);
		if (f != null) {
			backgroundImageField.setText(f.getAbsolutePath());
		}
	}

	@FXML
	protected void handleSaveAction(ActionEvent event) {

		// TODO validation とメッセージ出すとか
		if (pathField.getText() == null) {
			return;
		}
		File path = new File(pathField.getText());
		if (path.exists() == false) {
			return;
		}
		File background = new File(backgroundImageField.getText());
		Config.load().updateUserSettings(
			path,
			ViewerMode.modeOfName(viewerModeComboBox.getSelectionModel().getSelectedItem()),
			background);
		close();
	}

	@FXML
	protected void handleCancelAction(ActionEvent event) {
		close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		directoryChooser = new DirectoryChooser();
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image", "*.jpg", "*.jpeg", "*.png", "*.gif"));

		Config config = Config.load();
		pathField.setText(config.getStartupPath().getAbsolutePath());
		viewerModeComboBox.getItems().clear();
		viewerModeComboBox.getItems().addAll(ViewerMode.namesOfAllMode());
		viewerModeComboBox.getSelectionModel().select(config.getViewerMode().getName());
		Path imagePath = config.getBackgroundImagePath();
		if (imagePath != null) {
			backgroundImageField.setText(imagePath.toString());
		}
	}

	void showOn(Pane parent) {
		this.parent = parent;

		rootPane.prefHeightProperty().bind(parent.heightProperty());
		rootPane.prefWidthProperty().bind(parent.widthProperty());

		parent.getChildren().add(rootPane);
		pathField.requestFocus();
	}

	void close() {
		parent.getChildren().remove(rootPane);
	}

}
