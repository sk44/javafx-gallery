/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk44.jfxgallery.views;

import javafx.scene.image.ImageView;

/**
 *
 * @author sk
 */
public enum TurnPageAnimation {

	LEFT {

			@Override
			double getPivotX(ImageView imageView) {
				return imageView == null ? 0.0 : imageView.getFitWidth();
			}

			@Override
			double getStartAngle() {
				return 0.0;
			}

			@Override
			double getEndAngle() {
				return 90.0;
			}

			@Override
			boolean isRightToLeft() {
				return false;
			}

		},
	RIGHT {

			@Override
			double getPivotX(ImageView imageView) {
				return 0.0;
			}

			@Override
			double getStartAngle() {
				return 90;
			}

			@Override
			double getEndAngle() {
				return 0;
			}

			@Override
			boolean isRightToLeft() {
				return true;
			}

		};

	abstract double getPivotX(ImageView imageView);

	abstract double getStartAngle();

	abstract double getEndAngle();

	abstract boolean isRightToLeft();

}
