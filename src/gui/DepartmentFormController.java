package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	private Department entity;
	private DepartmentService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
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
	public void onbtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		if (service == null) {
			throw new IllegalStateException("service was null :/");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifysubcribeDataChangeListener();
			Utils.currentStage(event).close();
		}catch(ValidationException e) {
			setMessageError(e.getError());
		}
		catch (DbException e) {
			Alerts.showAlerts("error saving department", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifysubcribeDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Department getFormData() {
		Department obj = new Department();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "campo obrigatório!");
		}
		if (exception.getError().size() > 0) {
			throw exception;
		}
		obj.setName(txtName.getText());
		return obj;

	}

	@FXML
	public void onbtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();

	}

	public void SetDepartment(Department entity) {
		this.entity = entity;
	}

	public void SetDepartmentService(DepartmentService service) {
		this.service = service;
	}

	public void subcribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
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
		if (entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

	public void setMessageError(Map<String, String> errors) {
		Set<String> filds = errors.keySet();
		if (filds.contains("name")) {
			lblErrorName.setText(errors.get("name"));
		}
	}

}
