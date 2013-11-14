/*
 * Copyright (c) 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */
package sk44.jfxgallery.views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * Resizable image view container.
 *
 * @author sakouznetk
 * @see https://javafx-jira.kenai.com/browse/RT-10610
 * @see https://javafx-jira.kenai.com/browse/RT-21337
 * @see http://stackoverflow.com/questions/15951284/javafx-image-resizing
 */
public class ImageViewPane extends Region {

	public static ImageView createImageView(Path imagePath) {

		Image image;
		try {
			image = new Image(Files.newInputStream(imagePath));
		} catch (IOException ex) {
			Logger.getLogger(ImageViewPane.class.getName()).log(Level.SEVERE, null, ex);
			return null;
		}
		ImageView imageView = new ImageView();
		imageView.setImage(image);
		imageView.setSmooth(true);
		imageView.setCache(true);
		imageView.setPreserveRatio(true);

		return imageView;
	}

	private final ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();
	protected final HPos hPos;

	public ObjectProperty<ImageView> imageViewProperty() {
		return imageViewProperty;
	}

	public ImageView getImageView() {
		return imageViewProperty.get();
	}

	public void setImageView(ImageView imageView) {
		this.imageViewProperty.set(imageView);
	}

	public ImageViewPane(HPos hPos) {
		this(new ImageView(), hPos);
	}

	@Override
	protected void layoutChildren() {
		ImageView imageView = imageViewProperty.get();
		if (imageView != null) {
			imageView.setFitWidth(getWidth());
			imageView.setFitHeight(getHeight());
			layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, hPos, VPos.CENTER);
		}
		super.layoutChildren();
	}

	public ImageViewPane(ImageView imageView, HPos hPos) {
		this.hPos = hPos;
		imageViewProperty.addListener(new ChangeListener<ImageView>() {
			@Override
			public void changed(ObservableValue<? extends ImageView> arg0, ImageView oldIV, ImageView newIV) {
				if (oldIV != null) {
					getChildren().remove(oldIV);
				}
				if (newIV != null) {
					getChildren().add(newIV);
				}
			}
		});
		this.imageViewProperty.set(imageView);
	}

	public void replaceImage(Path imagePath) {

		ImageView imageView = createImageView(imagePath);
		if (imageView == null) {
			return;
		}
		setImageView(imageView);
	}

}
