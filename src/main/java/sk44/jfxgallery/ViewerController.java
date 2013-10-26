/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery;

import javafx.scene.layout.Pane;
import sk44.jfxgallery.models.ImageWindowArgs;

/**
 *
 * @author sk
 */
public interface ViewerController {

	void showOn(Pane parent, ImageWindowArgs images);

}
