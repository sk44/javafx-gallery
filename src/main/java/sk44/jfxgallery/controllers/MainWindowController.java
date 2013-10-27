/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import sk44.jfxgallery.controllers.ConfigureWindowController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import sk44.jfxgallery.models.Config;
import sk44.jfxgallery.models.ImageWindowArgs;
import sk44.jfxgallery.models.PathModel;

/**
 *
 * @author sk
 */
public class MainWindowController implements Initializable {

	private final ObservableList<PathModel> directories = FXCollections.observableArrayList();
	private Path currentPath;
	private LoadImageTask loadImageTask;
	@FXML
	private TextField currentPathTextField;
	@FXML
	private ScrollPane flowScrollPane;
	@FXML
	private TilePane thumbnails;
	@FXML
	private ListView<PathModel> directoryView;
	@FXML
	private AnchorPane rootPane;

	@FXML
	protected void handleKeyPressedOnList(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER) {
			moveToSelectedDirectory();
		}
	}

	@FXML
	protected void handleConfigureAction(ActionEvent event) {
		FXMLLoader loader = new FXMLLoader(getClass()
			.getResource("/views/configureWindow.fxml"));
		try {
			loader.load();
		} catch (IOException ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
			return;
		}
		ConfigureWindowController window = loader.getController();
		window.showOn(rootPane);
	}

	@FXML
	protected void handleMoveToAction(ActionEvent event) {
		Path moveTo = new File(currentPathTextField.getText()).toPath();
		if (Files.exists(moveTo) == false) {
			System.err.println("moveTo " + moveTo.toString() + " does not exist.");
			return;
		}
		moveTo(moveTo);
	}

	@FXML
	protected void handleDirectoriesMouseClicked(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			moveToSelectedDirectory();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		directoryView.setCellFactory(new Callback<ListView<PathModel>, ListCell<PathModel>>() {
			@Override
			public ListCell<PathModel> call(ListView<PathModel> p) {
				return new ListCell<PathModel>() {
					@Override
					protected void updateItem(PathModel t, boolean empty) {
						super.updateItem(t, empty);
						if (empty == false) {
							setText(t.getName());
						}
					}
				};
			}
		});
		currentPathTextField.prefWidthProperty().bind(rootPane.widthProperty().subtract(180));
		// flowPane が回りこまなくなるのでバインドしとく
		thumbnails.prefWidthProperty().bind(flowScrollPane.widthProperty());

		moveTo(Config.load().getStartupPath().toPath());
	}

	private void moveToSelectedDirectory() {
		final PathModel selectedItem = directoryView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			return;
		}
		moveTo(selectedItem.getPath());
	}

	private void moveTo(Path path) {
		currentPath = path.toAbsolutePath().normalize();
		currentPathTextField.setText(currentPath.toString());
		directories.clear();

		if (currentPath.getParent() != null) {
			// 上へ
			directories.add(PathModel.parentModelForPath(path.getParent()));
		}
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)) {
			for (Path entry : stream) {
				if (Files.isDirectory(entry)) {
					directories.add(PathModel.modelForPath(entry));
				}
			}
			// TODO sort
			directoryView.setItems(directories);
			loadImages();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void loadImages() {
		// 画像ロード中に移動する場合、いったんキャンセル
		if (loadImageTask != null && loadImageTask.isDone() == false) {
			System.out.println("task will cancel.");
			loadImageTask.onCancelled = new Runnable() {
				@Override
				public void run() {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							startLoadImages();
						}
					});
				}
			};
			loadImageTask.cancel();
			loadImageTask = null;
			System.out.println("task cancel completed.");
			return;
		}
		startLoadImages();
	}

	private void startLoadImages() {

		thumbnails.getChildren().clear();
		// ファイルが大量にある場合遅いので別スレッドで読み込み.
		loadImageTask = new LoadImageTask();
		Thread t = new Thread(loadImageTask);
		t.setDaemon(true);
		t.start();
	}

	class LoadImageTask extends Task<Void> {

		Runnable onCancelled;

		@Override
		protected Void call() throws Exception {
			System.out.println("loading start.");

			try (DirectoryStream<Path> stream = Files
				.newDirectoryStream(currentPath, "*.{jpg,jpeg,png,gif}")) {

				int index = 0;
				for (final Path entry : stream) {
					if (isCancelled()) {
						System.out.println("task cancelled.");
						if (onCancelled != null) {
							onCancelled.run();
						}
						break;
					}
					if (Files.isDirectory(entry)) {
						continue;
					}
					final Image image = new Image(Files.newInputStream(entry), 120.0, 100.0, true, true);
					final ImageView imageView = new ImageView(image);
					imageView.setCache(true);
					// TODO 高さが揃わない
					imageView.setFitHeight(100.0);
					imageView.setFitWidth(120.0);
					imageView.setPreserveRatio(true);
					final String fileName = entry.getFileName().toString();

					final Button button = new Button(fileName);
					button.setPrefHeight(110.0);
					button.setPrefWidth(130.0);
					button.setGraphic(imageView);
					button.setContentDisplay(ContentDisplay.TOP);
					button.setTextAlignment(TextAlignment.JUSTIFY);
					button.getStyleClass().add("thumbnail");

					final int current = index;
					button.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent t) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									openImage(
										new ImageWindowArgs(
											entry.toAbsolutePath(),
											current,
											new ImageWindowArgs.IndexToPathFunction() {
												@Override
												public Path pathOfIndex(int index) {
													// TODO いろいろつらい
													List<Node> labels = thumbnails.getChildren();
													if (index < 0 || labels.size() - 1 < index) {
														return null;
													}
													Button l = (Button) labels.get(index);
													return new File(currentPath.toFile(),
														l.getText()).toPath();
												}
											}));
								}
							});
						}
					});

					Platform.runLater(
						new Runnable() {
							@Override
							public void run() {
								// new Tooltip はメインスレッドで動かす必要があるっぽい
								// http://stackoverflow.com/questions/13864182/create-new-tooltip-on-not-javafx-application-thread
								final Tooltip tooltip = new Tooltip(fileName);
								tooltip.getStyleClass().add("filenameTooltip");
								button.setTooltip(tooltip);
								button.setGraphic(imageView);
								thumbnails.getChildren().add(button);
							}
						});
					index++;
				}
			} catch (IOException ex) {
				Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
			}

			return null;
		}
	}

	private void openImage(ImageWindowArgs args) {
		Config config = Config.load();
		try {
			FXMLLoader loader = new FXMLLoader(getClass()
				.getResource(config.getViewerMode().fxmlPath()));
			loader.load();
			ViewerController imageWindow = loader.getController();
			imageWindow.showOn(rootPane, args);
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		}
	}

}
