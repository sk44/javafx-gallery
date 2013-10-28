/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sk44.jfxgallery.models.ImageWindowArgs;
import sk44.jfxgallery.views.ImageViewPane;

/**
 *
 * @author sk
 */
public class SeparatedImageWindowController implements Initializable, ViewerController {

	@FXML
	private AnchorPane rootPane;
	@FXML
	private AnchorPane leftContainer;
	@FXML
	private AnchorPane rightContainer;
	private final ImageViewPane leftImageViewPane = new ImageViewPane(HPos.RIGHT);
	private final ImageViewPane rightImageViewPane = new ImageViewPane(HPos.LEFT);
	private Pane parent;
	private ImageWindowArgs leftImages;
	private ImageWindowArgs rightImages;

	@FXML
	protected void handleKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
			case J:
				if (event.isShiftDown()) {
					showNextHalf();
				} else {
					showNext();
				}
				break;
			case K:
				if (event.isShiftDown()) {
					showPreviousHalf();
				} else {
					showPrevious();
				}
				break;
			case ESCAPE:
				close();
				break;
			default:
				break;
		}
	}

	private void showNext() {
		if (leftImages.isNextFileExists() == false) {
			return;
		}
		rightImages.next();
		rightImages.next();
		loadRightImage();

		leftImages.next();
		leftImages.next();
		if (leftImages.isCurrentPathExists() == false) {
			leftImageViewPane.setImageView(null);
			return;
		}
		loadLeftImage();
	}

	private void showNextHalf() {
		if (leftImages.isNextFileExists() == false) {
			return;
		}
		rightImages.next();
		loadRightImage();

		leftImages.next();
		loadLeftImage();
	}

	private void showPrevious() {
		if (rightImages.isPreviousFileExists() == false) {
			return;
		}
		leftImages.previous();
		leftImages.previous();
		loadLeftImage();

		rightImages.previous();
		rightImages.previous();
		if (rightImages.isCurrentPathExists() == false) {
			rightImageViewPane.setImageView(null);
			return;
		}
		loadRightImage();
	}

	private void showPreviousHalf() {
		if (rightImages.isPreviousFileExists() == false) {
			return;
		}
		leftImages.previous();
		loadLeftImage();

		rightImages.previous();
		loadRightImage();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		rightImageViewPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				showPrevious();
			}
		});
		leftImageViewPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				showNext();
			}
		});
	}

	@Override
	public void showOn(Pane parent, ImageWindowArgs param) {
		this.parent = parent;
		this.rightImages = param;
		this.leftImages = ImageWindowArgs.Next(param);

		rootPane.prefHeightProperty().bind(parent.heightProperty());
		rootPane.prefWidthProperty().bind(parent.widthProperty());

		leftContainer.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
		rightContainer.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));

		AnchorPane.setRightAnchor(leftImageViewPane, 0.0);
		AnchorPane.setLeftAnchor(leftImageViewPane, 0.0);
		AnchorPane.setTopAnchor(leftImageViewPane, 0.0);
		AnchorPane.setBottomAnchor(leftImageViewPane, 0.0);
		leftContainer.getChildren().add(leftImageViewPane);

		AnchorPane.setRightAnchor(rightImageViewPane, 0.0);
		AnchorPane.setLeftAnchor(rightImageViewPane, 0.0);
		AnchorPane.setTopAnchor(rightImageViewPane, 0.0);
		AnchorPane.setBottomAnchor(rightImageViewPane, 0.0);
		rightContainer.getChildren().add(rightImageViewPane);

		loadRightImage();
		loadLeftImage();

		parent.getChildren().add(rootPane);
		rootPane.requestFocus();
	}

	private void loadRightImage() {
		loadImageOn(rightImageViewPane, rightImages);
	}

	private void loadLeftImage() {
		loadImageOn(leftImageViewPane, leftImages);
	}

	private static void loadImageOn(ImageViewPane imageViewPane, ImageWindowArgs images) {

		if (images.isCurrentPathExists() == false) {
			System.out.println("current path does not exists.");
			return;
		}
		try {
			Image image = new Image(Files.newInputStream(images.currentPath()));
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setSmooth(true);
			imageView.setCache(true);
			imageView.setPreserveRatio(true);
			imageViewPane.setImageView(imageView);
		} catch (IOException ex) {
			Logger.getLogger(ImageWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void close() {
		parent.getChildren().remove(rootPane);
	}

}
