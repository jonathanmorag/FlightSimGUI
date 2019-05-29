package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;

import interpreter.Interpreter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import matrix.Matrix;
import matrix.Position;
import server_side.AirplaneListener;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class MainWindowController extends Window implements Initializable, Observer {
	
	PrintWriter outToSolver;
	PrintWriter outToSim;
	@FXML JoystickController joystick;
	@FXML MapDrawer mapDrawer;
	@FXML RadioButton manual;
	@FXML RadioButton auto;
	@FXML JoystickController joystickDrawer;
	@FXML TextArea textArea;
	@FXML Circle outerCircle;
	@FXML Circle innerCircle;
	@FXML Slider rudderSlider;
	@FXML Slider throttleSlider;
	@FXML ToggleGroup tg;
	Socket server;
	public static double startX;
	public static double startY;
	double orgSceneX;
	double orgSceneY;
	boolean manualFlag;
	boolean autoFlag;
	Matrix matrix;
	static String solverIP;
	static int solverPort;
	
	public void connectClicked() {
		String[] ip = new String[1];
		int[] port = new int[1];
		Stage commentWindow = new Stage();
		VBox box = new VBox(20);
		Label ipCommentlabel = new Label("Enter the ip of the simulator");
		TextField ipInput = new TextField();
		Label portCommentlabel = new Label("Enter the port of the simulator");
		TextField portInput = new TextField();
		Button b = new Button("Submit");
		box.getChildren().addAll(ipCommentlabel, ipInput, portCommentlabel, portInput, b);
		commentWindow.setScene(new Scene(box, 350, 250));
		commentWindow.show();
		b.setOnAction(e -> {
			ip[0] = ipInput.getText();
			port[0] = Integer.parseInt(portInput.getText());
			try {
				server = new Socket(ip[0], port[0]);
				outToSim = new PrintWriter(server.getOutputStream());
				System.out.println("Client is connected to a remote Server.");
			} catch (IOException exe) {}
			finally {
				commentWindow.close();
			}
		});

	}
	
	public void onAirplanePositionChange() {
		mapDrawer.setAirplanePosition(AirplaneListener.airplanePosition);
	}

	public void loadDataClicked() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Load Data");
		fc.setInitialDirectory(new File("./resources"));
		fc.setSelectedExtensionFilter(new ExtensionFilter("Text Files", "*.txt"));
		File selectedFile = fc.showOpenDialog(this);
		if (selectedFile != null) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader(selectedFile));
				String[] result = reader.readLine().split(",");
				startX = Double.parseDouble(result[0]);
				startY = Double.parseDouble(result[1]);
				Position start = new Position((int)startX,(int)startY);
				 int cellSize = Integer.parseInt(result[2]);
				String[] heights = Arrays.copyOfRange(result, 4, result.length);
				matrix = buildMatrix(heights, start,cellSize);
				mapDrawer.setHeightData(matrix); // painting
				
				
				
				reader.close();
			} catch (IOException e) {}
		}
	}
	

	public Matrix buildMatrix(String[] dataFromCsv, Position start,int cellsize) {
		int size = (int) Math.sqrt(dataFromCsv.length);
		int[][] mat = new int[size][size];
		int c = 0;
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				mat[i][j] = Integer.parseInt(dataFromCsv[c++]);
			}
		}
		return new Matrix(mat, start, null);

	}
	
	public void closeFromMenuBarClicked(ActionEvent e) {
		// to implement
		//Stage window = (Stage)this.getScene().getWindow();
		//window.close();
	}

	public void calculatePathClicked() {
		Stage commentWindow = new Stage();
		VBox box = new VBox(20);
		Label ipCommentlabel = new Label("Enter ip of a solver server");
		TextField ipInput = new TextField();
		Label portCommentlabel = new Label("Enter port of a solver server");
		TextField portInput = new TextField();
		Button b = new Button("Submit");
		box.getChildren().addAll(ipCommentlabel, ipInput, portCommentlabel, portInput, b);
		
		b.setOnAction(e -> {
			solverIP = ipInput.getText();
			solverPort = Integer.parseInt(portInput.getText());
			sendDataToSolver();
			commentWindow.close();
		});
		
		if(server==null) {
			commentWindow.setScene(new Scene(box, 350, 250));
			commentWindow.show();
		}
		else
			sendDataToSolver();
		
		
	}
	private void sendDataToSolver() {
		try {
			server = new Socket(solverIP, solverPort);
			System.out.println("Client is connected to a remote Server.");
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			outToSolver = new PrintWriter(server.getOutputStream());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < matrix.getData().length; i++) {
				for (int j = 0; j < matrix.getData()[0].length; j++) {
					sb.append(matrix.getData()[i][j] + ",");
				}

				outToSolver.println(sb.substring(0, sb.length() - 1).toString());
				outToSolver.flush();
			}
			outToSolver.println("end");
			outToSolver.flush();
			outToSolver.println("0,0");
			outToSolver.flush();
//			out.println("8,8");
			// out.println(myData.getEntrance().row+","+myData.getEntrance().col);
			outToSolver.println(matrix.getExit().row+","+matrix.getExit().col);
			outToSolver.flush();

			System.out.println(in.readLine());
		} catch (IOException e) {} 
	}

	
	public void aboutClicked() {
		Stage window = new Stage();
		window.setHeight(180);
		window.setWidth(400);
		window.setTitle("About");
		StackPane root = new StackPane();
		Text t = new Text("© 2019 All Rights Reserved to Royi Hamo & Jonathan Morag");
		Button b = new Button("OK");
		b.setOnMouseClicked(e-> {
			Stage s = (Stage)b.getScene().getWindow();
			s.close();
		});
		b.setTranslateY(40);
		root.getChildren().addAll(t, b);
		b.setPadding(new Insets(12));
		t.setTranslateY(-15);
		window.setScene(new Scene(root, 200, 200));
		window.show();
	}
	
	
	public void radioButtonClicked() {
		tg = new ToggleGroup();
		manual.setToggleGroup(tg);
		auto.setToggleGroup(tg);
		if (tg.getSelectedToggle().equals(manual)) {
			System.out.println("Manual Pilot mode is now Activated");
			manualFlag=true;
			autoFlag=false;
		}
		if (tg.getSelectedToggle().equals(auto)) // Autopilot
		{
			manualFlag=false;
			autoFlag=true;
			FileChooser fc = new FileChooser();
			fc.setTitle("Load File to interpret automatically");
			fc.setInitialDirectory(new File("./resources"));
			fc.setSelectedExtensionFilter(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fc.showOpenDialog(this);
			try {
				Scanner sc = new Scanner(selectedFile);
				while (sc.hasNextLine()) {
					textArea.appendText(sc.nextLine());
					textArea.appendText("\n");
				}
				sc.close();
				Interpreter i = new Interpreter();
				System.out.println(i.interpret(i.lexer(selectedFile.getName())));
			} catch (FileNotFoundException e) {
			}

			System.out.println("Autopilot mode is now Activated");
		}
	}

	public void innerPressed(MouseEvent e) {
		if(manualFlag) {
				innerCircle.setCenterX(0);
				innerCircle.setCenterY(0);
				orgSceneX = e.getSceneX();
				orgSceneY = e.getSceneY();
				innerCircle.toFront();
		}
	}

	public void innerDragged(MouseEvent e) {
		if(manualFlag) {
//			System.out.println("(" + orgSceneX + "," + orgSceneY + ")"); //printing x and y 
			double offsetX = e.getSceneX() - orgSceneX;
			double offsetY = e.getSceneY() - orgSceneY;

			//boudaries check
			if(!outerCircle.contains(innerCircle.getCenterX(), innerCircle.getCenterY())) {return;}
			System.out.println(innerCircle.getCenterX());
				
			//sending orders to sim
			if(outToSim!=null) {
				outToSim.flush();
				outToSim.println("set /controls/flight/elevator " + (innerCircle.getCenterY()/(-100))); //uncomment when connected
				outToSim.flush();
				outToSim.println("set /controls/flight/aileron " + innerCircle.getCenterX()/100); //uncomment when connected
				outToSim.flush();
			}
				

			Circle c = (Circle) (e.getSource());
			innerCircle.setCenterX(c.getCenterX() + offsetX);
			innerCircle.setCenterY(c.getCenterY() + offsetY);
			orgSceneX = e.getSceneX();
			orgSceneY = e.getSceneY();
		}
	}
	
	public void innerReleased(MouseEvent e) {
			innerCircle.setCenterX(0);
			innerCircle.setCenterY(0);
	}
	
	public void mapClicked(MouseEvent e) {
		
//			System.out.println("X: " + (e.getSceneX()-5) + "Y: " + (e.getSceneY()-60));
			matrix.setExit(mapDrawer.setRoute((e.getSceneX() -5),(e.getSceneY()-60)));
			if(server!=null) {
				calculatePathClicked();
			}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		manual.setSelected(true);
		manualFlag = true;
		throttleSlider.setMin(0);
		throttleSlider.setMax(1);
		rudderSlider.setMin(-1);
		rudderSlider.setMax(1);
		
		rudderSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	if(manualFlag) {
                    System.out.println("rudder: "+ new_val);
                    outToSim.flush();
                    outToSim.println("set /controls/flight/rudder "+ new_val); //uncomment when connected
                    outToSim.flush();
            	}
            }
        });
		throttleSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		if(manualFlag) {
            			System.out.println("throttle: "+ new_val);
            			outToSim.flush();
            			outToSim.println("set /controls/engines/current-engine/throttle "+ new_val); //uncomment when connected
            			outToSim.flush();
            		}
            }
        });
		
	}

	@Override
	public void update(Observable o, Object arg) {
		onAirplanePositionChange();
	}

}
