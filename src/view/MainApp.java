package view;
	
import Models.AirplaneListenerModel;
import Models.ConnectModel;
import Models.MatrixModel;
import interpreter.ConnectCommand;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {
	
	// AirplaneListenerModel alm;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			AirplaneListenerModel alm = new AirplaneListenerModel(5500);
			MatrixModel matModel = new MatrixModel();
			ConnectModel conModel = new ConnectModel();
			ViewModel vm = new ViewModel(matModel, alm, conModel);
			alm.addObserver(vm);
			matModel.addObserver(vm);
			conModel.addObserver(vm);
			FXMLLoader fxl = new FXMLLoader();
			MainWindowController mwc;
			// BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			BorderPane root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			mwc = fxl.getController();
			if(mwc == null) {
				System.out.println("mwc in null");
			}
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
