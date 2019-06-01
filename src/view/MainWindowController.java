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
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;


import Models.Property;
import interpreter.Interpreter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class MainWindowController extends Window implements Initializable, Observer {
	
	ViewModel vm;
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
	public Property<String> ipInput;
	public Property<String> portInput;
	
	//properties
	public Property<Matrix> propertyMat;
	public Property<Position> startPos;
	public Property<String[]> csv;
	
	public static double startX;
	public static double startY;
	double orgSceneX;
	double orgSceneY;
	boolean manualFlag;
	boolean autoFlag;
	Matrix matrix;
	static String solverIP;
	static int solverPort;
	
	int airplanePosX;
	int airplanePosY;
	
	public void setViewModel(ViewModel vm) {
		this.vm=vm;
		propertyMat.bindTo(vm.propertyMat);
		vm.csv.bindTo(this.csv);
		vm.ipSim.bindTo(this.ipInput);
		vm.portSim.bindTo(this.portInput);
	}
	
	
	public void connectClicked() {
		TextField ipInput;
		TextField portInput;
		Stage commentWindow = new Stage();
		VBox box = new VBox(20);
		Label ipCommentlabel = new Label("Enter the ip of the simulator");
		ipInput = new TextField();
		Label portCommentlabel = new Label("Enter the port of the simulator");
		portInput = new TextField();
		Button b = new Button("Submit");
		box.getChildren().addAll(ipCommentlabel, ipInput, portCommentlabel, portInput, b);
		commentWindow.setScene(new Scene(box, 350, 250));
		commentWindow.show();
		b.setOnAction(e -> {
			this.ipInput.set(ipInput.getText());
			this.portInput.set(portInput.getText());
			System.out.println("mwc" + this.ipInput.get() + " " + this.portInput.get());
			commentWindow.close();vm.connect();
		});

	}
	
	public void onAirplanePositionChange() {
		mapDrawer.setAirplanePosition(new Position(airplanePosX,airplanePosY));
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
				csv.set(reader.readLine().split(","));
				vm.buildMatrix();
			} catch (IOException e) {}
		}
	}

	
	public void closeFromMenuBarClicked() {
		javafx.application.Platform.exit();
		System.exit(0);
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
		propertyMat = new Property<>();
		csv = new Property<>();
		ipInput = new Property<>();
		portInput = new Property<>();
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
		String data = (String)arg;
		if(data.equals("airplane")) {
			airplanePosX = vm.airplanePosX.get();
			airplanePosY = vm.airplanePosY.get();
			onAirplanePositionChange();
		}
		if(data.equals("matrix")) {
			mapDrawer.setHeightData(propertyMat.get()); //painting
		}
		
	}

}
