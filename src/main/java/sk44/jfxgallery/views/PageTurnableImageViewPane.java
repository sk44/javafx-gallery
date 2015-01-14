/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.views;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Point3D;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 *
 * @author sk
 */
public class PageTurnableImageViewPane extends ImageViewPane {

    public interface TurnPageCallback {

        void execute();
    }

    private final TurnPageAnimation animation;

    public PageTurnableImageViewPane(HPos hPos, TurnPageAnimation animation) {
        super(new ImageView(), hPos);
        this.animation = animation;
    }

    public void openPage(final ImageView next, final TurnPageCallback callback) {

        ImageView imageView = getImageView();
        if (imageView == null) {
            imageView = next;
        }
        Timeline timeline = createTimeline(imageView, animation.isRightToLeft() == false);
        timeline.setOnFinished((ActionEvent t) -> {
            setImageView(next);
            if (callback != null) {
                callback.execute();
            }
        });
        timeline.play();
    }

    public void closePage(final ImageView next, final TurnPageCallback callback) {

        Timeline timeline = createTimeline(next, animation.isRightToLeft());
        setImageView(next);
        timeline.setOnFinished((ActionEvent t) -> {
            if (callback != null) {
                callback.execute();
            }
        });
        timeline.play();
    }

    protected Timeline createTimeline(ImageView imageView, boolean turned) {
        // 第二引数が回転の基軸
        Rotate rotate = new Rotate(0, animation.getPivotX(getImageView()), 0, 0, new Point3D(0.0, 1.0, 0.0));
        imageView.getTransforms().add(rotate);
        return new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(rotate.angleProperty(), turned ? animation.getStartAngle()
                    : animation.getEndAngle())),
            new KeyFrame(Duration.millis(200),
                new KeyValue(rotate.angleProperty(), turned ? animation.getEndAngle()
                    : animation.getStartAngle()))
        );

    }
}
