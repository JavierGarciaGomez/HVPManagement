package com.JGG.HVPManagement.model;

import com.JGG.HVPManagement.dao.*;
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
import java.time.*;
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
        if (branchName == null || branchName.equals("")) return null;
        for (Branch branch : model.branches) {
            if (branch.getName().equals(branchName)) {
                return branch;
            }
        }
        return null;
    }

    public WorkingDayType getWorkingDayTypeByAbbr(String abbr) {
        if (abbr.equals("")) return null;
        for (WorkingDayType workingDayType : model.workingDayTypes) {
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

    // todo review, now the opening hours has a end date
    // todo review, maybe load the list with a runnable
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

    public List<OpeningHoursDetailed> getOpeningHoursDetailedListByDate(LocalDate startDate, LocalDate endDate) {
        List<OpeningHoursDetailed> openingHoursDetailedList = new ArrayList<>();
        for (Branch branch : model.branches) {
            for (LocalDate localDate = startDate; localDate.isBefore(endDate.plusDays(1)); localDate = localDate.plusDays(1)) {
                for (OpeningHours openingHours : model.openingHoursList) {
                    if (openingHours.getBranch().equals(branch)) {
                        if (localDate.isAfter(openingHours.getStartDate()) &&
                                (openingHours.getEndDate() == null || localDate.isBefore(openingHours.getEndDate()))) {
                            OpeningHoursDetailed openingHoursDetailed = new OpeningHoursDetailed();
                            openingHoursDetailed.setBranch(branch);
                            openingHoursDetailed.setLocalDate(localDate);
                            openingHoursDetailed.setOpeningHour(LocalDateTime.of(localDate, openingHours.getOpeningHour()));
                            LocalDateTime closingDateTime = LocalDateTime.of(localDate, openingHours.getClosingHour());
                            if (openingHours.getClosingHour().isBefore(openingHours.getOpeningHour())) {
                                closingDateTime = closingDateTime.plusDays(1);
                            }
                            openingHoursDetailed.setClosingHour(closingDateTime);
                            openingHoursDetailedList.add(openingHoursDetailed);
                        }
                    }
                }
            }
        }
        return openingHoursDetailedList;
    }

    public AttendanceRegister getLastAttendanceRegisterByCollaborator(Collaborator collaborator) {
        AttendanceRegister attendanceRegister = null;
        LocalDateTime localDateTime = LocalDateTime.MIN;
        for (AttendanceRegister tempAttendanceRegister : model.attendanceRegisters) {
            if (tempAttendanceRegister.getCollaborator().equals(collaborator) && tempAttendanceRegister.getLocalDateTime().isAfter(localDateTime)) {
                localDateTime = tempAttendanceRegister.getLocalDateTime();
                attendanceRegister = tempAttendanceRegister;
            }
        }
        return attendanceRegister;
    }

    public WorkSchedule getNextWorkScheduleByLastAttendanceRegisterOld(AttendanceRegister lastAttendanceRegister, Collaborator collaborator) {
        WorkSchedule workSchedule = new WorkSchedule();
        // set the startDate. If there is no previous register, startDate is now
        LocalDate startDate = LocalDate.now();
        // set the startDate. If there is a previous register startdate is the date of the last register, but if an action was an exit, then chages it to next day
        if (lastAttendanceRegister != null) {
            startDate = lastAttendanceRegister.getLocalDateTime().toLocalDate();
            if (lastAttendanceRegister.getLocalDateTime().toLocalDate().equals(startDate) && lastAttendanceRegister.getAction().equals("Salida")) {
                startDate = startDate.plusDays(1);
            }
        }


        // loop to find the next workschedule of the startDate
        for (LocalDate localDate = startDate; localDate.isBefore(startDate.plusDays(6)); localDate = localDate.plusDays(1)) {
            for (WorkSchedule tempWorkSchedule : model.workSchedules) {
                if (tempWorkSchedule.getCollaborator().equals(collaborator)) {
                    if (tempWorkSchedule.getWorkingDayType().isItNeedHours()) {
                        if (tempWorkSchedule.getLocalDate().equals(localDate)) {
                            return tempWorkSchedule;
                        }
                    }
                }
            }
        }
        return workSchedule;
    }

    public LocalDate getMexicanDate(LocalDateTime localDateTime) {
        Locale spainLocale = new Locale("es", "ES", "");
        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale.equals(spainLocale)) {
            ZoneId oldZonee = ZoneId.of("Europe/Madrid");
            ZoneId newZonee = ZoneId.of("America/Mexico_City");
            localDateTime = localDateTime.atZone(oldZonee).withZoneSameInstant(newZonee).toLocalDateTime();
        }
        return localDateTime.toLocalDate();
    }

    public WorkSchedule getNextWorkScheduleByLastAttendanceRegister(AttendanceRegister lastAttendanceRegister, Collaborator collaborator) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Mexico/General"));
        LocalDate zonedLocalDate = zonedDateTime.toLocalDate();
        LocalDate startDate = zonedLocalDate;

        if (lastAttendanceRegister != null) {
            if (lastAttendanceRegister.getLocalDateTime().isAfter(zonedDateTime.toLocalDateTime()) && lastAttendanceRegister.getAction().equals("Salida")) {
                startDate = startDate.plusDays(1);
            }
        }

        for (LocalDate localDate = startDate; localDate.isBefore(startDate.plusDays(6)); localDate = localDate.plusDays(1)) {
            for (WorkSchedule tempWorkSchedule : model.workSchedules) {
                if (tempWorkSchedule.getCollaborator().equals(collaborator) && tempWorkSchedule.getWorkingDayType().isItNeedHours()
                        && tempWorkSchedule.getLocalDate().equals(localDate)) {
                    return tempWorkSchedule;
                }
            }
        }
        return null;
    }

    public WorkSchedule getWorkScheduleWithHoursByCollaboratorAndDate(Collaborator collaborator, LocalDate localDate) {
        WorkSchedule workSchedule = null;
        for (WorkSchedule tempWorkSchedule : model.workSchedules) {
            if (tempWorkSchedule.getCollaborator().equals(collaborator)) {
                if (tempWorkSchedule.getWorkingDayType().isItNeedHours()) {
                    if (tempWorkSchedule.getLocalDate().equals(localDate)) {
                        workSchedule = tempWorkSchedule;
                        break;
                    }
                }
            }
        }
        return workSchedule;
    }

    public WorkSchedule getWorkScheduleByCollaboratorAndDate(Collaborator collaborator, LocalDate localDate) {
        WorkSchedule workSchedule = null;
        for (WorkSchedule tempWorkSchedule : model.workSchedules) {
            if (collaborator.getId() == (tempWorkSchedule.getCollaborator().getId())) {
                if (tempWorkSchedule.getLocalDate().equals(localDate)) {
                    workSchedule = tempWorkSchedule;
                    break;
                }
            }
        }
        return workSchedule;
    }

    public boolean checkIfAttendanceRegisterExists(AttendanceRegister tempAttendanceRegister) {
        LocalDate localDate = tempAttendanceRegister.getLocalDateTime().toLocalDate();
        Collaborator collaborator = tempAttendanceRegister.getCollaborator();
        String action = tempAttendanceRegister.getAction();
        for (AttendanceRegister attendanceRegister : model.attendanceRegisters) {
            if (attendanceRegister.getLocalDateTime().toLocalDate().equals(localDate) && attendanceRegister.getCollaborator().equals(collaborator) && attendanceRegister.getAction().equals(action)) {
                return true;
            }
        }
        return false;
    }

    public LocalDate getFirstDayOfTheFortNight(LocalDate localDate) {
        LocalDate lastDayOfTheLastMonth;
        LocalDate lastDayOfThisMonth;
        LocalDate day14thOfTheMonth;
        LocalDate firstDayFortNight;
        lastDayOfThisMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        lastDayOfTheLastMonth = localDate.minusMonths(1);
        lastDayOfTheLastMonth = lastDayOfTheLastMonth.with(TemporalAdjusters.lastDayOfMonth());
        day14thOfTheMonth = localDate.withDayOfMonth(14);

        firstDayFortNight = lastDayOfTheLastMonth;
        if (localDate.isAfter(day14thOfTheMonth) && localDate.isBefore(lastDayOfThisMonth)) {
            firstDayFortNight = day14thOfTheMonth.plusDays(1);
        }
        if (localDate.isEqual(lastDayOfThisMonth)) {
            firstDayFortNight = localDate;
        }

        return firstDayFortNight;
    }

    public LocalDate getLastDayOfTheFortNight(LocalDate localDate) {
        LocalDate lastDayOfThisMonth;
        LocalDate day14thOfTheMonth;
        LocalDate lastDayOfTheFortNight;
        lastDayOfThisMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        day14thOfTheMonth = localDate.withDayOfMonth(14);

        lastDayOfTheFortNight = day14thOfTheMonth;

        if (localDate.isAfter(day14thOfTheMonth) && localDate.isBefore(lastDayOfThisMonth)) {
            lastDayOfTheFortNight = lastDayOfThisMonth.minusDays(1);
        }
        if (localDate.isEqual(lastDayOfThisMonth)) {
            lastDayOfTheFortNight = lastDayOfTheFortNight.plusMonths(1);
        }
        return lastDayOfTheFortNight;
    }

    public List<AttendanceRegister> getAttendanceRegistersByDates(LocalDate startDate, LocalDate endDate) {
        List<AttendanceRegister> attendanceRegisters = new ArrayList<>();
        for (AttendanceRegister attendanceRegister : model.attendanceRegisters) {
            if (attendanceRegister.getLocalDateTime().toLocalDate().isAfter(startDate.minusDays(1))
                    && attendanceRegister.getLocalDateTime().toLocalDate().isBefore(endDate.plusDays(1))) {
                attendanceRegisters.add(attendanceRegister);
            }
        }
        return attendanceRegisters;
    }

    public List<AttendanceRegister> getAttendanceRegistersByCollaboratorAndDates(Collaborator collaborator, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRegister> attendanceRegisters = new ArrayList<>();
        for (AttendanceRegister attendanceRegister : model.attendanceRegisters) {
            if (attendanceRegister.getCollaborator().equals(collaborator)
                    && attendanceRegister.getLocalDateTime().toLocalDate().isAfter(startDate.minusDays(1))
                    && attendanceRegister.getLocalDateTime().toLocalDate().isBefore(endDate.plusDays(1))) {
                attendanceRegisters.add(attendanceRegister);
            }
        }
        return attendanceRegisters;
    }

    public List<WorkSchedule> getWorkSchedulesWithBranchesByCollaboratorAndDate(Collaborator collaborator, LocalDate startDate, LocalDate endDate) {
        List<WorkSchedule> workSchedules = new ArrayList<>();
        for (WorkSchedule tempWorkSchedule : model.workSchedules) {
            if (tempWorkSchedule.getCollaborator().equals(collaborator)) {
                if (tempWorkSchedule.getWorkingDayType().isItNeedHours()) {
                    if (tempWorkSchedule.getLocalDate().isAfter(startDate.minusDays(1)) && tempWorkSchedule.getLocalDate().isBefore(endDate.plusDays(1))) {
                        workSchedules.add(tempWorkSchedule);
                    }
                }
            }
        }
        return workSchedules;
    }

    public Collaborator getCollaboratorFromUserName(String userName) {
        for (Collaborator collaborator : model.collaborators) {
            if (collaborator.getUser().getUserName().equals(userName)) {
                return collaborator;
            }
        }
        return null;
    }

    public List<WorkSchedule> getWorkSchedulesBetweenDates(LocalDate firstDate, LocalDate lastDate) {
        List<WorkSchedule> workSchedules = new ArrayList<>();
        for (WorkSchedule workSchedule : model.workSchedules) {
            if (workSchedule.getLocalDate().isAfter(firstDate.minusDays(1)) && workSchedule.getLocalDate().isBefore(lastDate.plusDays(1))) {
                workSchedules.add(workSchedule);
            }
        }
        return workSchedules;
    }

    public void updateWorkSchedulesToDBCopy(List<WorkSchedule> workschedulesToUpdate) {
        for (WorkSchedule workScheduleToUpdate : workschedulesToUpdate) {
            WorkSchedule retrievedWorkSchedule = getWorkScheduleByCollaboratorAndDate(workScheduleToUpdate.getCollaborator(), workScheduleToUpdate.getLocalDate());
            int index = model.workSchedules.indexOf(retrievedWorkSchedule);
            workScheduleToUpdate.setId(retrievedWorkSchedule.getId());
            model.workSchedules.set(index, workScheduleToUpdate);
        }
    }

    public int getMaxCollaboratorId() {
        int maxId = Integer.MIN_VALUE;
        for (Collaborator collaborator : model.collaborators) {
            maxId = Math.max(maxId, collaborator.getCollaboratorId());
        }
        return maxId;
    }

    public boolean isCollaboratorIdUsed(int collaboratorId) {
        for (Collaborator collaborator : model.collaborators) {
            if (collaboratorId == collaborator.getCollaboratorId()) {
                return true;
            }
        }
        return false;
    }


    public String formatToMoney(double positionWage) {
        return "$ " + String.format("%.2f", positionWage);
    }

    public void setMondayDate() {
        if (model.selectedLocalDate.getDayOfWeek().equals(DayOfWeek.MONDAY))
            model.mondayOfTheWeek = model.selectedLocalDate;
        else model.mondayOfTheWeek = model.selectedLocalDate.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
    }

    public void setLastDayOfMonth() {
        model.lastDayOfMonth = model.selectedLocalDate.with(TemporalAdjusters.lastDayOfMonth());
    }

    public boolean oneOfEquals(int a, int b, int expected) {
        return (a == expected) || (b == expected);
    }

    public <T> boolean oneOfEquals(T a, T b, T expected) {
        return a.equals(expected) || b.equals(expected);
    }


    public void loadAppointments() {
        model.appointments = AppointmentDAO.getInstance().getAllApointments();
    }

    public void loadAttendanceRegisters() {
        model.attendanceRegisters = AttendanceRegisterDAO.getInstance().getAttendanceRegisters();
    }

    public void loadBranches() {
        model.branches = BranchDAO.getInstance().getBranches();
        model.branchesAndNone = new ArrayList<>(model.branches);
        model.branchesAndNone.add(null);
        model.branchesNames = new ArrayList<>();
        for (Branch branch : model.branches) {
            model.branchesNames.add(branch.getName());
        }
        model.branchesNamesAndNone = new ArrayList<>(model.branchesNames);
        model.branchesNamesAndNone.add("None");
    }

    public void loadCollaborators() {
        model.collaborators = CollaboratorDAO.getInstance().getCollaborators();
        model.collaborators.sort(Comparator.comparing(collaborator -> collaborator.getUser().getUserName()));
        model.activeAndWorkerCollaborators = new ArrayList<>();
        for (Collaborator collaborator : model.collaborators) {
            if (!"Asesor".equals(collaborator.getJobPosition().getName())) {
                if (collaborator.getActive()) {
                    model.activeAndWorkerCollaborators.add(collaborator);
                }
            }
        }
        model.activeAndWorkerCollaboratorsAndNull = new ArrayList<>(model.activeAndWorkerCollaborators);
        model.activeAndWorkerCollaboratorsAndNull.add(null);


        model.activeAndWorkersUserNames = new ArrayList<>();
        for (Collaborator collaborator : model.activeAndWorkerCollaborators) {
            model.activeAndWorkersUserNames.add(collaborator.getUser().getUserName());
        }
        model.activeAndWorkersUserNamesAndNull = new ArrayList<>(model.activeAndWorkersUserNames);
        model.activeAndWorkersUserNamesAndNull.add(null);
        model.allUserNames = new ArrayList<>();
        for (Collaborator collaborator : model.collaborators) {
            model.allUserNames.add(collaborator.getUser().getUserName());
        }
        Collections.sort(model.allUserNames);
    }

    public void loadIncidents() {
        model.incidents = IncidentDAO.getInstance().getIncidents();
    }

    public void loadJobPositions() {
        model.jobPositions = JobPositionDAO.getInstance().getJobPositions();
    }

    public void loadOpeningHours() {
        model.openingHoursList = OpeningHoursDAO.getInstance().getOpeningHoursList();
    }

    public void loadUsers() {
        model.users = UserDAO.getInstance().getUsers();
    }

    public void loadWorkingDayTypes() {
        model.workingDayTypes = WorkingDayTypeDAO.getInstance().getWorkingDayTypes();
        model.workingDayTypesAbbr = new ArrayList<>();
        for (WorkingDayType workingDayType : model.workingDayTypes) {
            model.workingDayTypesAbbr.add(workingDayType.getAbbr());
        }
    }

    public void loadWorkSchedules() {
        model.workSchedules = WorkScheduleDAO.getInstance().getWorkSchedules();
    }

    public void setNullTemporaryVariables() {
        model.selectedCollaborator = null;
        model.appointmentToEdit = null;
        model.tempWorkSchedules = null;
        model.selectedLocalDate = null;
        model.mondayOfTheWeek = null;
        model.lastDayOfMonth = null;
        model.appointmentDate = null;
        model.appontimenTime = null;
        model.selectedBranch = null;
        model.incidentType = null;
        model.selectedView = null;
        model.hasErrors = false;
        model.hasWarnings = false;
        model.errorList = null;
        model.warningList = null;
    }


    // If the ending time is a LocalTime before of the startingTime, it changes to the next day
    public LocalDateTime getEndingDateTimeWithTimeAdjuster(LocalDateTime startingLDT, LocalTime endingTime) {
        LocalDateTime localDateTime = LocalDateTime.of(startingLDT.toLocalDate(), endingTime);
        LocalTime startingTime = startingLDT.toLocalTime();
        if (endingTime.isBefore(startingTime)) {
            localDateTime = localDateTime.plusDays(1);
        }
        return localDateTime;
    }

    public OpeningHoursDetailed getOpeningHoursDetailedByBranchAndDate(Branch branch, LocalDate localDate) {
        for (OpeningHoursDetailed openingHoursDetailed : model.tempOpeningHoursDetailedList) {
            if (branch.equals(openingHoursDetailed.getBranch()) && localDate.equals(openingHoursDetailed.getLocalDate())) {
                return openingHoursDetailed;
            }
        }
        return new OpeningHoursDetailed();
    }

    public List<LocalDateTime> getAvailableHoursByDateAndBranch(LocalDate localDate, Branch branch) {
        List<LocalDateTime> availableHours = new ArrayList<>();
        OpeningHoursDetailed tempOpeningHoursDetailed = getOpeningHoursDetailedByBranchAndDate(branch, localDate);

        LocalDateTime openingTime = tempOpeningHoursDetailed.getOpeningHour();
        LocalDateTime closingTime = tempOpeningHoursDetailed.getClosingHour();
        LocalDateTime localTime = openingTime;

        do {
            availableHours.add(localTime);
            localTime = localTime.plusHours(1);
            if (localTime.getMinute() != 0) {
                localTime = localTime.withMinute(0);
            }
        } while (localTime.isBefore(closingTime));
        return availableHours;
    }

    public List<LocalTime> getAvailableHoursByDates(LocalDate startDate, LocalDate endDate) {
        List<LocalTime> availableHours = new ArrayList<>();
        LocalTime startingHour = LocalTime.MAX;
        LocalTime closingHour = LocalTime.MIN;
        for(OpeningHours openingHours: model.openingHoursList){
            if (startDate.isAfter(openingHours.getStartDate().minusDays(1)) &&
                    (openingHours.getEndDate() == null || endDate.isBefore(openingHours.getEndDate().plusDays(1)))) {
                startingHour=startingHour.isBefore(openingHours.getOpeningHour())?startingHour:openingHours.getOpeningHour();
                closingHour=closingHour.isAfter(openingHours.getClosingHour())?closingHour:openingHours.getClosingHour();
            }
        }
        int numHours = closingHour.getHour()-startingHour.getHour();
        if(closingHour.getHour()<startingHour.getHour()){
            numHours = 24-startingHour.getHour()+closingHour.getHour();
        }
        for(int i=0; i<numHours; i++){
            availableHours.add(startingHour);
            if(startingHour.getHour()==23){
                startingHour = startingHour.withHour(0);
            } else{
                startingHour = startingHour.plusHours(1);
            }
        }
        return availableHours;
    }

    public LocalDate setMexicanDate(LocalDate now) {
        if (LocalTime.now().getHour() < 7) {
            now = now.minusDays(1);
        }
        return now;
    }

    public List<WorkSchedule> getWorkSchedulesWithBranchesBetweenDates(LocalDate startDate, LocalDate endDate) {
        List<WorkSchedule> workSchedules = new ArrayList<>();
        for (WorkSchedule tempWorkSchedule : model.workSchedules) {
            if (tempWorkSchedule.getWorkingDayType().isItNeedBranches()) {
                if (tempWorkSchedule.getLocalDate().isAfter(startDate.minusDays(1)) && tempWorkSchedule.getLocalDate().isBefore(endDate.plusDays(1))) {
                    workSchedules.add(tempWorkSchedule);
                }
            }
        }
        return workSchedules;
    }
}