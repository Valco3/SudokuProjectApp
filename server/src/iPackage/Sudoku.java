package iPackage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Sudoku extends Remote {
    int[][] generateSudokuBoard(int difficulty) throws RemoteException;
    int[][] presentSolution() throws RemoteException;
    void saveData(String userName, int difficulty, boolean solved, double Time) throws RemoteException;
}
