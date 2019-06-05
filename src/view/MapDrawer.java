package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import matrix.Matrix;
import matrix.Position;

public class MapDrawer extends Canvas {

	int[][] heightData;
	int aCol;
	int aRow;
	int toDrawRow;
	int toDrawCol;
	List<Position> points = new ArrayList<>();
	boolean paintFlag;
	
	public void setHeightData(Matrix m) {
		heightData = m.getData();
		redraw();
	}

	public void setAirplanePosition(Position pos) {
		aCol = pos.col;
		aRow = pos.row;
		redraw();
	}

	public Position setRoute(double clickedX, double clickedY) {
		double cellSize = (double) ((400.0) / (double) (heightData.length));
		toDrawCol = (int) ((clickedX) / cellSize);
		toDrawRow = (int) ((clickedY) / cellSize);
		redraw(); // Drawing X
		return new Position(toDrawRow, toDrawCol);
//			System.out.println(dest);
	}

	public void redraw() {
		if (heightData != null) {
			double W = getWidth();
			double H = getHeight();
			double w = W / heightData[0].length;
			double h = H / heightData.length;
			
			GraphicsContext gc = getGraphicsContext2D();
			Image airplane = null;
			Image destination = null;
			Image path = null;
			try {
				airplane = new Image(new FileInputStream("./resources/images/Airplane.png"));
				destination = new Image(new FileInputStream("./resources/images/Destination2.png"));
				path = new Image(new FileInputStream("./resources/images/Path.png"));
			} catch (FileNotFoundException e) {
				System.out.println("file not found");
			}
			gc.clearRect(0, 0, W, H);

			for (int i = 0; i < heightData.length; i++) {
				for (int j = 0; j < heightData[0].length; j++) {
					gc.setFill(setColor(heightData[i][j]));
					gc.fillRect(j * w, i * h, w, h);
//					gc.setFill(Color.GRAY);							//show values on map
//					gc.fillText(String.valueOf(heightData[i][j]),j*w, i*h);
					if (i == toDrawRow && j == toDrawCol) {
						gc.drawImage(destination, j * w, i * h, w, h); // draw destination X
					}
//					if(paintFlag) {
						for(Position p : points) {
							if(p.row == j && p.col == i) {
//								gc.setFill(Color.BLACK);
//								gc.fillRect(j * w, i * h, w, h);
								gc.drawImage(path, j * w, i * h, w, h);
							}
						}System.out.println("after path loop");
//					}
				}
			}
			gc.drawImage(airplane, aCol * w, aRow * h, 2 * w, 2 * h); // draw Airplane
		}
//		paintFlag=false;
		
	}

	private Color setColor(int value) {
		switch (value) {
		case 0:
			return Color.rgb(204, 0, 0); // red
		case 1:
			return Color.rgb(255, 0, 0);
		case 2:
			return Color.rgb(255, 51, 51);
		case 3:
			return Color.rgb(255, 102, 102);
		case 4:
			return Color.rgb(255, 204, 153);
		case 5:
			return Color.rgb(255, 255, 153);
		case 6:
			return Color.rgb(255, 255, 102);
		case 7:
			return Color.rgb(255, 255, 0); // yellow
		case 8:
			return Color.rgb(255, 255, 102);
		case 9:
			return Color.rgb(255, 255, 153);
		case 10:
			return Color.rgb(204, 255, 153);
		case 11:
			return Color.rgb(153, 255, 153);
		case 12:
			return Color.rgb(102, 255, 102);
		case 13:
			return Color.rgb(51, 255, 51);
		case 14:
			return Color.rgb(0, 255, 0);
		case 15:
			return Color.rgb(0, 204, 0); // green
		}

		return null;
	}

	public void paintPath(String shortestPath, Position current) {
		// System.out.println(shortestPath);
		points.clear();
		String[] steps = shortestPath.split(",");
		Position prev = current;
		// points = new ArrayList<>();
		for(String s: steps) {
			if(s.equals("Right")) 
			{
				prev = new Position(prev.row + 1, prev.col);
				points.add(prev);
			}
			if (s.equals("Down")) {
				prev = new Position(prev.row, prev.col + 1);
				points.add(prev);
			}
			if (s.equals("Left")) {
				prev = new Position(prev.row - 1, prev.col);
				points.add(prev);
			}
				
			if (s.equals("Up")) {
				prev = new Position(prev.row, prev.col - 1);
				points.add(prev);
			}
			// (0,1) -> (0,2) -> (1,2) -> (2,2)
		}
		points.remove(points.size()-1);
//		paintFlag = true;
		redraw();
		
	}

}
