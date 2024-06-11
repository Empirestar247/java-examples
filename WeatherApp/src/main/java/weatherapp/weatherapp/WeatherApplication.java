package weatherapp.weatherapp;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.time.LocalTime;

public class WeatherApplication extends Application {
    private Text longitugeText;
    private Slider longitudeSlider;
    private Text latitudeText;
    private Slider latitudeSlider;
    private TextField cityTextField;
    private Text weatherInfo;
    private ComboBox<String> unitSelector;
    private ListView<String> historyView;
    private ImageView weatherIcon;
    private BackgroundImage backgroundImage;
    private VBox mainBox;
    private HBox root;
    private VBox forecastBox;
    private VBox historyViewBox;

    private String apiKey = "Your API key here";
    private static final DecimalFormat df = new DecimalFormat("0.00");



    @Override
    public void start(Stage primaryStage) {

        weatherInfo = new Text();
        weatherIcon = new ImageView();

        historyView = new ListView<>();
        historyView.setPrefWidth(400);

        forecastBox = new VBox();
        forecastBox.setPrefWidth(400);

        VBox coordinatesBox = startSliderBox();
        VBox cityBox = startCityBox();
        HBox unitSelectorBox = startUnitSelectorBox();
        historyViewBox = startHistoryViewBox();

        mainBox = new VBox(30,unitSelectorBox, coordinatesBox, cityBox, weatherInfo, weatherIcon);
        mainBox.setPrefWidth(400);
        root = new HBox(30, mainBox, historyViewBox, forecastBox);
        UpdateBackgroundImage();

        Scene scene = new Scene(root, 1200, 600);

        primaryStage.setTitle("Weather App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox startSliderBox(){
        latitudeSlider = new Slider(-90, 90, 0);
        latitudeSlider.setShowTickLabels(true);
        latitudeText = new Text();
        latitudeText.setFont(new Font(15));
        latitudeText.setText("Latitude: " + latitudeSlider.getValue());
        latitudeSlider.setOnMouseDragged(e -> updateLatitudeText());

        longitudeSlider = new Slider(-180, 180, 0);
        longitudeSlider.setShowTickLabels(true);
        longitugeText = new Text();
        longitugeText.setFont(new Font(15));
        longitugeText.setText("Longitude: " + longitudeSlider.getValue());
        longitudeSlider.setOnMouseDragged(e -> updateLongitudeText());

        Button fetchByCoordinatesButton = new Button("Get Weather by coordinates");
        fetchByCoordinatesButton.setOnAction(e -> fetchWeatherData(df.format(latitudeSlider.getValue()), df.format(longitudeSlider.getValue())));

        VBox latitudeBox = new VBox(latitudeText, latitudeSlider, longitugeText, longitudeSlider);
        return new VBox(latitudeBox, fetchByCoordinatesButton);

    }

    private VBox startCityBox(){
        Label label = new Label();
        label.setFont(new Font(15));
        label.setText("Enter city name");

        cityTextField = new TextField();

        Button fetchButton = new Button("Get Weather by city name");
        fetchButton.setOnAction(e -> fetchWeatherData(cityTextField.getText()));

        return new VBox(label, cityTextField, fetchButton);
    }

    private HBox startUnitSelectorBox(){
        unitSelector = new ComboBox<>();
        unitSelector.getItems().addAll("Metric", "Imperial");
        unitSelector.setValue("Metric");

        Label unitLabel = new Label("Select the unit: ");

        HBox box = new HBox(10, unitLabel, unitSelector);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private VBox startHistoryViewBox(){
        Text label = new Text();
        label.setFont(new Font(25));
        label.setText("Historic of weather fetch");

        historyView = new ListView<>();

        VBox box = new VBox(10, label, historyView);
        box.setAlignment(Pos.CENTER);
        box.setPrefWidth(400);
        return box;
    }

    private void updateLatitudeText(){
        try {
            latitudeText.setText("Latitude: "  + latitudeSlider.getValue());
        } catch (Exception e) {
            showAlert("Error", "Unexpected error occurred");
        }
    }

    private void updateLongitudeText(){
        try {
            longitugeText.setText("Longitude: "  + longitudeSlider.getValue());
        } catch (Exception e) {
            showAlert("Error", "Unexpected error occurred");
        }
    }

    private void fetchWeatherData(String latitude, String longitude) {
        try {
            String unit = unitSelector.getValue();
            String urlString = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&units=" + unit + "&appid=" + apiKey;

            String weatherResponse =  GetWeather(urlString);
            JSONObject json = new JSONObject(weatherResponse);
            parseWeatherData(json);


            String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&units=" + unit + "&appid=" + apiKey;
            String forecastResponse = GetWeather(urlForecast);
            JSONObject jsonForecast = new JSONObject(forecastResponse);
            updateForecast(jsonForecast);


        } catch (Exception e) {
            showAlert("Error", "Failed to fetch weather data");
            e.printStackTrace();
        }
    }

    private void fetchWeatherData(String cityName){
        try {
            String unit = unitSelector.getValue();
            String cityNameEncoded = URLEncoder.encode(cityName, StandardCharsets.UTF_8);
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + cityNameEncoded + "&units=" + unit + "&appid=" + apiKey;

            String weatherResponse = GetWeather(urlString);
            JSONObject json = new JSONObject(weatherResponse);
            parseWeatherData(json);


            String urlForecast = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityNameEncoded + "&units=" + unit + "&appid=" + apiKey;
            String forecastResponse = GetWeather(urlForecast);
            JSONObject jsonForecast = new JSONObject(forecastResponse);
            updateForecast(jsonForecast);

        } catch (Exception e) {
            showAlert("Error", "Failed to fetch weather data");
            e.printStackTrace();
        }
    }

    private String GetWeather(String urlString) throws IOException {
        URI uri = URI.create(urlString);
        URL url = uri.toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        Scanner sc = new Scanner(url.openStream());
        StringBuilder response = new StringBuilder();
        while (sc.hasNext()) {
            response.append(sc.nextLine());
        }
        sc.close();

        return response.toString();
    }

    private void parseWeatherData(JSONObject json) {

        String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
        String iconCode = json.getJSONArray("weather").getJSONObject(0).getString("icon");
        double temp = json.getJSONObject("main").getDouble("temp");
        int humidity = json.getJSONObject("main").getInt("humidity");
        double windSpeed = json.getJSONObject("wind").getDouble("speed");
        String cityName = json.getString("name");

        String tempUnit = unitSelector.getValue().equals("Metric") ? "Celsius" : "Fahrenheit";
        String windUnit = unitSelector.getValue().equals("Metric") ? "m/s" : "mph";

        weatherInfo.setText(String.format("Description: %s\nTemperature: %.2f %s\nHumidity: %d%%\nWind Speed: %.2f %s",
                description, temp, tempUnit, humidity, windSpeed, windUnit));
        weatherInfo.setFill(Color.SNOW);

        updateWeatherIcon(iconCode);

        if(cityName != null){
            updateHistory(cityName, temp, tempUnit);
        }else{
            updateHistory(temp, tempUnit);
        }

    }

    private void updateForecast(JSONObject json) throws FileNotFoundException {
        JSONArray weatherForecast = json.getJSONArray("list");

        String cityName = json.getJSONObject("city").getString("name");
        Text forecastText = new Text("Forecast for " + cityName);
        forecastText.setFont(new Font(25));

        forecastBox.getChildren().clear();
        forecastBox.setAlignment(Pos.CENTER);
        forecastBox.getChildren().add(forecastText);

        for(int i = 0; i < weatherForecast.length() && i < 5; i++) {
            JSONObject weatherEntry = weatherForecast.getJSONObject(i);
            String description = weatherEntry.getJSONArray("weather").getJSONObject(0).getString("description");
            String iconCode = weatherEntry.getJSONArray("weather").getJSONObject(0).getString("icon");
            double temp = weatherEntry.getJSONObject("main").getDouble("temp");
            int humidity = weatherEntry.getJSONObject("main").getInt("humidity");
            double windSpeed = weatherEntry.getJSONObject("wind").getDouble("speed");
            String date = weatherEntry.getString("dt_txt");


            String tempUnit = unitSelector.getValue().equals("Metric") ? "Celsius" : "Fahrenheit";
            String windUnit = unitSelector.getValue().equals("Metric") ? "m/s" : "mph";

            Text forecastInfo = new Text(String.format("Date: %s\nDescription: %s\nTemperature: %.2f %s\nHumidity: %d%%\nWind Speed: %.2f %s",
                    date, description, temp, tempUnit, humidity, windSpeed, windUnit));

            FileInputStream input = new FileInputStream("./assets/WeatherIcons/" + iconCode + ".png");
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            HBox box = new HBox(imageView, forecastInfo);
            forecastBox.getChildren().add(box);
        }

    }

    private void UpdateBackgroundImage() {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        hour = 13;
        String backgroundImagePath;
        if (hour >= 5 && hour < 12) {
            backgroundImagePath = "./assets/Backgrounds/morning.jpg";
        } else if (hour >= 12 && hour < 17) {
            backgroundImagePath = "./assets/Backgrounds/afternoon.jpg";
        } else if (hour >= 17 && hour < 20) {
            backgroundImagePath = "./assets/Backgrounds/evening.jpg";
        } else {
            backgroundImagePath = "./assets/Backgrounds/night.jpg";
        }

        try {
            FileInputStream input = new FileInputStream(backgroundImagePath);
            Image backgroundImage = new Image(input);
            BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(100, 100, true, true, true, true));
            root.setBackground(new Background(background));
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load background image");
        }
    }

    private void updateWeatherIcon(String iconCode) {
        try {
            FileInputStream input = new FileInputStream("./assets/WeatherIcons/" + iconCode + ".png");
            Image image = new Image(input);
            weatherIcon.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load weather icon");
        }
    }

    private void updateHistory(double temp, String tempUnit) {
        String historyEntry = String.format("%.2f° %s at %s", temp, tempUnit, java.time.LocalDateTime.now());
        historyView.getItems().add(historyEntry);
    }

    private void updateHistory(String cityName, double temp, String tempUnit) {
        String historyEntry = String.format("%s %.2f° %s at %s",cityName, temp, tempUnit, java.time.LocalDateTime.now());
        historyView.getItems().add(historyEntry);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
