package gui.application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import trackerbot.TrackerBot;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private TrackerBot trackerBot;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/userImage.png"));
    private Image trackerBotImage = new Image(this.getClass().getResourceAsStream("/images/botImage.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setTrackerBot(TrackerBot bot) {
        this.trackerBot = bot;
    }

    @FXML
    public void handleUserInput() {
        String input = userInput.getText();
        String response = trackerBot.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBotDialog(response, trackerBotImage)
        );
        userInput.clear();
    }

}
