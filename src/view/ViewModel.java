package view;

import java.util.Observable;
import java.util.Observer;

import Models.AirplaneListenerModel;
import Models.ConnectModel;
import Models.MatrixModel;
import Models.Property;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import matrix.Matrix;

public class ViewModel extends Observable implements Observer {
	
	MatrixModel matrixModel;
	AirplaneListenerModel airplaneModel;
	ConnectModel connectModel;
	
	//properties
	public IntegerProperty airplanePosX;
	public IntegerProperty airplanePosY;
	
	public Property<Matrix> propertyMat;
	public Property<String[]> csv;
	
	//controls
	
	//sliders
	public DoubleProperty throttle;
	public DoubleProperty rudder;
	
	//connect button
	public Property<String> ipSim;
	public Property<String> portSim;
	
	public ViewModel(MatrixModel matrixModel ,AirplaneListenerModel airplaneModel, ConnectModel connectModel) {
		this.matrixModel = matrixModel;
		this.airplaneModel = airplaneModel;
		this.connectModel = connectModel;
		airplanePosX = new SimpleIntegerProperty();
		airplanePosY = new SimpleIntegerProperty();
		propertyMat = new Property<>();
		csv = new Property<>();
		ipSim = new Property<>();
		portSim = new Property<>();
		
		//solverIP = new SimpleStringProperty();
		//solverPort = new SimpleIntegerProperty();
		//...
	}
	public void buildMatrix() {
		matrixModel.buildMatrix(csv.get());
	}
	
	public void connect() {
		System.out.println("view model " + ipSim.get()+" ," + portSim.get());
		connectModel.connect(ipSim.get(), Integer.parseInt(portSim.get()));
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
