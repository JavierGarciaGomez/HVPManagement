package com.JGG.HVPManagement.model;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

public class Utilities {
    // todo change all utilities instances
    private final static Utilities instance = new Utilities();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");


    public static Utilities getInstance() {
        return instance;
    }


    public boolean showAlert(Alert.AlertType alertType, String title, String contentText) {
        boolean confirm = false;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(contentText);

        if (alertType == Alert.AlertType.CONFIRMATION) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                confirm = true;
            }
        } else {
            alert.show();
        }
        return confirm;
    }

    public String getNowAsText() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String nowAsText = dtf.format(now);
        return nowAsText;
    }

    public static void main(String[] args) throws ParseException {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String time = dateTimeFormatter.format(now);
        System.out.println(time);

        Timestamp timestamp = Timestamp.valueOf(now);
        System.out.println("Timestamp" + timestamp);
    }

    public Date StringToDate(String string) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(string);
        return date;
    }

    public String getDateAsString(LocalDateTime localDateTime) {
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
            if (wait) {
                stage.showAndWait();
            } else
                stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // todo delete
    public int getQuartersWorked(LocalDate startingDate, LocalDate endingDate) {
        LocalDate newEndingDate = endingDate;
        int month = endingDate.getMonthValue();

        if (month <= 3) {
            newEndingDate = endingDate.withMonth(1);
        } else if (month <= 6) {
            newEndingDate = endingDate.withMonth(4);
        } else if (month <= 9) {
            newEndingDate = endingDate.withMonth(7);
        } else {
            newEndingDate = endingDate.withMonth(10);
        }
        System.out.println(startingDate + " " + newEndingDate);
        System.out.println("Passed months " + ChronoUnit.MONTHS.between(startingDate.withDayOfMonth(1), newEndingDate.withDayOfMonth(1)));
        return ((int) ChronoUnit.MONTHS.between(startingDate.withDayOfMonth(1), newEndingDate.withDayOfMonth(1))) / 3;
    }


    public LocalDate getLocalDateOrReturnToday(LocalDate localDate) {
        if (localDate == null) {
            return LocalDate.now();
        } else {
            return localDate;
        }
    }

    public long getDaysBetweenOrReturnZero(LocalDate startingDate, LocalDate endingDate) {
        try {
            return ChronoUnit.DAYS.between(startingDate, endingDate);
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public int getQuartersWorkedOrReturnZero(LocalDate startingDate, LocalDate endingDate) {
        try {
            int month = endingDate.getMonthValue();
            if (month <= 3) {
                endingDate = endingDate.withMonth(1);
            } else if (month <= 6) {
                endingDate = endingDate.withMonth(4);
            } else if (month <= 9) {
                endingDate = endingDate.withMonth(7);
            } else {
                endingDate = endingDate.withMonth(10);
            }
            System.out.println("Passed months " + ChronoUnit.MONTHS.between(startingDate.withDayOfMonth(1), endingDate.withDayOfMonth(1)));
            return ((int) ChronoUnit.MONTHS.between(startingDate.withDayOfMonth(1), endingDate.withDayOfMonth(1))) / 3;
        } catch (NullPointerException e) {
            return 0;
        }

    }

    public double getDoubleOrReturnZero(Integer value) {
        try {
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getSeniorityWageBonus(double yearlyPercentageWageBonus, LocalDate startingDate, LocalDate endingDate) {
        try {
            if (startingDate != null && endingDate != null && yearlyPercentageWageBonus > 0) {
                int month = endingDate.getMonthValue();
                if (month <= 3) {
                    endingDate = endingDate.withMonth(1);
                } else if (month <= 6) {
                    endingDate = endingDate.withMonth(4);
                } else if (month <= 9) {
                    endingDate = endingDate.withMonth(7);
                } else {
                    endingDate = endingDate.withMonth(10);
                }
                long years = ChronoUnit.YEARS.between(startingDate, endingDate.withDayOfMonth(1));

                return years * yearlyPercentageWageBonus;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public double getGrossWage(double wageBase, double wageProportion, double seniorityWageBonus, double degreeBonus, double fixedWageBonus) {
        if (wageBase > 0 && wageProportion > 0) {
            return wageBase * (1 + seniorityWageBonus) * wageProportion + degreeBonus + fixedWageBonus;
        } else {
            return 0;
        }
    }

    public double convertStringToDouble(String text) {
        try{
            return Double.parseDouble(text);
        }catch(NumberFormatException ignore){
            return 0;
        }
    }
}
