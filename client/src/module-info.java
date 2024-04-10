module client {
    requires java.rmi;
    requires server;
    requires javafx.fxml;
    requires javafx.controls;
    requires SudokuFXMLApp;
    opens client to javafx.fxml;
    exports client to javafx.graphics;
}