package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class JoystickDrawer extends Canvas{
	
	GraphicsContext gc = getGraphicsContext2D();
//	@FXML myCircle outerCircle;
//	@FXML myCircle innerCircle;
	Boolean innerPressed = false;
	
	
	
	
	public void fill() {
		gc.setFill(Color.LIGHTGRAY);
		gc.fillOval(100, 100, 100, 100);
		gc.setFill(Color.GRAY);
		gc.fillOval(120, 120, 60, 60);
	}
	
	// public void dragShape() {
	
//	double mx = ev.getX();
//	double my = ev.getY();
//	
//	x1 = mx - offsetX;
//	y1 = my - offsetY;
//	x2 = x1 + size;
//	y2 = y1 + size;
//	square = new Rectangle2D.Double(x1, y1, size, size);
//	repaint();  
//}


}