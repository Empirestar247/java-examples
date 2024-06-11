module weatherapp.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.json;

    opens weatherapp.weatherapp to javafx.fxml;
    exports weatherapp.weatherapp;
}