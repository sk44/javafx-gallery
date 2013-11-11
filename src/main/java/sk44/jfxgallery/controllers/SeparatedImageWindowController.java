/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
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
import sk44.jfxgallery.views.PageTurnableImageViewPane;
import sk44.jfxgallery.views.TurnPageAnimation;

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
	private final PageTurnableImageViewPane leftImageViewPane = new PageTurnableImageViewPane(HPos.RIGHT, TurnPageAnimation.LEFT);
	private final PageTurnableImageViewPane rightImageViewPane = new PageTurnableImageViewPane(HPos.LEFT, TurnPageAnimation.RIGHT);
	// ページ繰り時に後ろに表示させる用
	private final ImageViewPane leftBackgroundImageViewPane = new ImageViewPane(HPos.RIGHT);
	private final ImageViewPane rightBackgroundImageViewPane = new ImageViewPane(HPos.LEFT);
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

	private static void turnPage(ImageWindowArgs fromImages,
		ImageViewPane fromBackgroundImageViewPane,
		PageTurnableImageViewPane fromImageViewPane,
		final ImageWindowArgs toImages,
		final ImageViewPane toBackgroundImageViewPane,
		final PageTurnableImageViewPane toImageViewPane,
		final boolean forward) {

		if (forward) {
			fromImages.turnPage();
		} else {
			fromImages.turnBackPage();
		}
		ImageView nextImageView;
		if (fromImages.isCurrentPathExists()) {
			nextImageView = createImageView(fromImages.currentPath());
			fromBackgroundImageViewPane.setImageView(nextImageView);
		} else {
			fromBackgroundImageViewPane.setImageView(null);
			nextImageView = null;
		}
		toBackgroundImageViewPane.setImageView(toImageViewPane.getImageView());
		fromImageViewPane.turnPageFirstHalf(nextImageView, new PageTurnableImageViewPane.TurnPageCallback() {
			@Override
			public void execute() {
				if (forward) {
					toImages.turnPage();
				} else {
					toImages.turnBackPage();
				}
				ImageView toImageView = createImageView(toImages.currentPath());
				toImageViewPane.turnPageSecondHalf(toImageView, new PageTurnableImageViewPane.TurnPageCallback() {
					@Override
					public void execute() {
						toBackgroundImageViewPane.setImageView(null);
					}
				});
			}
		});
	}

	private void showNext() {
		if (leftImages.isNextFileExists() == false) {
			return;
		}
		turnPage(leftImages, leftBackgroundImageViewPane, leftImageViewPane, rightImages, rightBackgroundImageViewPane, rightImageViewPane, true);
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
		turnPage(rightImages, rightBackgroundImageViewPane, rightImageViewPane, leftImages, leftBackgroundImageViewPane, leftImageViewPane, false);
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
		leftContainer.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));

		AnchorPane.setRightAnchor(leftBackgroundImageViewPane, 0.0);
		AnchorPane.setLeftAnchor(leftBackgroundImageViewPane, 0.0);
		AnchorPane.setTopAnchor(leftBackgroundImageViewPane, 0.0);
		AnchorPane.setBottomAnchor(leftBackgroundImageViewPane, 0.0);
		leftContainer.getChildren().add(leftBackgroundImageViewPane);

		AnchorPane.setRightAnchor(leftImageViewPane, 0.0);
		AnchorPane.setLeftAnchor(leftImageViewPane, 0.0);
		AnchorPane.setTopAnchor(leftImageViewPane, 0.0);
		AnchorPane.setBottomAnchor(leftImageViewPane, 0.0);
		leftContainer.getChildren().add(leftImageViewPane);

		rightContainer.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));

		AnchorPane.setRightAnchor(rightBackgroundImageViewPane, 0.0);
		AnchorPane.setLeftAnchor(rightBackgroundImageViewPane, 0.0);
		AnchorPane.setTopAnchor(rightBackgroundImageViewPane, 0.0);
		AnchorPane.setBottomAnchor(rightBackgroundImageViewPane, 0.0);
		rightContainer.getChildren().add(rightBackgroundImageViewPane);

		AnchorPane.setRightAnchor(rightImageViewPane, 0.0);
		AnchorPane.setLeftAnchor(rightImageViewPane, 0.0);
		AnchorPane.setTopAnchor(rightImageViewPane, 0.0);
		AnchorPane.setBottomAnchor(rightImageViewPane, 0.0);
		rightContainer.getChildren().add(rightImageViewPane);

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

	private static Image loadImage(Path imagePath) {
		try {
			return new Image(Files.newInputStream(imagePath));
		} catch (IOException ex) {
			Logger.getLogger(SeparatedImageWindowController.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
	}

	private static ImageView createImageView(Path imagePath) {
		Image image = loadImage(imagePath);
		if (image == null) {
			return null;
		}
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setSmooth(true);
		imageView.setCache(true);
		imageView.setPreserveRatio(true);
		return imageView;

	}

	private static void loadImageOn(ImageViewPane imageViewPane, ImageWindowArgs images) {

		ImageView imageView = createImageView(images.currentPath());
		if (imageView == null) {
			return;
		}
		imageViewPane.setImageView(imageView);
	}

	void close() {
		parent.getChildren().remove(rootPane);
	}

}
