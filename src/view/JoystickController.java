package view;

import javafx.beans.property.DoubleProperty;
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
			innerCircle.setCenterX(0);
			innerCircle.setCenterY(0);
			orgSceneX = e.getSceneX();
			orgSceneY = e.getSceneY();
			innerCircle.toFront();
	}
	
	public void innerDragged(MouseEvent e) {
			double offsetX = e.getSceneX() - orgSceneX;
			double offsetY = e.getSceneY() - orgSceneY;

			// boudaries check
			if (outerCircle.contains(innerCircle.getCenterX(), innerCircle.getCenterY())) {
				//System.out.println(innerCircle.getCenterX() +" " + innerCircle.getCenterY());
				
				// sending orders to sim
				elevator.set(innerCircle.getCenterY() / (-100));
				aileron.set(innerCircle.getCenterX() / 100);
				
				Circle c = (Circle) (e.getSource());
				innerCircle.setCenterX(c.getCenterX() + offsetX);
				innerCircle.setCenterY(c.getCenterY() + offsetY);
				orgSceneX = e.getSceneX();
				orgSceneY = e.getSceneY();
			}

	}
	
	
	
}
