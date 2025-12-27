package me.jehn.javafxcourses;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static me.jehn.javafxcourses.Frame.switchScene;


public class HomeController implements Initializable {
    //DECLARATION
    @FXML
    private Button adminButton;
    @FXML
    private Button userButton;



    //METHODS
    @FXML
    void onAdminClick(ActionEvent event) throws IOException {
        switchScene("admin");
    }

    @FXML
    void onUserClick(ActionEvent event) throws IOException {
        switchScene("user");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
