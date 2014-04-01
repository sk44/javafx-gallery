/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.controllers;

import javafx.scene.layout.Pane;
import sk44.jfxgallery.models.ImagePager;

/**
 *
 * @author sk
 */
@FunctionalInterface
public interface ViewerController {

    void showOn(Pane parent, ImagePager images);

}
