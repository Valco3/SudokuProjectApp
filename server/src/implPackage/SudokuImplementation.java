package implPackage;

import iPackage.Sudoku;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Instant;
import java.util.*;

public class SudokuImplementation extends UnicastRemoteObject implements Sudoku {
    private int[][] sudokuBoard;
    private int[][] solvedBoard;
    private int difficulty;
    public SudokuImplementation(int difficulty) throws RemoteException{
        this.difficulty = difficulty;
        solvedBoard = new int[9][9];
        sudokuBoard = generateSudokuBoard(this.difficulty);
    }

    protected SudokuImplementation() throws RemoteException{
        difficulty = 1;
        solvedBoard = new int[9][9];
        sudokuBoard = generateSudokuBoard(this.difficulty);
    }

    @Override
    public int[][] generateSudokuBoard(int difficulty) throws RemoteException {
        this.difficulty = difficulty;
        int[][] boardToBeCreated = new int[9][9];
        helperFunc(boardToBeCreated);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                solvedBoard[i][j] = boardToBeCreated[i][j];
            }
        }
        removeDigits(boardToBeCreated);

        return boardToBeCreated;
    }

    public void removeDigits(int[][] inputBoard){
        Set<Integer> digitsToBeRemoved = new HashSet<>();
        Random random = new Random();

        while(digitsToBeRemoved.size() < difficulty * 20){
            int randomInt = random.nextInt(81) + 1;
            digitsToBeRemoved.add(randomInt);
            if(digitsToBeRemoved.size() == 40) break;
        }

        for(Integer i : digitsToBeRemoved){
            int row = (i - 1) / 9;
            int col = (i - 1) % 9;
            inputBoard[row][col] = 0;
        }
    }

    public void helperFunc(int[][] board){
        ArrayList<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Collections.shuffle(nums);
        for (int i = 0; i <= 8 ; i++) {
            board[0][i] = nums.get(i);
        }

        sudokuGenerator(board, 1, 0);
    }

    public boolean sudokuGenerator(int[][] inputBoard, int row, int col){
        if(row == 9){
            return true;
        }
        if(col == 9){
            return sudokuGenerator(inputBoard, row + 1, 0);
        }

        for (int i = 1; i <= 9; i++) {
            if(isValid(inputBoard, row, col, i)){
                inputBoard[row][col] = i;
                if(sudokuGenerator(inputBoard, row, col + 1)){
                    return true;
                }
                inputBoard[row][col] = 0;
            }
        }
        return false;
    }

    public boolean isValid(int[][] board, int row, int col, int num){
        for (int i = 0; i < 9; i++) {
            if(board[row][i] == num){
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if(board[i][col] == num){
                return false;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(board[(row - row % 3) + i][(col - col % 3) + j] == num){
                    return false;
                }
            }
        }

        return true;
    }

    public void printBoard(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(sudokuBoard[i][j]);
            }
            System.out.println();
        }
    }

    public void printSolvedBoard(){
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(solvedBoard[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    public int[][] presentSolution() throws RemoteException {
        return solvedBoard;
    }

    @Override
    public void saveData(String userName, int difficulty, boolean solved, double time) throws RemoteException {
        try {
            FileWriter writer = new FileWriter("data.txt", true);
            writer.write(String.format("Username: %s, Difficulty: %d, Solved: %b, Time: %.2f \n", userName, difficulty, solved, time));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
