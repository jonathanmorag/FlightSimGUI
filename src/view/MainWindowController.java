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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
	
	@FXML MapDrawer mapDrawer;
	@FXML RadioButton manual;
	@FXML RadioButton auto;
	@FXML TextArea textArea;
	@FXML Circle outerCircle;
	@FXML Circle innerCircle;
	@FXML ToggleGroup tg;
	

	//--------------simulator-----------------
	//controls
	@FXML Slider rudderSlider;
	@FXML Slider throttleSlider;
	public DoubleProperty elevator;
	public DoubleProperty aileron;
	public Property<String> ipSim;
	public Property<String> portSim;
//	public Property<Position> startPos;
	public Property<Position> exitPos;
	//----------------------------------------
	
	//----------------solver------------------
	public Property<String> ipSolver;
	public Property<String> portSolver;
	//----------------------------------------
	
	
	
	//properties
	public Property<Matrix> propertyMat;
	public Property<String[]> csv;
	
	double orgSceneX;
	double orgSceneY;
	boolean manualFlag;
	boolean autoFlag;
	Matrix matrix;
	
	int airplanePosX;
	int airplanePosY;
	
	//define bindings
	public void setViewModel(ViewModel vm) {
		this.vm=vm;
		propertyMat.bindTo(vm.propertyMat);
		vm.csv.bindTo(this.csv);
		vm.ipSim.bindTo(this.ipSim);
		vm.portSim.bindTo(this.portSim);
		vm.ipSolver.bindTo(this.ipSolver);
		vm.portSolver.bindTo(this.portSolver);
		vm.exitPos.bindTo(this.exitPos);
		//controls
		vm.rudder.bind(this.rudderSlider.valueProperty());
		vm.throttle.bind(this.throttleSlider.valueProperty());
		vm.aileron.bind(this.aileron);
		vm.elevator.bind(this.elevator);
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
			this.ipSim.set(ipInput.getText());
			this.portSim.set(portInput.getText());
			commentWindow.close();
			vm.connectToSimulator();
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
		commentWindow.setScene(new Scene(box, 350, 250));
		commentWindow.show();
		b.setOnAction(e -> {
			ipSolver.set(ipInput.getText());
			portSolver.set(portInput.getText());
			commentWindow.close();
			vm.connectToSolver();
		});
		
//		if(server==null) {
//			commentWindow.setScene(new Scene(box, 350, 250));
//			commentWindow.show();
//		}
//		else
//			sendDataToSolver();
	}
	
//	private void sendDataToSolver() {
//		try {
//			server = new Socket(solverIP, solverPort);
//			System.out.println("Connected to a solver server.");
//			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
//			outToSolver = new PrintWriter(server.getOutputStream());
			
			//convert matrix to a problem
//			for (int i = 0; i < matrix.getData().length; i++) {
//				StringBuilder sb = new StringBuilder();
//				for (int j = 0; j < matrix.getData()[0].length; j++) {
//					sb.append(matrix.getData()[i][j] + ",");
//				}
//				outToSolver.println(sb.substring(0, sb.length() - 1).toString());
//				outToSolver.flush();
//			}
//			ConvertToProblem(matrix);
//			outToSolver.println("end");
//			outToSolver.flush();
//			outToSolver.println("0,0");
//			outToSolver.flush();
//			out.println("8,8");
			// out.println(myData.getEntrance().row+","+myData.getEntrance().col);
//			outToSolver.println(matrix.getExit().row+","+matrix.getExit().col);
//			outToSolver.flush();
//
//			System.out.println(in.readLine());
//		} catch (IOException e) {} 
//	}

	
	public void aboutClicked() {
		Stage window = new Stage();
		window.setHeight(180);
		window.setWidth(400);
		window.setTitle("About");
		StackPane root = new StackPane();
		Text t = new Text("� 2019 All Rights Reserved to Royi Hamo & Jonathan Morag");
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
				Scanner sc = new Scanner(selectedFile); //display chosen file in text area
				while (sc.hasNextLine()) {
					textArea.appendText(sc.nextLine());
					textArea.appendText("\n");
				}
				sc.close();
				Interpreter i = new Interpreter();
				System.out.println(i.interpret
						(i.lexer(selectedFile.getName()))); //need to move logic to model .. :(
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
//			System.out.println(innerCircle.getCenterX());
				
			//sending orders to sim
				elevator.set(innerCircle.getCenterY()/(-100));
				vm.sendElevatorValues();
				aileron.set(innerCircle.getCenterX()/100);
				vm.sendAileronValues();
				

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
			exitPos.set(mapDrawer.setRoute((e.getSceneX() -5),(e.getSceneY()-60)));
			vm.setExitPosition();
			vm.requestSolution();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		propertyMat = new Property<>();
		csv = new Property<>();
		ipSim = new Property<>();
		portSim = new Property<>();
		ipSolver = new Property<>();
		portSolver = new Property<>();
		elevator = new SimpleDoubleProperty();
		aileron = new SimpleDoubleProperty();
//		startPos = new Property<>();
		exitPos = new Property<>();
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
            		vm.sendRudderValues();
            	}
            }
        });
		throttleSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            		if(manualFlag) {
            			vm.sendThrottleValues();
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
			onAirplanePositionChange();					//painting airplane
		}
		if(data.equals("matrix")) {
			mapDrawer.setHeightData(propertyMat.get()); //painting map
		}
		
	}

}
