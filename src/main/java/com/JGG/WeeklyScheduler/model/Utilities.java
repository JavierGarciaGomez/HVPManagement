package com.JGG.WeeklyScheduler.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Utilities {
    // todo change all utilities instances
    private final static Utilities instance = new Utilities();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    public static Utilities getInstance(){
        return instance;
    }


    public boolean showAlert(Alert.AlertType alertType, String title, String contentText){
        boolean confirm = false;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(contentText);

        if(alertType==Alert.AlertType.CONFIRMATION){
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                confirm=true;
            }
        } else{
            alert.show();
        }
        return confirm;
    }

    public String getNowAsText(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String nowAsText=dtf.format(now);
        return nowAsText;
    }

    public static void main(String[] args) throws ParseException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String time = dateTimeFormatter.format(now);
        System.out.println(time);

        Timestamp timestamp = Timestamp.valueOf(now);
        System.out.println("Timestamp" +timestamp);
    }

    public Date StringToDate(String string) throws ParseException {
        Date date=new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(string);
        return date;
    }

    public String getDateAsString(LocalDateTime localDateTime){
        return dateTimeFormatter.format(localDateTime);
    }


    public void loadWindow(String viewPath, Stage stage, String title, StageStyle stageStyle, boolean resizable, boolean wait) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(viewPath)));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initStyle(stageStyle);
            stage.getIcons().add(new Image("/icon/HVPicon.jpg"));
            stage.setResizable(resizable);
            if(wait){
                stage.showAndWait();
            }else
                stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
