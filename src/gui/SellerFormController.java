package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exception.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {
	private Seller entity;
	private SellerService service;
	private DepartmentService departmentService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private DatePicker dpBirthDate;
	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Department> comboBoxDepartment;
	private ObservableList<Department> obsList;
	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	@FXML
	private Label lblErrorName;
	@FXML
	private Label lblErrorEmail;
	@FXML
	private Label lblErrorBirthDate;
	@FXML
	private Label lblErrorBaseSalary;

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
		} catch (ValidationException e) {
			setMessageError(e.getError());
		} catch (DbException e) {
			Alerts.showAlerts("error saving department", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifysubcribeDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Seller getFormData() {
		Seller obj = new Seller();
		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "campo obrigatório!");
		}
		obj.setName(txtName.getText());

		if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "campo obrigatório!");
		}
		obj.setEmail(txtEmail.getText());

		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "campo obrigatório!");
		} else {
			Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setBirthDate(Date.from(instant));
		}
		if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "campo obrigatório!");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		obj.setDepartment(comboBoxDepartment.getValue());

		if (exception.getError().size() > 0) {
			throw exception;
		}

		return obj;

	}

	@FXML
	public void onbtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();

	}

	public void SetSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subcribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		saveAndCancelImgs();

	}

	private void initializeNodes() {
		Constraints.setTextFildInteger(txtId);
		Constraints.setTextFildMaxLenght(txtName, 15);
		Constraints.setTextFildMaxLenght(txtEmail, 70);
		Constraints.setTextFildDouble(txtBaseSalary);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();

	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("entity was null :/");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}

		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
		comboBoxDepartment.setValue(entity.getDepartment());
	}

	public void setMessageError(Map<String, String> errors) {
		Set<String> filds = errors.keySet();
		lblErrorName.setText((filds.contains("name") ? errors.get("name") : ""));
		lblErrorEmail.setText((filds.contains("email") ? errors.get("email") : ""));
		lblErrorBirthDate.setText((filds.contains("birthDate") ? errors.get("birthDate") : ""));
		lblErrorBaseSalary.setText((filds.contains("baseSalary") ? errors.get("baseSalary") : ""));
	}

	public void loadAssociateObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("departmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));

	}
	public void saveAndCancelImgs() {
		ImageView imageSave = new ImageView("/img/save.png");
		ImageView imageCancel = new ImageView("/img/cancel.png");
		btnSave.setGraphic(imageSave);
		btnCancel.setGraphic(imageCancel);
		imageSave.setFitHeight(25);
		imageSave.setFitWidth(25);
		
		imageCancel.setFitHeight(25);
		imageCancel.setFitWidth(25);
		
		
	}

}
