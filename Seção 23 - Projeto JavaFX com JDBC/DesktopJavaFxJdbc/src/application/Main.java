package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

	// Como iremos reutilizar o <MenuBar> da cena principal em todo o programa
	// devemos criar uma refer�ncia est�tica a ela.

	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));

			// O ScrollPane ser� nosso container a pai.
			ScrollPane scrollPane = loader.load();

			// Definindo que a nosso container principal ir� ocupar toda largura e altura da
			// tela(palco).
			scrollPane.setFitToHeight(true);
			scrollPane.setFitToWidth(true);

			// Cena principal
			mainScene = new Scene(scrollPane);
			primaryStage.setScene(mainScene);
			// T�tulo do palco
			primaryStage.setTitle("Sample JavaFX application");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Scene getMainScene() {
		return mainScene;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
