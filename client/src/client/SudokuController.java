package client;

import gui.SudokuAppController;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ResourceBundle;

import iPackage.Sudoku;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class SudokuController {
    private Sudoku server;
    private int[][] inputBoard;
    private String name;
    private int difficulty;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private SudokuAppController pnlUserComponent;

    @FXML
    void initialize() {
        assert pnlUserComponent != null : "fx:id=\"pnlUserComponent\" was not injected: check your FXML file 'sudoku.fxml'.";
        initializeRMI();
        initializeUser();
        startNewGame();
    }

    public void initializeUser(){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("User Login");
        textInputDialog.setHeaderText("Input username");
        textInputDialog.showAndWait();
        name = textInputDialog.getResult();
    }

    public void startNewGame(){
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Difficulty setting");
        textInputDialog.setHeaderText("Input desired difficulty (1, 2 or 3)");
        textInputDialog.showAndWait();
        int result = Integer.parseInt(textInputDialog.getResult());
        while(result < 1 || result > 3){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Invalid difficulty setting");
            alert.showAndWait();
            textInputDialog.showAndWait();
            result = Integer.parseInt(textInputDialog.getResult());
        }
        difficulty = result;
        try {
            loadData();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        pnlUserComponent.loadContent(inputBoard);

        long startTime = System.currentTimeMillis();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Whenever you are done with the attempt at solving the puzzle you can proceed");
        alert.setHeaderText(name);
        alert.show();
        alert.setOnCloseRequest(dialogEvent -> {
            if(pnlUserComponent.getIsSolving()){
                long endTime = System.currentTimeMillis();
                try {
                    server.saveData(name, difficulty, false, (endTime - startTime) / 1000.0);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Alert alertInProgress = new Alert(Alert.AlertType.CONFIRMATION, "You didn't finish solving the puzzle." +
                        " Would you like to see a solution to it?", ButtonType.YES, ButtonType.NO);
                alertInProgress.showAndWait();
                if(alertInProgress.getResult() == ButtonType.YES){
                    try {
                        pnlUserComponent.showSolution(server.presentSolution());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else{
                try {
                    server.saveData(name, difficulty, true, (pnlUserComponent.getSubmissionTime() - startTime) / 1000.0);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                Alert alertFinished = new Alert(Alert.AlertType.INFORMATION, "Congratulations! You solved the puzzle!");
                alertFinished.showAndWait();
            }

            Alert newGameInitializer = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to start a new game?", ButtonType.YES, ButtonType.NO);
            newGameInitializer.showAndWait();
            if(newGameInitializer.getResult() == ButtonType.YES){
                startNewGame();
            }else{
                Platform.exit();
            }
        });
    }

    protected void initializeRMI(){
        try{
            Registry registry = LocateRegistry.getRegistry(1099);
            server = (Sudoku) registry.lookup("SudokuGame");
            System.out.println("Connected to server");
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() throws RemoteException {
        inputBoard = server.generateSudokuBoard(difficulty);
    }

}
