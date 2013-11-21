/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import sk44.jfxgallery.models.Config;
import sk44.jfxgallery.models.ImagePager;
import sk44.jfxgallery.views.ImageViewPane;

/**
 *
 * @author sk
 */
public class ImageWindowController implements Initializable, ViewerController {

	@FXML
	private Pane rootPane;
	@FXML
	private BorderPane imageContainer;
	@FXML
	private Label imageNameLabel;
	@FXML
	private Button buttonPrevious;
	@FXML
	private Button buttonNext;
	private final ImageViewPane imageViewPane = new ImageViewPane(HPos.CENTER);
	private ImagePager pager;
	private Pane parent;

	@FXML
	protected void handleNextAction(ActionEvent event) {
		showNext();
	}

	@FXML
	protected void handlePreviousAction(ActionEvent event) {
		showPrevious();
	}

	@FXML
	protected void handleKeyPressed(KeyEvent event) {
		switch (event.getCode()) {
			case J:
				showNext();
				break;
			case K:
				showPrevious();
				break;
			case ESCAPE:
				close();
				break;
			default:
				break;
		}
	}

	private void showPrevious() {
		if (pager.previousFileExistsProperty().get() == false) {
			return;
		}
		pager.previous();
		loadImage();
	}

	private void showNext() {
		if (pager.nextFileExistsProperty().get() == false) {
			return;
		}
		pager.next();
		loadImage();
	}

	@Override
	public void showOn(Pane parent, ImagePager param) {

		this.pager = param;
		this.parent = parent;

		rootPane.prefHeightProperty().bind(parent.heightProperty());
		rootPane.prefWidthProperty().bind(parent.widthProperty());
		buttonNext.visibleProperty().bind(pager.nextFileExistsProperty());
		buttonPrevious.visibleProperty().bind(pager.previousFileExistsProperty());

		loadImage();
		parent.getChildren().add(rootPane);
		// キーボードで親を操作できてしまうので
		rootPane.requestFocus();
	}

	void close() {
		parent.getChildren().remove(rootPane);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// AnchorPane.setXxxAnchor すれば AnchorPane でもいいかも
		imageContainer.setCenter(imageViewPane);
	}

	private void loadImage() {
		if (pager.isCurrentPathExists() == false) {
			System.out.println("current path does not exists.");
			return;
		}
		// TODO バインドする
		imageNameLabel.setText(pager.currentPath().getFileName().toString());

		try {
			Image image = new Image(Files.newInputStream(pager.currentPath()));
			final ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setSmooth(true);
			imageView.setCache(true);
			imageView.setPreserveRatio(true);

			final ContextMenu popup = new ContextMenu();
			MenuItem menu = new MenuItem("use as background");
			menu.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					Config.load().updateBackgroundImage(pager.currentPath());
				}
			});
			popup.getItems().add(menu);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					if (t.getButton() == MouseButton.SECONDARY) {
						popup.show(imageView, t.getScreenX(), t.getScreenY());
						return;
					}
					popup.hide();
					close();
				}
			});
			showImage(imageView);
		} catch (IOException ex) {
			Logger.getLogger(ImageWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void showImage(ImageView newImage) {

		imageViewPane.setImageView(newImage);

		// TODO みなおす
		FadeTransition fadeIn = new FadeTransition(Duration.millis(300));
		fadeIn.setFromValue(0.1);
		fadeIn.setToValue(1.0);
		fadeIn.setAutoReverse(true);
		fadeIn.setCycleCount(1);
		fadeIn.setNode(newImage);
		fadeIn.play();

	}
}
