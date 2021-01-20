package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department department;

	private DepartmentService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<DataChangeListener>();

	@FXML
	private Label labelError;

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldName;

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
		if (department == null || service == null) {
			throw new IllegalStateException("Service or Department was null!");
		}

		try {

			ValidationException exception = new ValidationException("Validation error");

			if (textFieldName.getText().trim().equals("") || textFieldName.getText() == null) {
				exception.addError("name", "Field can't be embty!");
			}

			if (exception.getErrors().size() > 0) {
				throw exception;
			}

			Department department = new Department(Utils.tryParseToInt(textFieldId.getText()), textFieldName.getText());
			service.saveOrUpdate(department);
			notifyDataChangeListeners();
			Alerts.showAlert("Department Save", null, department.getName() + " CREATED", AlertType.CONFIRMATION);
			Utils.currentStage(event).close();

		} catch (DbException e) {
			Alerts.showAlert("Error saving department", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	// Inje��o de dependencia manual, em rela��o a entidade Department
	public void setDepartment(Department department) {
		this.department = department;
	}

	// Inje��o de dependencia manual, em rela��o a entidade DepartmentService
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Constraints.setTextFieldMaxLength(textFieldName, 50);
		Constraints.setTextFieldInteger(textFieldId);
	}

	public void updateFormData() {
		if (department == null) {
			throw new IllegalStateException("Department was null!");
		}
		textFieldId.setText(String.valueOf(department.getId()));
		textFieldName.setText(department.getName());
	}
}
