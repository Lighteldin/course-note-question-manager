package me.jehn.javafxcourses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

import static me.jehn.javafxcourses.Frame.switchScene;

public class UserController {
    //DECLARATION
    @FXML
    private Button backButton;

    //METHODS
    @FXML
    void onBackClick(ActionEvent event) throws IOException {
        switchScene("home");
    }
}
