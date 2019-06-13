package view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class JoystickController {

	Circle innerCircle, outerCircle;
	Slider rudder, throttle;
	DoubleProperty aileron, elevator;
	double orgSceneX, orgSceneY;

	public JoystickController(Circle innerCircle, Circle outerCircle, Slider rudder, Slider throttle) {
		this.innerCircle = innerCircle;
		this.outerCircle = outerCircle;
		this.rudder = rudder;
		this.throttle = throttle;
		this.aileron = new SimpleDoubleProperty();
		this.elevator = new SimpleDoubleProperty();
		orgSceneX = orgSceneY = 0;
	}

	public void innerReleased(MouseEvent e) {              	// When inner circle is released event handler 
		innerCircle.setCenterX(0);
		innerCircle.setCenterY(0);
		elevator.set(0);
		aileron.set(0);
	}

	public void innerPressed(MouseEvent e) {				// When inner circle is pressed event handler 
		orgSceneX = e.getSceneX();
		orgSceneY = e.getSceneY();
		innerCircle.toFront();
	}

	public void innerDragged(MouseEvent e) {				// When inner circle is dragged event handler 

		double maxRange = outerCircle.getRadius() - innerCircle.getRadius() + 15;
		innerCircle.setCenterX(0);
		innerCircle.setCenterY(0);
		if (e.getX() >= 0)
			innerCircle.setCenterX(Double.min(innerCircle.getCenterX() + maxRange, e.getX()));
		else
			innerCircle.setCenterX(Double.max(innerCircle.getCenterX() - maxRange, e.getX()));
		if (e.getY() >= 0)
			innerCircle.setCenterY(Double.min(innerCircle.getCenterY() + maxRange, e.getY()));
		else
			innerCircle.setCenterY(Double.max(innerCircle.getCenterY() - maxRange, e.getY()));
		elevator.set(innerCircle.getCenterY() / (-100));
		aileron.set(innerCircle.getCenterX() / 100);

	}
}
