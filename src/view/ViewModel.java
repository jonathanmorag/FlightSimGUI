package view;

import java.util.Observable;
import java.util.Observer;

import Models.AirplaneListenerModel;
import Models.ConnectModel;
import Models.MatrixModel;
import Models.Property;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import matrix.Matrix;
import matrix.Position;

public class ViewModel extends Observable implements Observer {
	
	MatrixModel matrixModel;
	AirplaneListenerModel airplaneModel;
	ConnectModel connectModel;
	
	
	//properties
	public IntegerProperty airplanePosX;
	public IntegerProperty airplanePosY;
	
	public Property<Matrix> propertyMat;
	public Property<String[]> csv;
	
	public Property<Position> exitPos;
	
	//controls
	public DoubleProperty aileron;
	public DoubleProperty elevator;
	public DoubleProperty throttle;
	public DoubleProperty rudder;
	
	
	public Property<String> ipSolver;
	public Property<String> portSolver;
	//connect button
	public Property<String> ipSim;
	public Property<String> portSim;
	
	public ViewModel(MatrixModel matrixModel ,AirplaneListenerModel airplaneModel, ConnectModel connectModel) {
		this.matrixModel = matrixModel;
		this.airplaneModel = airplaneModel;
		this.connectModel = connectModel;
		airplanePosX = new SimpleIntegerProperty();
		airplanePosY = new SimpleIntegerProperty();
		
		//controls
		aileron = new SimpleDoubleProperty();
		elevator = new SimpleDoubleProperty();
		rudder = new SimpleDoubleProperty();
		throttle = new SimpleDoubleProperty();
		
		propertyMat = new Property<>();
		csv = new Property<>();
		ipSim = new Property<>();
		portSim = new Property<>();
		ipSolver = new Property<>();
		portSolver = new Property<>();
		exitPos = new Property<>();
		
		
		//solverIP = new SimpleStringProperty();
		//solverPort = new SimpleIntegerProperty();
		//...
	}
	
	//send values to simulator
	public void sendRudderValues() {
		connectModel.sendCommandToSimulator("set /controls/flight/rudder ",rudder.get());
	}
	public void sendAileronValues() {
		connectModel.sendCommandToSimulator("set /controls/flight/aileron ",aileron.get());
	}
	public void sendThrottleValues() {
		connectModel.sendCommandToSimulator("set /controls/engines/current-engine/throttle ",throttle.get());
	}
	public void sendElevatorValues() {
		connectModel.sendCommandToSimulator("set /controls/flight/elevator ",elevator.get());
	}
	
	public void setExitPosition() {
		matrixModel.setExitPosition(exitPos.get());
	}
	
	public void buildMatrix() {
		matrixModel.buildMatrix(csv.get());
	}
	
	public void connectToSimulator() {
		connectModel.connect(ipSim.get(), Integer.parseInt(portSim.get()));
	}
	
	public void requestSolution() {
		matrixModel.requestSolution();
	}
	public void connectToSolver() {
		matrixModel.connect(ipSolver.get(), Integer.parseInt(portSolver.get()));
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		if (o == airplaneModel) {
			this.airplanePosX.set(airplaneModel.getAirplanePosition().col);
			this.airplanePosY.set(airplaneModel.getAirplanePosition().row);
			setChanged();
			notifyObservers("airplane");
		}
		if (o == matrixModel) {
			propertyMat.set(matrixModel.getMatrix());
			airplaneModel.setStartCooX(matrixModel.getStartCooX());
			airplaneModel.setStartCooY(matrixModel.getStartCooY());
			setChanged();
			notifyObservers("matrix");
		}
	}

}
