package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	private Department entity;
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	@FXML
	private Label lblErrorName;

	@FXML
	public void onbtnSaveAction() {
		System.out.println("onbtnSaveAction");

	}

	@FXML
	public void onbtnCancelAction() {
		System.out.println("onbtnCancelAction");

	}

	public void SetDepartment(Department entity) {
		this.entity = entity;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFildInteger(txtId);
		Constraints.setTextFildMaxLenght(txtName, 15);

	}

	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

}
