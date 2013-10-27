/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
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
	private ComboBox<String> viewerModeComboBox;
	private Pane parent;

	@FXML
	protected void handleBrowseAction(ActionEvent event) {
		DirectoryChooser dc = new DirectoryChooser();
		File dir = dc.showDialog(null);
		if (dir != null) {
			pathField.setText(dir.getAbsolutePath());
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
		Config.update(path, ViewerMode.modeOfName(viewerModeComboBox.getSelectionModel().getSelectedItem()));
		close();
	}

	@FXML
	protected void handleCancelAction(ActionEvent event) {
		close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Config config = Config.load();
		pathField.setText(config.getStartupPath().getAbsolutePath());
		viewerModeComboBox.getItems().clear();
		viewerModeComboBox.getItems().addAll(ViewerMode.namesOfAllMode());
		viewerModeComboBox.getSelectionModel().select(config.getViewerMode().getName());
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
