package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import server_side.AirplaneListener;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class MainApp extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AirplaneListener al = new AirplaneListener(5500);
			FXMLLoader fxl = new FXMLLoader();
			// BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			BorderPane root = fxl.load(getClass().getResource("MainWindow.fxml").openStream());
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			MainWindowController mwc = fxl.getController();
			al.addObserver(mwc);
			al.start();
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
