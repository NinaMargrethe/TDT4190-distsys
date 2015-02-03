import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TicTacToeRemote extends Remote{
    public int one()throws RemoteException;
}
