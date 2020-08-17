
package com.JGG.HVPManagement.controller;

import com.JGG.HVPManagement.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddImageController implements Initializable {
    public ImageView imvImage;
    public Button btnSave;
    private User user;
    private List<File> files;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void initData(User user){
        this.user=user;
    }


    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
        dragEvent.consume();
    }

    public void handleOnDragDropped(DragEvent dragEvent) throws FileNotFoundException {
        files = dragEvent.getDragboard().getFiles();
        Image image = new Image(new FileInputStream(files.get(0)));
        imvImage.setImage(image);
    }

    public void save(ActionEvent event) {
        File copy = new File("res\\" + user.getUser() + ".png");
        List<Integer> bytes = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(files.get(0));
            int stream;
            while ((stream = fileInputStream.read()) != -1) {
                bytes.add(stream);
            }
            fileInputStream.close();




            FileOutputStream fileOutputStream = new FileOutputStream(copy, true);
            for (Integer b : bytes) {
                fileOutputStream.write(b);
            }
            System.out.println(copy.length());
            Stage thisStage = (Stage) btnSave.getScene().getWindow();
            thisStage.close();


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
