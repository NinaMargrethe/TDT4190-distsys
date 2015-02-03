import java.rmi.*;
import java.rmi.server.*;


public class TicTacToeRemoteImpl extends UnicastRemoteObject implements TicTacToeRemote {
    @Override
    public void newGame() throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void quitGame() throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setMark(int x, int y) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOpponent(TicTacToeRemote remote) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setOpponentMark() throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
