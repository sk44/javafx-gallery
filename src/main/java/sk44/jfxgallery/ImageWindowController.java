/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery;

import sk44.jfxgallery.models.ImageWindowArgs;
import sk44.jfxgallery.views.ImageViewPane;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author sk
 */
public class ImageWindowController implements Initializable {

	@FXML
	private BorderPane rootPane;
	@FXML
	private Label imageNameLabel;
	@FXML
	private Button buttonPrevious;
	@FXML
	private Button buttonNext;
	private final ImageViewPane imageViewPane = new ImageViewPane();
	private ImageWindowArgs args;
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
		if (args.previousFileExistsProperty().get() == false) {
			return;
		}
		args.previous();
		loadImage();
	}

	private void showNext() {
		if (args.nextFileExistsProperty().get() == false) {
			return;
		}
		args.next();
		loadImage();
	}

	void showOn(Pane parent, ImageWindowArgs args) {

		this.args = args;
		this.parent = parent;

		rootPane.prefHeightProperty().bind(parent.heightProperty());
		rootPane.prefWidthProperty().bind(parent.widthProperty());
		buttonNext.visibleProperty().bind(args.nextFileExistsProperty());
		buttonPrevious.visibleProperty().bind(args.previousFileExistsProperty());

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
		rootPane.setCenter(imageViewPane);
	}

	private void loadImage() {
		if (args.isCurrentPathExists() == false) {
			System.out.println("current path does not exists.");
			return;
		}
		// TODO バインドする
		imageNameLabel.setText(args.currentPath().getFileName().toString());

		try {
			Image image = new Image(Files.newInputStream(args.currentPath()));
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setSmooth(true);
			imageView.setCache(true);
			imageView.setPreserveRatio(true);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
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
