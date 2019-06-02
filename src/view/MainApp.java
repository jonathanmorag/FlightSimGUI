package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import models.AirplaneListenerModel;
import models.ConnectModel;
import models.MatrixModel;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {
	
	// AirplaneListenerModel alm;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("FlightGear Simulator GUI Application");
			primaryStage.getIcons().add(new Image("file:resources/App_icon2.png"));
			AirplaneListenerModel alm = new AirplaneListenerModel(5500);
			MatrixModel matModel = new MatrixModel();
			ConnectModel conModel = new ConnectModel();
			ViewModel vm = new ViewModel(matModel, alm, conModel);
			alm.addObserver(vm);
			matModel.addObserver(vm);
			conModel.addObserver(vm);
			FXMLLoader fxl = new FXMLLoader();
			MainWindowController mwc;
			BorderPane root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());
			Scene scene = new Scene(root,800,700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			mwc = fxl.getController();
			mwc.setViewModel(vm);
			vm.addObserver(mwc);
			alm.start();
			primaryStage.setScene(scene);
			primaryStage.show();
			
			primaryStage.setOnCloseRequest(e-> {
				alm.stop();
			});
		} catch(Exception e) {e.printStackTrace();}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
