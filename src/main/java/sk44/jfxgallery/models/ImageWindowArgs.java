/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.models;

import java.nio.file.Path;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author sk
 */
public class ImageWindowArgs {

	public interface IndexToPathFunction {

		Path pathOfIndex(int index);
	}

	public static ImageWindowArgs Next(ImageWindowArgs prev) {
		return new ImageWindowArgs(prev.getNextPath(), prev.currentIndex.get() + 1, prev.indexToPath);
	}
	private Path path;
	private final IntegerProperty currentIndex;
	private final IndexToPathFunction indexToPath;
	private final ReadOnlyBooleanProperty nextFileExists;
	private final ReadOnlyBooleanProperty previousFileExists;

	public ImageWindowArgs(Path path, int index, IndexToPathFunction indexToPath) {
		this.path = path;
		this.currentIndex = new SimpleIntegerProperty(index);
		this.indexToPath = indexToPath;
		this.nextFileExists = new ReadOnlyBooleanPropertyBase() {
			{
				currentIndex.addListener(new InvalidationListener() {
					@Override
					public void invalidated(Observable o) {
						fireValueChangedEvent();
					}
				});
			}

			@Override
			public boolean get() {
				return getNextPath() != null;
			}

			@Override
			public Object getBean() {
				return ImageWindowArgs.this;
			}

			@Override
			public String getName() {
				return "nextFileExists";
			}
		};
		this.previousFileExists = new ReadOnlyBooleanPropertyBase() {
			{
				currentIndex.addListener(new InvalidationListener() {
					@Override
					public void invalidated(Observable o) {
						fireValueChangedEvent();
					}
				});
			}

			@Override
			public boolean get() {
				return getPreviousPath() != null;
			}

			@Override
			public Object getBean() {
				return ImageWindowArgs.this;
			}

			@Override
			public String getName() {
				return "previousFileExists";
			}
		};
	}

	public Path currentPath() {
		return path;
	}

	public boolean isCurrentPathExists() {
		return path != null;
	}

	public void next() {
		Path newPath = getNextPath();
//		if (newPath == null) {
//			return;
//		}
		addIndex(1);
		path = newPath;
	}

	public void previous() {
		Path newPath = getPreviousPath();
//		if (newPath == null) {
//			return;
//		}
		addIndex(-1);
		path = newPath;
	}

	private Path getNextPath() {
		int current = currentIndex.get();
		return indexToPath.pathOfIndex(current + 1);
	}

	private Path getPreviousPath() {
		int current = currentIndex.get();
		return indexToPath.pathOfIndex(current - 1);
	}

	private void addIndex(int value) {
		int current = currentIndex.get();
		currentIndex.set(current + value);
	}

	public ReadOnlyBooleanProperty nextFileExistsProperty() {
		return nextFileExists;
	}

	public ReadOnlyBooleanProperty previousFileExistsProperty() {
		return previousFileExists;
	}
}
