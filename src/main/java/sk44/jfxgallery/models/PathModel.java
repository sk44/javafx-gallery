/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.models;

import java.nio.file.Path;

/**
 *
 * @author sk
 */
public class PathModel {

	private final Path path;
	private final boolean parent;

	public static PathModel modelForPath(Path path) {
		return new PathModel(path, false);
	}

	public static PathModel parentModelForPath(Path path) {
		return new PathModel(path, true);
	}

	private PathModel(Path path, boolean parent) {
		this.path = path.toAbsolutePath().normalize();
		this.parent = parent;
	}

	public Path getPath() {
		return path;
	}

	public String getName() {
		if (parent) {
			return "..";
		}
		return path.getFileName().toString();
	}
}
