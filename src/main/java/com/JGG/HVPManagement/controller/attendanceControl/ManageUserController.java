package com.JGG.HVPManagement.controller.attendanceControl;

import com.JGG.HVPManagement.controller.attendanceControl.AddImageController;
import com.JGG.HVPManagement.dao.UserDAO;
import com.JGG.HVPManagement.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.ResourceBundle;

public class ManageUserController implements Initializable {
    public Button btnRegister;
    public Button btnCancel;
    public TextField txtId;
    public PasswordField txtPass;
    public TextField txtUser;
    public TextField txtlastName;
    public TextField txtName;
    public Button btnAddPicture;
    public CheckBox chkActive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.txtId.setText(String.valueOf(UserDAO.getInstance().getMaxID() + 1));
        this.chkActive.setSelected(true);
    }

    public void register(ActionEvent event) {
        boolean isValid = true;

        int id = Integer.parseInt(txtId.getText());
        String name = txtName.getText();
        String lastName = txtlastName.getText();
        String userName = txtUser.getText().toUpperCase();
        String pass = txtPass.getText();
        boolean isActive = chkActive.isSelected();
        String errorList = "No se ha podido registrar el usuario, porque se encontraron los siguientes errores:\n";

        User user = new User(id, name, lastName, userName, pass, isActive);

        if (UserDAO.getInstance().getUserbyId(user.getId()) != null) {
            errorList += "Id ya registrado\n";
            isValid = false;
        }

        if (name.length() <= 3) {
            errorList += "El nombre no puede tener menos de tres caracteres\n";
            isValid = false;
        }
        if (lastName.length() <= 3) {
            errorList += "El apellido no puede tener menos de tres caracteres\n";
            isValid = false;
        }
        if (userName.length() != 3) {
            errorList += "El usuario se debe conformar por tres caracteres\n";
            isValid = false;
        }
        if (UserDAO.getInstance().getUserbyUserName(user.getUserName()) != null) {
            errorList += "Usuario ya registrado\n";
            isValid = false;
        }

        if (pass.length() < 4 || pass.length() > 11) {
            errorList += "El password debe tener entre 4 y 10 caracteres\n";
            isValid = false;
        }

        if (isValid) {
            // TODO test 20200810... Before user.addUser();
            UserDAO.getInstance().createUser(user);
            addPicture(user);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText(errorList);
            alert.showAndWait();
        }
    }

    public void checkDigits(TextField textField, String regex) {
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            change.setText(change.getText().replaceAll(regex, ""));
            return change;
        });
        textField.setTextFormatter(formatter);
    }

    public void validateNumbers(KeyEvent keyEvent) {
        DecimalFormat format = new DecimalFormat("#.0");
        txtId.setTextFormatter(new TextFormatter<>(c ->
        {
            System.out.println(c.getText());
            if (c.getControlNewText().isEmpty()) {
                return c;
            }

            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            } else {
                return c;
            }
        }));
    }

    public void generateUserName() {
        try {
            String name = txtName.getText();
            String lastName = txtlastName.getText();
            String fullName = name + " " + lastName;
            String[] nameElements = fullName.split("\\s+");

            char firstChar = nameElements[0].charAt(0);
            char secondChar = nameElements[nameElements.length - 2].charAt(0);
            char thirdChar = nameElements[nameElements.length - 1].charAt(0);
            String userName = (firstChar + Character.toString(secondChar) + thirdChar).toUpperCase();

            txtUser.setText(userName);
        } catch (IndexOutOfBoundsException ignored) {

        }
    }

    public void addPicture(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("view/AddImage.fxml"));
            Parent root = fxmlLoader.load();
            AddImageController controller = fxmlLoader.getController();
            controller.initData(user);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.showAndWait();

            Stage thisStage = (Stage) btnCancel.getScene().getWindow();
            thisStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
