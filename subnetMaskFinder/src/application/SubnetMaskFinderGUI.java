package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SubnetMaskFinderGUI extends Application {

    private TextField cidrTextField;
    private TextArea resultTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Subnet Mask Finder");
        primaryStage.setResizable(false);

        Label cidrLabel = new Label("CIDR:");
        cidrTextField = new TextField();
        cidrTextField.setPromptText("Enter CIDR");
        Button calculateButton = new Button("Calculate");
        calculateButton.setOnAction(e -> calculateSubnetMask());
        resultTextArea = new TextArea();
        resultTextArea.setEditable(false);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(8);
        grid.setHgap(10);

        grid.add(cidrLabel, 0, 0);
        grid.add(cidrTextField, 1, 0, 2, 1);
        grid.add(calculateButton, 1, 1);
        grid.add(resultTextArea, 0, 2, 3, 1);

        cidrTextField.setOnAction(e -> calculateSubnetMask());

        grid.setStyle("-fx-background-color: #f0f0f0;");
        calculateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        resultTextArea.setStyle("-fx-font-family: 'Courier New', monospace; -fx-background-color: #ffffff; -fx-border-color: #ccc; -fx-border-width: 1px;");
        cidrLabel.setStyle("-fx-text-fill: #333;");
        cidrTextField.setStyle("-fx-prompt-text-fill: #999; -fx-border-color: #ccc; -fx-border-width: 1px; -fx-padding: 5px;");

        Scene scene = new Scene(grid, 300, 150);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void calculateSubnetMask() {
        try {
            int cidr = Integer.parseInt(cidrTextField.getText());
            int blockCount = 0;
            int lastBlock = 0;
            int msbValue = 128;
            int cidrReminder = 0;
            String block = "255.";
            String subnetMask = "";

            if (cidr < 1 || cidr > 32) {
                resultTextArea.setText("Please enter a valid number from 1 to 32");
                return;
            }

            for (int i = 1; i <= 3; i++) {
                if (cidr > 7) {
                    cidr -= 8;
                    blockCount++;
                }
            }
            cidrReminder = cidr;

            cidr = Integer.parseInt(cidrTextField.getText());

            for (int i = 0; i < cidrReminder; i++) {
                lastBlock += msbValue;
                msbValue /= 2;
                if (cidr < 8) {
                    block = Integer.toString(lastBlock);
                    blockCount = 0;
                }
            }

            for (int i = 1; i <= blockCount || blockCount == 0; i++) {
                subnetMask += block;
                if (i == blockCount || !subnetMask.startsWith("255")) {
                    if (subnetMask.startsWith("255"))
                        subnetMask += lastBlock;
                    if (blockCount < 3 || !subnetMask.startsWith("255")) {
                        for (int j = 0; j < (3 - blockCount); j++) {
                            subnetMask += ".0";
                        }
                        blockCount = -1;
                    }
                }
            }

            resultTextArea.setText(subnetMask);

        } catch (NumberFormatException e) {
            resultTextArea.setText("Please enter a valid number");
        } catch (Exception e) {
            resultTextArea.setText("An error occurred: " + e.getMessage());
        }
    }
}
