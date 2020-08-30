package com.JGG.HVPManagement.controller.workSchedule;

import com.JGG.HVPManagement.entity.Branch;
import com.JGG.HVPManagement.entity.JobPosition;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.HoursByDateByBranch;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HoursByDateAndBranchController implements Initializable {
    public BorderPane rootPane;
    public TableView<HoursByDateByBranch> tblTable;
    public TableColumn<HoursByDateByBranch, String> colBranch;
    public TableColumn<HoursByDateByBranch, String> colDate;
    public TableColumn<HoursByDateByBranch, Integer> colHours;
    private Model model;
    private Utilities utilities;
    private List<HoursByDateByBranch> hoursByDateByBranches;


    // todo create an abstract class or an interface for the similar classes
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        utilities = Utilities.getInstance();
        setCellValueFactories();
        countMedicalHoursByDateAndBranch();
        loadTable();
    }


    private void setCellValueFactories() {
        this.colBranch.setCellValueFactory(new PropertyValueFactory<>("branch"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("dayOfWeek"));
        this.colHours.setCellValueFactory(new PropertyValueFactory<>("hours"));
    }

    private void loadTable() {
        hoursByDateByBranches.sort(Comparator.comparing(HoursByDateByBranch::getHours).reversed());
        ObservableList<HoursByDateByBranch> hoursByDateByBranchObservableList = FXCollections.observableList(hoursByDateByBranches);
        this.tblTable.setItems(hoursByDateByBranchObservableList);
    }

    public void countMedicalHoursByDateAndBranch() {
        List<JobPosition> jobPositions = new ArrayList<>();
        jobPositions.add(utilities.getJobPositionByName("Director médico"));
        jobPositions.add(utilities.getJobPositionByName("Veterinario A"));
        jobPositions.add(utilities.getJobPositionByName("Veterinario B"));
        jobPositions.add(utilities.getJobPositionByName("Asistente A"));
        jobPositions.add(utilities.getJobPositionByName("Asistente B"));
        jobPositions.add(utilities.getJobPositionByName("Pasante médico"));

        // made a list branch, day, hours
        hoursByDateByBranches = new ArrayList<>();
        for (WorkSchedule workSchedule : model.tempWorkSchedules) {
            if (workSchedule.getWorkingDayType().getItNeedBranches()) {
                for (Branch branch : model.branches) {
                    if (workSchedule.getBranch().equals(branch)) {
                        if (jobPositions.contains(workSchedule.getCollaborator().getJobPosition())) {
                            int minutesWorked = (int) ChronoUnit.MINUTES.between(workSchedule.getStartingTime(), workSchedule.getEndingTime());
                            HoursByDateByBranch hoursByDateByBranch = getHoursByDateByBranch(workSchedule.getLocalDate(), workSchedule.getBranch());
                            hoursByDateByBranch.setMinuteSum(hoursByDateByBranch.getMinuteSum() + minutesWorked);
                        }
                    }

                }
            }
        }
        for (HoursByDateByBranch hoursByDateByBranch : hoursByDateByBranches) {
            System.out.println(hoursByDateByBranch.getBranch());
            System.out.println(hoursByDateByBranch.getLocalDate());
            System.out.println(hoursByDateByBranch.getMinuteSum() / 60);
        }
    }

    private HoursByDateByBranch getHoursByDateByBranch(LocalDate localDate, Branch branch) {
        for (HoursByDateByBranch hoursByDateByBranch : hoursByDateByBranches) {
            if (hoursByDateByBranch.getBranch().equals(branch) && hoursByDateByBranch.getLocalDate().equals(localDate)) {
                return hoursByDateByBranch;
            }
        }
        HoursByDateByBranch hoursByDateByBranch = new HoursByDateByBranch(branch, localDate, 0);
        hoursByDateByBranches.add(hoursByDateByBranch);
        return hoursByDateByBranch;

    }


}
