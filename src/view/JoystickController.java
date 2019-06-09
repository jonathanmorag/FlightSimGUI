package view;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class JoystickController {

	Circle innerCircle, outerCircle;
	Slider rudder, throttle;
	DoubleProperty aileron, elevator;
	double orgSceneX, orgSceneY;

	public JoystickController(Circle innerCircle, Circle outerCircle, Slider rudder, Slider throttle,
			DoubleProperty aileron, DoubleProperty elevator) {
		this.innerCircle = innerCircle;
		this.outerCircle = outerCircle;
		this.rudder = rudder;
		this.throttle = throttle;
		this.aileron = aileron;
		this.elevator = elevator;
		orgSceneX = orgSceneY = 0;
	}

	public void innerReleased(MouseEvent e) {
		innerCircle.setCenterX(0);
		innerCircle.setCenterY(0);
		elevator.set(0);
		aileron.set(0);
	}

	public void innerPressed(MouseEvent e) {
//			innerCircle.setCenterX(0);
//			innerCircle.setCenterY(0);
		orgSceneX = e.getSceneX();
		orgSceneY = e.getSceneY();
		innerCircle.toFront();
	}

	public void innerDragged(MouseEvent e) {
		double offsetX = e.getSceneX() - orgSceneX;
		double offsetY = e.getSceneY() - orgSceneY;
		Circle c1 = (Circle) (e.getSource());
		// boudaries check
		if (outerCircle.contains(innerCircle.getCenterX(), innerCircle.getCenterY())) {
			// System.out.println(innerCircle.getCenterX() +" " + innerCircle.getCenterY());
			// sending orders to sim
			elevator.set(innerCircle.getCenterY() / (-100));
			aileron.set(innerCircle.getCenterX() / 100);

			innerCircle.setCenterX(c1.getCenterX() + offsetX);
			innerCircle.setCenterY(c1.getCenterY() + offsetY);
			orgSceneX = e.getSceneX();
			orgSceneY = e.getSceneY();
		} else {
//			System.out.println("offsetX,Y out of bound: " + offsetX + "," + offsetY);
			System.out.println("innerX,Y: " + innerCircle.getCenterX() + "," + innerCircle.getCenterY());
//			offsetX = 0;
//			offsetY = 0;
			System.out.println(orgSceneX);
			System.out.println(orgSceneY);
			Circle c = (Circle) (e.getSource());
			switch (getQuadrant(innerCircle.getCenterX(), innerCircle.getCenterY())) {
			case 1: {
				innerCircle.setCenterX(c.getCenterX() - 1);
				innerCircle.setCenterY(c.getCenterY() + 1);
				break;
			}
			case 2: {
				innerCircle.setCenterX(c.getCenterX() + 1);
				innerCircle.setCenterY(c.getCenterY() + 1);
				break;
			}
			case 3: {
				innerCircle.setCenterX(c.getCenterX() + 1);
				innerCircle.setCenterY(c.getCenterY() - 1);
				break;
			}
			case 4: {
				innerCircle.setCenterX(c.getCenterX() - 1);
				innerCircle.setCenterY(c.getCenterY() - 1);
				break;
			}
			}
			orgSceneX = e.getSceneX() + offsetX;
			orgSceneY = e.getSceneY() + offsetY;
		}
	}

	public int getQuadrant(double x, double y) {
		if ((x <= 100 && y <= 0) && (x >= 0 && y >= -100)) {
			return 1;
		}
		if ((x < 100 && y > 0) && (x > 0 && y < 100)) {
			return 4; 
		}
		if ((x >= -100 && y <= 0) && (x <= 0 && y >= -100)) {
			return 2;
		}
		if ((x >= -100 && y > 0) && (x <= 0 && y <= 100)) {
			return 3;
		}
		return 4;
	}
}
