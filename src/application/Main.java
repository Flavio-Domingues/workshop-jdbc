package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static Scene scene;

	@Override
	public void start(Stage stage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load();
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);
			scene = new Scene(scrollPane);
			stage.setScene(scene);
			stage.setTitle("WorkShop");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Scene getScene() {
		return scene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
