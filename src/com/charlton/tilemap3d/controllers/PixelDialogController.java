package com.charlton.tilemap3d.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;

public class PixelDialogController implements EventHandler<ActionEvent> {

    private PixelDialogHandler handler;

    public void setCloseHandler(PixelDialogHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(ActionEvent event) {
        if(event.getSource() == submitBtn){
            handler.submit(Integer.parseInt(tPixelWidth.getText()), Integer.parseInt(tPixelHeight.getText()));
        }else if(event.getSource() == cancelBtn){
            handler.close();
        }
    }

    public interface PixelDialogHandler {
        void close();
        void submit(int width, int height);
    }

    public DialogPane dialogPane;
    public TextField tPixelWidth;
    public TextField tPixelHeight;
    public Button submitBtn;
    public Button cancelBtn;

    @FXML
    public void initialize(){
        submitBtn.setOnAction(this);
        cancelBtn.setOnAction(this);
    }


}
