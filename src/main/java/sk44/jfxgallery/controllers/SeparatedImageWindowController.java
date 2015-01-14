/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import sk44.jfxgallery.models.ImagePager;
import sk44.jfxgallery.views.Page;

/**
 *
 * @author sk
 */
public class SeparatedImageWindowController implements Initializable, ViewerController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private AnchorPane leftContainer;
    @FXML
    private AnchorPane rightContainer;
    private Pane parent;
    private final Page leftPage = Page.left();
    private final Page rightPage = Page.right();

    @FXML
    protected void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case J:
                if (event.isShiftDown()) {
                    showNextHalf();
                } else {
                    showNext();
                }
                break;
            case K:
                if (event.isShiftDown()) {
                    showPreviousHalf();
                } else {
                    showPrevious();
                }
                break;
            case ESCAPE:
                close();
                break;
            default:
                break;
        }
    }

    private void showNext() {
        if (leftPage.isNextFileExists() == false) {
            return;
        }
        leftPage.turnPage(rightPage);
    }

    private void showNextHalf() {
        if (leftPage.isNextFileExists() == false) {
            return;
        }
        rightPage.loadNext();
        leftPage.loadNext();
    }

    private void showPrevious() {
        if (rightPage.isPreviousFileExists() == false) {
            return;
        }
        rightPage.turnPage(leftPage);
    }

    private void showPreviousHalf() {
        if (rightPage.isPreviousFileExists() == false) {
            return;
        }
        leftPage.loadPrevious();
        rightPage.loadPrevious();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        leftContainer.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        leftPage.fillIn(leftContainer);
        rightContainer.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        rightPage.fillIn(rightContainer);
        rightPage.setOnMouseClicked((MouseEvent t) -> {
            showPrevious();
        });
        leftPage.setOnMouseClicked((MouseEvent t) -> {
            showNext();
        });
    }

    @Override
    public void showOn(Pane parent, ImagePager param) {

        this.parent = parent;
        rightPage.setPager(param);
        leftPage.setPager(ImagePager.Next(param));

        rootPane.prefHeightProperty().bind(parent.heightProperty());
        rootPane.prefWidthProperty().bind(parent.widthProperty());

        rightPage.loadImage();
        leftPage.loadImage();

        parent.getChildren().add(rootPane);
        rootPane.requestFocus();
    }

    void close() {
        parent.getChildren().remove(rootPane);
    }

}
