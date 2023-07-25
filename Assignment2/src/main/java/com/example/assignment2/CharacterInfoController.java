package com.example.assignment2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;

import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

public class CharacterInfoController {

    @FXML
    private Label nameLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private Label speciesLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label originLabel;

    @FXML
    private Button goBackButton;

    @FXML
    private ImageView characterImageView;

    @FXML
    private void goBack(ActionEvent event) {
        //button goback
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        // Close the window
        stage.close();
    }
    public void setCharacterName(String characterName) {
        nameLabel.setText("Name: " + characterName);
        searchCharacterInfo(characterName);
    }

    private void searchCharacterInfo(String characterName) {
        try {
            String encodedName = URLEncoder.encode(characterName, StandardCharsets.UTF_8.toString());
            String apiUrl = "https://rickandmortyapi.com/api/character/?name=" + encodedName;

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

                    if (results.length() > 0) {
                        JSONObject character = results.getJSONObject(0);

                        String status = character.getString("status");
                        String species = character.getString("species");
                        String gender = character.getString("gender");

                        JSONObject originObject = character.getJSONObject("origin");
                        String origin = originObject.getString("name");

                        String imageUrl = character.getString("image");

                        Image image = new Image(imageUrl);

                        characterImageView.setImage(image);
                        statusLabel.setText("Status: " + status);
                        speciesLabel.setText("Species: " + species);
                        genderLabel.setText("Gender: " + gender);
                        originLabel.setText("Origin: " + origin);
                    } else {
                        statusLabel.setText("Status: Unknown");
                        speciesLabel.setText("Species: Unknown");
                        genderLabel.setText("Gender: Unknown");
                        originLabel.setText("Origin: Unknown");
                    }
                } else {
                    // if code!= 200
                    System.out.println("Failed to get character information. Status code: " + statusCode);
                }
            } catch (IOException | InterruptedException e) {
                //catch blocks
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
