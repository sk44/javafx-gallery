package sk44.jfxgallery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JfxGalleryApplication extends Application {

	private Stage stage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene((Pane) FXMLLoader
			.load(getClass().getResource("/views/mainWindow.fxml")));
		primaryStage.setScene(scene);
		primaryStage.setTitle("Gallery");
//		primaryStage.setWidth(500);
//		primaryStage.setHeight(400);
		stage = primaryStage;
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		System.out.println("full: " + stage.isFullScreen() + " width: " + stage.getWidth() + " height: " + stage.getHeight());
		System.out.println("x: " + stage.getX() + " y: " + stage.getY());
		super.stop();
	}

}
