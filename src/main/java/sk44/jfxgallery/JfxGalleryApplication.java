package sk44.jfxgallery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class JfxGalleryApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Scene scene = new Scene((Pane) FXMLLoader
			.load(getClass().getResource("/views/mainWindow.fxml")));
		primaryStage.setScene(scene);
		primaryStage.setTitle("Gallery");
		primaryStage.show();
	}
}
