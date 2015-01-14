/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.views;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import sk44.jfxgallery.models.PathModel;

/**
 *
 * @author sk
 */
public class DirectoryViewCellFactory implements Callback<ListView<PathModel>, ListCell<PathModel>> {

    @Override
    public ListCell<PathModel> call(ListView<PathModel> param) {
        return new ListCell<PathModel>() {
            @Override
            protected void updateItem(PathModel t, boolean empty) {
                super.updateItem(t, empty);
                // JavaFX8 では空の場合に null なりを再設定しないと表示がおかしなことに
                if (empty) {
                    setText(null);
                } else {
                    setText(t.getName());
                }
            }
        };
    }

}
