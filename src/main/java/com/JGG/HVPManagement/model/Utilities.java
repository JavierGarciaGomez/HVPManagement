package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.entity.*;
import com.JGG.HVPManagement.interfaces.MyInitializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;


public class Utilities {
    private Model model;

    public Utilities() {
        this.model = Model.getInstance();
    }
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
            alert.showAndWait();
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

    public MyInitializable loadWindowWithInitData(String viewPath, Stage stage, String title, StageStyle stageStyle, boolean resizable, boolean wait) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(viewPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.initStyle(stageStyle);
            stage.getIcons().add(new Image("/icon/HVPicon.jpg"));
            stage.setResizable(resizable);

            MyInitializable controller = loader.getController();
            controller.initData();

            if (wait) {
                stage.showAndWait();
            } else
                stage.show();
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public double convertStringToDoubleOrReturnZero(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException ignore) {
            return 0;
        }
    }

    public String normalizeText(String originalString) {
        String normalizedString = Normalizer.normalize(originalString, Normalizer.Form.NFD);
        String newString = normalizedString.replaceAll("[^\\p{ASCII}]", "");
        return newString;
    }

    public Double convertStringToDouble(String text) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        ParsePosition parsePosition = new ParsePosition(0);
        Number number = numberFormat.parse(text, parsePosition);

        if (parsePosition.getIndex() != text.length()) {
            try {
                throw new ParseException("Invalid input", parsePosition.getIndex());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return number.doubleValue();
    }

    public String convertDoubleToString(double value) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String string = Double.toString(value);
        return string;

    }

    public Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {

            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                    return node;
                }
            }

        }
        return null;
    }

    public LocalTime convertMexicanTimeToSpainTime(LocalTime originalLocalTime) {
        LocalTime newLocalTime = originalLocalTime;
        if (TimeZone.getDefault().getID().equals("Europe/Paris")) {
            newLocalTime = originalLocalTime.minusHours(7);
        }
        return newLocalTime;
    }

    public int convertToMexicanHour(int hour) {
        int newHour = hour;
        if (TimeZone.getDefault().getID().equals("Europe/Paris")) {
            if (hour < 7) {
                newHour += 17;
            } else {
                newHour -= 7;
            }
        }
        System.out.println("CONVERTED FROM " + hour + " to new: " + newHour);
        return newHour;
    }

    public LocalDate getMondayLocalDate(LocalDate localDate) {
        if (localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            return localDate;
        } else {
            return localDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        }
    }

    public void clearGridPanesNodesChildren(GridPane gridPane, int startingCol, int startingRow) {
        for (int col = startingCol; col < gridPane.getColumnCount(); col++) {
            for (int row = startingRow; row < gridPane.getRowCount(); row++) {
                Node node = getNodeFromGridPane(gridPane, col, row);
                Pane pane = (Pane) node;
                pane.getChildren().clear();
            }
        }
    }

    public void clearGridPaneChildren(GridPane gridPane, int startingCol, int startingRow) {
        for (int col = startingCol; col < gridPane.getColumnCount(); col++) {
            for (int row = startingRow; row < gridPane.getRowCount(); row++) {
                Node node = getNodeFromGridPane(gridPane, col, row);
                gridPane.getChildren().remove(node);
            }
        }
    }

    public Branch getBranchByName(String branchName) {
        if (branchName.equals("")) return null;
        for (Branch branch : model.branches) {
            if (branch.getName().equals(branchName)) {
                return branch;
            }
        }
        return null;
    }

    public WorkingDayType getWorkingDayTypeByAbbr(String abbr) {
        if (abbr.equals("")) return null;
        for (WorkingDayType workingDayType : model.workingDayTypeList) {
            if (workingDayType.getAbbr().equals(abbr)) {
                return workingDayType;
            }
        }
        return null;
    }

    public void addChangeListenerToTimeField(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String inputText = textField.getText();
                try {
                    if (inputText.equals("")) {
                        return;
                    }
                    if (inputText.length() <= 2) {
                        Integer.parseInt(inputText);
                        if (inputText.length() == 1) inputText = "0" + inputText;
                        textField.setText(inputText + ":00");
                    } else {
                        LocalTime.parse(inputText);
                    }
                } catch (NumberFormatException | DateTimeParseException e) {
                    this.showAlert(Alert.AlertType.ERROR, "Time format error", "The hour format is incorrect, it has to be like 10:00 or just the hour: 12");
                    textField.setText("");
                    textField.requestFocus();
                }
            }
        });
    }

    public JobPosition getJobPositionByName(String jobPositionName) {
        for (JobPosition jobPosition : model.jobPositions) {
            if (jobPosition.getName().equals(jobPositionName)) {
                return jobPosition;
            }
        }
        return null;
    }

    public OpeningHours getOpeningHoursByBranchAndDate(Branch branch, LocalDate localDate) {
        int difInDays = Integer.MAX_VALUE;
        int difInDaysAux;
        OpeningHours tempOpeningHours = null;
        for (OpeningHours openingHours : model.openingHoursList) {
            if (openingHours.getBranch().equals(branch) && (localDate.isAfter(openingHours.getStartDate()) ||
                    (localDate.isEqual(openingHours.getStartDate())))) {
                difInDaysAux = (int) ChronoUnit.DAYS.between(openingHours.getStartDate(), localDate);
                if (difInDaysAux < difInDays) {
                    difInDays = difInDaysAux;
                    tempOpeningHours = openingHours;
                }
            }
        }
        return tempOpeningHours;
    }

    public AttendanceRegister getLastAttendanceRegisterByCollaborator(Collaborator collaborator) {
        LocalDateTime localDateTime = LocalDateTime.MIN;
        AttendanceRegister lastAttendanceRegister = null;
        for (AttendanceRegister attendanceRegister : model.attendanceRegisters) {
            if (attendanceRegister.getCollaborator().equals(collaborator)) {
                if (attendanceRegister.getLocalDateTime().isAfter(localDateTime)) {
                    lastAttendanceRegister = attendanceRegister;
                }
            }
        }
        return lastAttendanceRegister;
    }

    public WorkSchedule getWorkScheduleByLastAttendanceRegister(AttendanceRegister lastAttendanceRegister) {
        Collaborator collaborator = lastAttendanceRegister.getCollaborator();
        WorkSchedule workSchedule = null;
        LocalDate localDate = null;
        if (lastAttendanceRegister.getLocalDateTime() == null) {
            localDate = LocalDate.now();
        } else {
            localDate = lastAttendanceRegister.getLocalDateTime().toLocalDate();
        }
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            if (tempWorkSchedule.getCollaborator().equals(collaborator)) {
                if (tempWorkSchedule.getWorkingDayType().getItNeedHours()) {
                    // if the conditions of today are met, retrieve today; if not, retrieve the next day
                    if (tempWorkSchedule.getLocalDate().equals(localDate)) {
                        workSchedule = tempWorkSchedule;
                    } else {
                        localDate = localDate.plusDays(1);
                    }
                }
            }
        }
        return workSchedule;
    }

    public WorkSchedule getWorkScheduleByCollaboratorAndDate(Collaborator collaborator, LocalDate localDate) {
        WorkSchedule workSchedule = null;
        for (WorkSchedule tempWorkSchedule : model.tempWorkSchedules) {
            if (tempWorkSchedule.getCollaborator().equals(collaborator)) {
                if (tempWorkSchedule.getWorkingDayType().getItNeedHours()) {
                    if (tempWorkSchedule.getLocalDate().equals(localDate)) {
                        workSchedule = tempWorkSchedule;
                    }
                }
            }
        }
        return workSchedule;
    }
}
