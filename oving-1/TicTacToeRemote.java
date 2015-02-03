import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToeRemote extends Remote {
    public void newGame(TicTacToe ticTacToe) throws RemoteException;
    public void quitGame() throws RemoteException;

    public void setMark(int x, int y) throws RemoteException;

    public void setOpponent(TicTacToeRemote remote) throws RemoteException;
    public void setOpponentMark(char opponentMark) throws RemoteException;

}
