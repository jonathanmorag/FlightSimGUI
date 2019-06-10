package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.SepiaTone;
import javafx.scene.input.MouseEvent;
import matrix.Matrix;
import matrix.Position;
import models.Property;

public class MainWindowController extends Window implements Initializable, Observer {

	ViewModel vm;
	PrintWriter outToSolver;
	@FXML
	TextArea logScreen;
	@FXML
	MapDrawer mapDrawer;
	@FXML
	RadioButton manual;
	@FXML
	RadioButton auto;
	@FXML
	TextArea textArea;
	@FXML
	Circle outerCircle;
	@FXML
	Circle innerCircle;
	@FXML
	ToggleGroup tg;
	JoystickController joystick;

	// --------------simulator-----------------
	// controls
	@FXML
	Slider rudderSlider;
	@FXML
	Slider throttleSlider;

	public Property<String> ipSim;
	public Property<String> portSim;
	public Property<Position> exitPos;
	// ----------------------------------------

	// ----------------solver------------------
	public Property<String> ipSolver;
	public Property<String> portSolver;
	public StringProperty shortestPath;
	// ----------------------------------------

	// -------------properties-----------------
	public Property<Matrix> propertyMat;
	public Property<String[]> csv;
	public StringProperty fileName;
	public BooleanProperty isConnectedToSolver;

	double orgSceneX;
	double orgSceneY;
	boolean manualFlag;
	boolean autoFlag;
	Position curAirplaneLocation;
	Matrix matrix;

	int airplanePosX;
	int airplanePosY;

	// define bindings
	public void setViewModel(ViewModel vm) {
		this.vm = vm;
		this.propertyMat.bindTo(vm.propertyMat);
		this.shortestPath.bind(vm.shortestPath);

		this.isConnectedToSolver.bind(vm.isConnectedToSolver);

		// bind MWC controls to VM controls
		vm.rudder.bind(this.rudderSlider.valueProperty());
		vm.throttle.bind(this.throttleSlider.valueProperty());
		vm.aileron.bind(joystick.aileron);
		vm.elevator.bind(joystick.elevator);

		vm.csv.bindTo(this.csv);
		vm.ipSim.bindTo(this.ipSim);
		vm.portSim.bindTo(this.portSim);
		vm.ipSolver.bindTo(this.ipSolver);
		vm.portSolver.bindTo(this.portSolver);
		vm.exitPos.bindTo(this.exitPos);
		vm.fileName.bind(this.fileName);

	}

	public void connectClicked() {
		Stage window = new Stage();
		GridPane grid = new GridPane();
		TextField ipInput = new TextField();
		TextField portInput = new TextField();
		Label ipCommentlabel = new Label("FlightGear simulator's IP:");
		Label portCommentlabel = new Label("FlightGear simulator's Port:");
		Button b = new Button("Connect");
		b.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			b.setCursor(Cursor.HAND);
			b.setEffect(new DropShadow());
		});
		b.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
			b.setCursor(null);
			b.setEffect(null);
		});
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Text connect = new Text("Connect");
		connect.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(connect, 0, 0);
		grid.add(ipCommentlabel, 0, 1);
		grid.add(ipInput, 1, 1);
		grid.add(portCommentlabel, 0, 2);
		grid.add(portInput, 1, 2);
		HBox hbButton = new HBox(10);
		hbButton.setAlignment(Pos.BOTTOM_CENTER);
		hbButton.getChildren().add(b);
		grid.add(hbButton, 1, 4);
		window.setScene(new Scene(grid, 400, 250));
		window.show();
		b.setOnAction(e -> {
			if (!ipInput.getText().equals("") && !portInput.getText().equals("")) {
				this.ipSim.set(ipInput.getText());
				this.portSim.set(portInput.getText());
				vm.connectToSimulator();
				logScreen.appendText("Established connection to the simulator.\n");
				window.close();
			} else
				logScreen.appendText("Invalid parameters.\n");
		});
	}

	public void onAirplanePositionChange() {
		curAirplaneLocation = new Position(airplanePosX, airplanePosY);
		mapDrawer.setAirplanePosition(curAirplaneLocation);
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
				logScreen.appendText("Map loaded succesfully.\n");
			} catch (IOException e) {
			}
		}
	}

	public void closeFromMenuBarClicked() {
		javafx.application.Platform.exit();
		System.exit(0);
	}

	public void calculatePathClicked() {
		Stage window = new Stage();
		GridPane grid = new GridPane();
		TextField ipInput = new TextField();
		TextField portInput = new TextField();
		Label ipCommentlabel = new Label("Enter IP of a solver server:");
		Label portCommentlabel = new Label("Enter Port of a solver server:");
		Button b = new Button("Connect");
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));
		Text connect = new Text("Calculate Path");
		connect.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(connect, 0, 0);
		grid.add(ipCommentlabel, 0, 1);
		grid.add(ipInput, 1, 1);
		grid.add(portCommentlabel, 0, 2);
		grid.add(portInput, 1, 2);
		HBox hbButton = new HBox(10);
		hbButton.setAlignment(Pos.BOTTOM_CENTER);
		hbButton.getChildren().add(b);
		grid.add(hbButton, 1, 4);
		b.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			b.setCursor(Cursor.HAND);
			b.setEffect(new DropShadow());
		});
		b.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
			b.setCursor(null);
			b.setEffect(null);
		});
		window.setScene(new Scene(grid, 400, 250));
		window.show();
		b.setOnAction(e -> {
			if (!ipInput.getText().equals("") && !portInput.getText().equals("")) {
				ipSolver.set(ipInput.getText());
				portSolver.set(portInput.getText());
				vm.connectToSolver();
				logScreen.appendText("Established connection to a solver server.\n");
				window.close();
			} else {
				logScreen.appendText("Invalid parameters.\n");
			}
		});
	}

	public void aboutClicked() {
		Stage window = new Stage();
		window.setHeight(180);
		window.setWidth(400);
		window.setTitle("About");
		StackPane root = new StackPane();
		Text t = new Text("© 2019 All Rights Reserved to Royi Hamo & Jonathan Morag");
		Button b = new Button("OK");
		b.setOnMouseClicked(e -> {
			Stage s = (Stage) b.getScene().getWindow();
			s.close();
		});
		root.getChildren().addAll(t, b);
		b.setPadding(new Insets(12));
		t.setTranslateY(-15);
		window.setScene(new Scene(root, 200, 200));
		window.show();
	}

	public void radioButtonClicked() {
		Reflection ref = new Reflection();
		ref.setInput(new SepiaTone());
		tg = new ToggleGroup();
		manual.setToggleGroup(tg);
		auto.setToggleGroup(tg);
		if (tg.getSelectedToggle().equals(manual)) {
			manual.setEffect(ref);
			auto.setEffect(null);
			logScreen.appendText("Manual Mode Activated.\n");
			manualFlag = true;
			autoFlag = false;
		}
		if (tg.getSelectedToggle().equals(auto)) { // Autopilot
			manual.setEffect(null);
			auto.setEffect(ref);
			manualFlag = false;
			autoFlag = true;
			FileChooser fc = new FileChooser();
			fc.setTitle("Load File to interpret automatically");
			fc.setInitialDirectory(new File("./resources"));
			fc.setSelectedExtensionFilter(new ExtensionFilter("Text Files", "*.txt"));
			File selectedFile = fc.showOpenDialog(this);
			try {
				if (selectedFile != null) {
					Scanner sc = new Scanner(selectedFile); // display chosen file in text area
					while (sc.hasNextLine()) {
						textArea.appendText(sc.nextLine());
						textArea.appendText("\n");
					}
					sc.close();
					fileName.setValue(selectedFile.getName());
					vm.interpret();
				}
			} catch (FileNotFoundException e) {}
			logScreen.appendText("Autopilot mode Activated.\n");
		}

	}

	public void innerPressed(MouseEvent e) {
		if (manualFlag) {
			joystick.innerPressed(e);
		}
	}

	public void innerDragged(MouseEvent e) {
		if (manualFlag) {
			joystick.innerDragged(e);
			vm.sendElevatorValues();
			vm.sendAileronValues();
		}
	}

	public void innerReleased(MouseEvent e) {
		joystick.innerReleased(e);
		vm.sendElevatorValues();
		vm.sendAileronValues();
	}

	public void mapClicked(MouseEvent e) {
		if (mapDrawer.heightData != null) {
			exitPos.set(mapDrawer.setRoute((e.getSceneX() - 5), (e.getSceneY() - 60)));
			vm.setExitPosition();
			vm.calculatePath();
			if (isConnectedToSolver.get()) {
				logScreen.appendText("Displaying shortest path.\n");
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		logScreen.setEditable(false);
		propertyMat = new Property<>();
		csv = new Property<>();
		ipSim = new Property<>();
		portSim = new Property<>();
		ipSolver = new Property<>();
		portSolver = new Property<>();
		isConnectedToSolver = new SimpleBooleanProperty();
		curAirplaneLocation = new Position(0, 0);
		exitPos = new Property<>();
		joystick = new JoystickController(innerCircle, outerCircle, rudderSlider, throttleSlider);
		fileName = new SimpleStringProperty();
		shortestPath = new SimpleStringProperty();
		manual.setSelected(true);
		auto.setEffect(null);
		Reflection ref = new Reflection();
		ref.setInput(new SepiaTone());
		manual.setEffect(ref);
		manualFlag = true;
		throttleSlider.setMin(0);
		throttleSlider.setMax(1);
		rudderSlider.setMin(-1);
		rudderSlider.setMax(1);

		joystick.rudder.valueProperty().addListener((ov, old_val, new_val) -> {
			if (manualFlag)
				vm.sendRudderValues();
		});
		joystick.throttle.valueProperty().addListener((ov, old_val, new_val) -> {
			if (manualFlag)
				vm.sendThrottleValues();
		});
	}

	@Override
	public void update(Observable o, Object arg) {
		String data = (String) arg;
		if (data.equals("airplane")) {
			airplanePosX = vm.airplanePosX.get();
			airplanePosY = vm.airplanePosY.get();
			onAirplanePositionChange(); // painting airplane
		}
		if (data.equals("shortest path")) {
			mapDrawer.paintPath(shortestPath.get(), curAirplaneLocation);
		}
		if (data.equals("matrix")) {
			mapDrawer.setHeightData(propertyMat.get()); // painting map
			mapDrawer.setCursor(Cursor.HAND);
		}

		if (data.equals("not connected")) {
			logScreen.appendText("Please connect to a solver server.\n");
		}

	}

}
