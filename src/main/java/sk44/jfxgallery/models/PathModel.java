/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public boolean isSamePath(Path other) {
        try {
            return Files.isSameFile(path, other);
        } catch (IOException ex) {
            Logger.getLogger(PathModel.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // selectionModel で同一性判定が必要っぽいので実装しておく
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.path);
        hash = 37 * hash + (this.parent ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PathModel other = (PathModel) obj;
        if (!Objects.equals(this.path, other.path)) {
            return false;
        }
        if (this.parent != other.parent) {
            return false;
        }
        return true;
    }

}
