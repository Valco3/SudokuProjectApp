package client;

import gui.SudokuFXML;
import iPackage.Sudoku;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SudokuClient extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        Parent root
                = FXMLLoader.load(getClass().getResource("sudoku.fxml"));

        Scene scene = new Scene(root);
        // Set the Window title
        stage.setTitle("");
        stage.sizeToScene();
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
            launch(args);
    }


}