package com.example.assignment2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private TextField textField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private Label characterCountLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //making the searchButton Work
        searchButton.setOnAction(this::onSearchButtonClick);

        //Listener fot the listView to open CharacterInfo.fxml
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                openCharacterInfo(newValue);
            }
        });
    }

    @FXML
    private void onSearchButtonClick(ActionEvent event) {
        String userInput = textField.getText().trim();

        //Api link to characters + user Input
        String apiUrl = "https://rickandmortyapi.com/api/character/?name=" + userInput;

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode == 200) {
                String responseBody = response.body();
                JSONObject json = new JSONObject(responseBody);
                JSONArray results = json.getJSONArray("results");

                //looping for each "name" in JSON
                List<String> characterNames = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject character = results.getJSONObject(i);
                    String name = character.getString("name");
                    characterNames.add(name);
                }

                //Clear and adding characters
                listView.getItems().clear();
                listView.getItems().addAll(characterNames);

                //Counter
                int characterCount = characterNames.size();
                String characterCountText = Integer.toString(characterCount);
                characterCountLabel.setText("Character Found(s): " + characterCountText);
                System.out.println(characterCount);
            } else {
                //if != code 200
                System.out.println("Failed to get character information. Status code: " + statusCode);
                listView.getItems().clear();
                listView.getItems().addAll("nothing here");
                characterCountLabel.setText("Character found(s): " + "0");
            }
        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }
    }

    private void openCharacterInfo(String characterName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CharacterInfo.fxml"));
            Parent root = loader.load();
            CharacterInfoController characterInfoController = loader.getController();
            characterInfoController.setCharacterName(characterName);

            //Size of the Window
            Scene scene = new Scene(root, 700, 600);

            //New Scene
            Stage stage = new Stage();
            stage.setTitle("Hey Rick!");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
