/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery;

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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 *
 * @author sk
 */
public class ImageWindowController implements Initializable {

	interface CloseHandler {

		void handleClose();
	}
	@FXML
	private BorderPane rootPane;
	@FXML
	private Label imageNameLabel;
	@FXML
	private Button buttonPrevious;
	@FXML
	private Button buttonNext;
	private ImageViewPane imageViewPane;
	private ImageWindowArgs args;
	private Pane basePane;
	private CloseHandler closeHandler;

	@FXML
	protected void handleNextAction(ActionEvent event) {
		showNext();
	}

	@FXML
	protected void handlePreviousAction(ActionEvent event) {
		showPrevious();
	}

	void showPrevious() {
		if (args.previousFileExistsProperty().get() == false) {
			return;
		}
		args.previous();
		loadImage();
	}

	void showNext() {
		if (args.nextFileExistsProperty().get() == false) {
			return;
		}
		args.next();
		loadImage();
	}

	void showOn(Pane basePane, ImageWindowArgs args, CloseHandler closeHandler) {
		this.args = args;
		this.basePane = basePane;
		this.closeHandler = closeHandler;

		rootPane.prefHeightProperty().bind(basePane.heightProperty());
		rootPane.prefWidthProperty().bind(basePane.widthProperty());
		buttonNext.disableProperty().bind(args.nextFileExistsProperty().not());
		buttonPrevious.disableProperty().bind(args.previousFileExistsProperty().not());

		loadImage();
		basePane.getChildren().add(rootPane);
	}

	void close() {
		basePane.getChildren().remove(rootPane);
		closeHandler.handleClose();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// なにかすることがあれば
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
			imageViewPane = new ImageViewPane(imageView);
			rootPane.setCenter(imageViewPane);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent t) {
					System.out.println("mouse clicked.");
					close();
				}
			});
			playAnimation(imageView);
		} catch (IOException ex) {
			Logger.getLogger(ImageWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void playAnimation(Node node) {
		// TODO みなおす
		FadeTransition fadeIn = new FadeTransition(Duration.millis(300));
		fadeIn.setFromValue(0.1);
		fadeIn.setToValue(1.0);
		fadeIn.setAutoReverse(true);
		fadeIn.setCycleCount(1);
		fadeIn.setNode(node);
		fadeIn.play();
	}
}
