package sk44.jfxgallery;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sk44.jfxgallery.models.Config;

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
		Config config = Config.load();
		primaryStage.setWidth(config.getWindowWidth());
		primaryStage.setHeight(config.getWindowHeight());
		primaryStage.setFullScreen(config.isFullScreen());
		primaryStage.setX(config.getWindowX());
		primaryStage.setY(config.getWindowY());
		stage = primaryStage;
		primaryStage.show();
	}

	@Override
	public void stop() throws Exception {
		Config config = Config.load();
		config.updateWindowSettings(stage.getHeight(), stage.getWidth(),
			stage.getX(), stage.getY(), stage.isFullScreen());
		super.stop();
	}

}
