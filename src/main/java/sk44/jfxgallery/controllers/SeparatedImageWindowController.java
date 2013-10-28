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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
	@FXML
	private BorderPane leftBorder;
	@FXML
	private BorderPane rightBorder;
	private final ImageViewPane leftImageViewPane = new ImageViewPane();
	private final ImageViewPane rightImageViewPane = new ImageViewPane();
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
		loadImageOn(rightImageViewPane, rightImages);

		leftImages.next();
		leftImages.next();
		if (leftImages.isCurrentPathExists() == false) {
			leftImageViewPane.setImageView(null);
			return;
		}
		loadImageOn(leftImageViewPane, leftImages);
	}

	private void showNextHalf() {
		if (leftImages.isNextFileExists() == false) {
			return;
		}
		rightImages.next();
		loadImageOn(rightImageViewPane, rightImages);

		leftImages.next();
		loadImageOn(leftImageViewPane, leftImages);
	}

	private void showPrevious() {
		if (rightImages.isPreviousFileExists() == false) {
			return;
		}
		leftImages.previous();
		leftImages.previous();
		loadImageOn(leftImageViewPane, leftImages);

		rightImages.previous();
		rightImages.previous();
		if (rightImages.isCurrentPathExists() == false) {
			rightImageViewPane.setImageView(null);
			return;
		}
		loadImageOn(rightImageViewPane, rightImages);
	}

	private void showPreviousHalf() {
		if (rightImages.isPreviousFileExists() == false) {
			return;
		}
		leftImages.previous();
		loadImageOn(leftImageViewPane, leftImages);

		rightImages.previous();
		loadImageOn(rightImageViewPane, rightImages);
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

		// TODO 画像が AnchorPane からはみだしてしまう
//		leftContainer.getChildren().add(leftImageViewPane);
//		AnchorPane.setRightAnchor(leftImageViewPane, 0.0);
//		rightContainer.getChildren().add(rightImageViewPane);
//		AnchorPane.setLeftAnchor(rightImageViewPane, 0.0);
		leftBorder.setCenter(leftImageViewPane);
		rightBorder.setCenter(rightImageViewPane);
//		leftBorder.setRight(leftImageViewPane);
//		rightBorder.setLeft(rightImageViewPane);
		loadImageOn(rightImageViewPane, rightImages);
		loadImageOn(leftImageViewPane, leftImages);

		parent.getChildren().add(rootPane);
		rootPane.requestFocus();
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
