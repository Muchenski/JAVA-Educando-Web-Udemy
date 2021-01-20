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
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	private Seller seller;

	private DepartmentService departmentService;

	private SellerService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<DataChangeListener>();

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

	@FXML
	private TextField textFieldEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField textFieldBaseSalary;

	@FXML
	private ComboBox<Department> cboDepartments;

	@FXML
	private ObservableList<Department> obsDepartments;

	@FXML
	private Button buttonSave;

	@FXML
	private Button buttonCancel;

	@FXML
	public void onButtonCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@FXML
	public void onButtonNewAction(ActionEvent event) {
		if (seller == null || service == null) {
			throw new IllegalStateException("Service or Seller was null!");
		}

		try {
			Seller seller = getFormData();
			service.saveOrUpdate(seller);
			notifyDataChangeListeners();
			Alerts.showAlert("Seller Save", null, seller.getName() + " SAVED!", AlertType.CONFIRMATION);
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving seller", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private Seller getFormData() {

		ValidationException exception = new ValidationException("Validation error");

		if (textFieldName.getText() == null || textFieldName.getText().trim().equals("")) {
			exception.addError("name", "Field can't be empty!");
		}

		if (textFieldEmail.getText() == null || textFieldEmail.getText().trim().equals("")) {
			exception.addError("email", "Field can't be empty!");
		}

		if (dpBirthDate.getValue() == null) {
			exception.addError("birthDate", "Field can't be empty!");
		}

		if (textFieldBaseSalary.getText() == null || textFieldBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Field can't be empty!");
		}

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));

		return new Seller(Utils.tryParseToInt(textFieldId.getText()), textFieldName.getText(), textFieldEmail.getText(),
				Date.from(instant), Utils.tryParseToDouble(textFieldBaseSalary.getText()), cboDepartments.getValue());
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	// Inje��o de dependencia manual, em rela��o a entidade Seller
	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	// Inje��o de dependencia manual, em rela��o a entidade SellerService e
	// DepartmentService
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 50);
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		Constraints.setTextFieldMaxLength(textFieldEmail, 100);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void updateFormData() {
		if (seller == null) {
			throw new IllegalStateException("Seller was null!");
		}
		textFieldId.setText(String.valueOf(seller.getId()));
		textFieldName.setText(seller.getName());
		textFieldEmail.setText(seller.getEmail());
		Locale.setDefault(Locale.US);
		textFieldBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
		if (seller.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}
		if (seller.getDepartment() == null) {
			cboDepartments.getSelectionModel().selectFirst();
		} else {
			cboDepartments.setValue(seller.getDepartment());
		}
	}

	private void setErrorMessages(Map<String, String> erros) {
		Set<String> fields = erros.keySet();
		if (fields.contains("name")) {
			labelErrorName.setText(erros.get("name"));
		} else {
			labelErrorName.setText("");
		}
		if (fields.contains("email")) {
			labelErrorEmail.setText(erros.get("email"));
		} else {
			labelErrorEmail.setText("");
		}
		if (fields.contains("baseSalary")) {
			labelErrorBaseSalary.setText(erros.get("baseSalary"));
		} else {
			labelErrorBaseSalary.setText("");
		}
		if (fields.contains("birthDate")) {
			labelErrorBirthDate.setText(erros.get("birthDate"));
		} else {
			labelErrorBirthDate.setText("");
		}
	}

	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null!");
		}
		List<Department> departments = departmentService.findAll();
		obsDepartments = FXCollections.observableArrayList(departments);
		cboDepartments.setItems(obsDepartments);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		cboDepartments.setCellFactory(factory);
		cboDepartments.setButtonCell(factory.call(null));
	}

}
