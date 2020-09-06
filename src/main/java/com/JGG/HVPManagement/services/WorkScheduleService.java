package com.JGG.HVPManagement.services;

import com.JGG.HVPManagement.dao.WorkScheduleDAO;
import com.JGG.HVPManagement.entity.WorkSchedule;
import com.JGG.HVPManagement.model.Model;
import com.JGG.HVPManagement.model.Utilities;

import java.util.List;


// todo Delete this class
public class WorkScheduleService {
    private final static WorkScheduleService instance = new WorkScheduleService();
    private Model model;
    private boolean hasNewSaves;
    private WorkScheduleDAO workScheduleDAO;

    public WorkScheduleService() {
        model = Model.getInstance();
        workScheduleDAO = WorkScheduleDAO.getInstance();
    }

    public static WorkScheduleService getInstance() {
        return instance;
    }

    public void createOrReplaceRegisters(List<WorkSchedule> tempWorkSchedules) throws InterruptedException {
        // if is empty it has new saves
        setHasNewSaves(tempWorkSchedules);
        // Do the changes in the database
        workScheduleDAO.createOrReplaceRegisters(tempWorkSchedules);

        // if there are new saves wait for the thread
        if (hasNewSaves) {
            Thread thread = null;
            thread.join();

        // If not, just made the changes in the copy
        } else {
            for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
                WorkSchedule tempWorkSchedule1 = Utilities.getInstance().getWorkScheduleWithHoursByCollaboratorAndDate(tempWorkSchedule.getCollaborator(), tempWorkSchedule.getLocalDate());
                tempWorkSchedule.setId(tempWorkSchedule1.getId());
                int index = model.workSchedules.indexOf(tempWorkSchedule);
                model.workSchedules.set(index, tempWorkSchedule);
            }
        }
    }

    private void setHasNewSaves(List<WorkSchedule> tempWorkSchedules) {
        if (model.workSchedules.isEmpty()) {
            hasNewSaves = true;
        } else {
            // if not check if is found, in that case is has not
            for (WorkSchedule tempWorkSchedule : tempWorkSchedules) {
                for (WorkSchedule retrievedWorkSchedule : model.workSchedules) {
                    // check if is already registered
                    if ((retrievedWorkSchedule.getLocalDate().equals(tempWorkSchedule.getLocalDate()))
                            && (retrievedWorkSchedule.getCollaborator().getId() == (tempWorkSchedule.getCollaborator().getId()))) {
                        hasNewSaves = false;
                        break;
                    }
                }
                // if one save is found then, break both loops
                if (hasNewSaves) {
                    break;
                }
            }
        }
    }

}
