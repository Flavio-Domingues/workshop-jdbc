package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable {
	@FXML
	private MenuItem menuItemDepartamento;

	@FXML
	private MenuItem menuItemVendedor;

	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemDepartamentoAction() {
		System.out.println("onMenuItemDepartamentoAction");

	}

	@FXML
	public void onMenuItemVendedorAction() {

	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/Sobre.fxml");

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	private void loadView(String nomeAbsoluto) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(nomeAbsoluto));
		try {
			VBox newVbox = loader.load();
			Scene mainScene = Main.getScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVbox.getChildren());

		} catch (IOException e) {
			Alerts.showAlerts("IO exception", "Erro ao carregar a tela", e.getMessage(), AlertType.ERROR);
		}

	}

}
