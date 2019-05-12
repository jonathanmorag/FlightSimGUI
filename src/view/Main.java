package view;
	
import javafx.application.Application;
import javafx.stage.Stage;
import server_side.MyClientHandler;
import server_side.MySerialServer;
import server_side.Server;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {}
	}
	
	public static void main(String[] args) {
		Server s = null;
		try {
			s = new MySerialServer(1234);
			s.start(new MyClientHandler());
			launch(args);
		} catch (Exception e) {}
		finally {
			s.stop();
		}
	}
}
