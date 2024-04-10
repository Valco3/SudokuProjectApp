package implPackage;

import iPackage.Sudoku;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SudokuRegistry{
    public static void main(String[] args) {
        try{
            Sudoku s = new SudokuImplementation();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("SudokuGame", s);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

}
