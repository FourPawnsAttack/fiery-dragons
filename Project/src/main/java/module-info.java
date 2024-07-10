module dragonix.fierydragons1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jdk.xml.dom;
    opens dragonix.fierydragons1 to javafx.fxml, com.google.gson;
    exports dragonix.fierydragons1;
    exports dragonix.fierydragons1.cards;
    opens dragonix.fierydragons1.cards to javafx.fxml, com.google.gson;
    exports dragonix.fierydragons1.handlers;
    opens dragonix.fierydragons1.handlers to javafx.fxml, com.google.gson;
    exports dragonix.fierydragons1.handlers.adapters;
    opens dragonix.fierydragons1.handlers.adapters to com.google.gson, javafx.fxml;
}