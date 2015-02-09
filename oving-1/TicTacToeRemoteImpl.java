import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.logging.Logger;


public class TicTacToeRemoteImpl extends UnicastRemoteObject implements TicTacToeRemote {
    private final static Logger LOGGER = Logger.getLogger(TicTacToeRemote.class.getName());
    private TicTacToe ticTacToe;
    private boolean myTurn;
    private char opponentMark;
    private final int GRPPORT = 62000;


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
        ticTacToe.setMark(x, y, opponentMark);
        this.setMyTurn(true);
        //ticTacToe.repaint();          //TODO ?
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

    // When a client doesn't find a server, it tries to bind itself as the server.
    public void bind(String url) {
        LOGGER.info("Server started");

        try {
            LocateRegistry.createRegistry(GRPPORT);
        }
        catch (RemoteException e) {
            LOGGER.info("Java RMI registry already exists");
        }

        try {
            Naming.rebind(url, this);
            LOGGER.info("Binding self");
        }
        catch (Exception e) {
            LOGGER.warning("Bind unsuccessful. Probably gonna explode soon.");
        }
    }

}
