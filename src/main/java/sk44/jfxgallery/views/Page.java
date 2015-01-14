/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.views;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import sk44.jfxgallery.models.ImagePager;

/**
 *
 * @author sk
 */
public class Page {

    public static Page left() {
        return new Page(TurnPageAnimation.LEFT, HPos.RIGHT, true);
    }

    public static Page right() {
        return new Page(TurnPageAnimation.RIGHT, HPos.LEFT, false);
    }

    private static void fillInAnchorPane(ImageViewPane pane) {
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
    }

    private final PageTurnableImageViewPane imageViewPane;
    private final ImageViewPane backgroundImageViewPane;
    private final boolean forward;
    private ImagePager pager;

    private Page(TurnPageAnimation animation, HPos pos, boolean forward) {
        this.imageViewPane = new PageTurnableImageViewPane(pos, animation);
        this.backgroundImageViewPane = new ImageViewPane(pos);
        this.forward = forward;
    }

    public void fillIn(AnchorPane anchorPane) {
        fillInAnchorPane(backgroundImageViewPane);
        anchorPane.getChildren().add(backgroundImageViewPane);
        fillInAnchorPane(imageViewPane);
        anchorPane.getChildren().add(imageViewPane);
    }

    public void setPager(ImagePager pager) {
        this.pager = pager;
    }

    public final void setOnMouseClicked(EventHandler<? super MouseEvent> eh) {
        imageViewPane.setOnMouseClicked(eh);
    }

    public boolean isNextFileExists() {
        return pager.isNextFileExists();
    }

    public boolean isPreviousFileExists() {
        return pager.isPreviousFileExists();
    }

    public void loadImage() {
        imageViewPane.replaceImage(pager.currentPath());
    }

    public void loadNext() {
        pager.next();
        loadImage();
    }

    public void loadPrevious() {
        pager.previous();
        loadImage();
    }

    public void turnPage(final Page toPage) {

        if (forward) {
            pager.turnPage();
        } else {
            pager.turnBackPage();
        }
        ImageView nextImageView;
        if (pager.isCurrentPathExists()) {
            nextImageView = ImageViewPane.createImageView(pager.currentPath());
            backgroundImageViewPane.setImageView(nextImageView);
        } else {
            backgroundImageViewPane.setImageView(null);
            nextImageView = null;
        }
        toPage.backgroundImageViewPane.setImageView(toPage.imageViewPane.getImageView());
        imageViewPane.openPage(nextImageView, () -> {
            if (forward) {
                toPage.pager.turnPage();
            } else {
                toPage.pager.turnBackPage();
            }
            ImageView toImageView = ImageViewPane.createImageView(toPage.pager.currentPath());
            toPage.imageViewPane.closePage(toImageView, () -> {
                toPage.backgroundImageViewPane.setImageView(null);
            });
        });
    }
}
