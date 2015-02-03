import java.rmi.*;
import java.rmi.server.*;


public class TicTacToeRemoteImpl extends UnicastRemoteObject implements TicTacToeRemote {
    private TicTacToe ticTacToe;
    private boolean myTurn;
    private char opponentMark;

    protected TicTacToeRemoteImpl(TicTacToe ticTacToe) throws RemoteException {
        this.ticTacToe = ticTacToe;
        setMyTurn(false);
        this.opponentMark = 'O';
    }

    @Override
    public void newGame(TicTacToe ticTacToe) throws RemoteException {
        ticTacToe.clearBoard();
    }

    @Override
    public void quitGame() throws RemoteException {
        ticTacToe.setServer(null);
        ticTacToe.clearBoard();
    }

    @Override
    public void setMark(int x, int y) throws RemoteException {

    }

    @Override
    public void setOpponent(TicTacToeRemote remote) throws RemoteException {
        ticTacToe.setServer(remote);
    }

    @Override
    public void setOpponentMark(char opponentMark) throws RemoteException {
        this.opponentMark = opponentMark;
    }

    @Override
    public boolean isMyTurn() throws RemoteException {
        return myTurn;
    }

    @Override
    public void setMyTurn(boolean myTurn) throws RemoteException {
        this.myTurn = myTurn;
    }
}
