package src.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import src.model.User;

public class DashboardController {
    @FXML private Label welcome, points;
    private User user;

    public void setUser(User user) {
        this.user = user;
        welcome.setText("Welcome, " + user.getName());
        points.setText("Points: " + user.getPoints());
    }
}
