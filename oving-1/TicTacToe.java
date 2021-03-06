import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Tic Tac Toe application.
 * Currently this is a stand-alone application where
 * players take alternating turns using the same computer.
 * <p/>
 * The task is to transform it to a networking application using RMI.
 */
public class TicTacToe extends JFrame implements ListSelectionListener {
    private static final int BOARD_SIZE = 15;
    private final BoardModel boardModel;
    private final JTable board;
    private final JLabel statusLabel = new JLabel();
    private final char playerMarks[] = {'X', 'O'};
    private int currentPlayer = 'X'; // Player to set the next mark.
    private TicTacToeRemoteImpl client = null;
    private TicTacToeRemoteImpl server = null;
    private final static Logger LOGGER = Logger.getLogger(TicTacToe.class.getName());
    private final String ADDR = "localhost:62000";
    private char myMark;


    public static void main(String args[]) {
        TicTacToe mine = new TicTacToe();
        mine.serverHandling();
        //System.setSecurityManager( new RMISecurityManager() );
        TicTacToe opponentClient = new TicTacToe();
        opponentClient.serverHandling();
    }

    public TicTacToe() {
        super("TDT4190: Tic Tac Toe");

        boardModel = new BoardModel(BOARD_SIZE);
        board = new JTable(boardModel);
        board.setFont(board.getFont().deriveFont(25.0f));
        board.setRowHeight(30);
        board.setCellSelectionEnabled(true);

        for (int i = 0; i < board.getColumnCount(); i++)
            board.getColumnModel().getColumn(i).setPreferredWidth(30);

        board.setGridColor(Color.BLACK);
        board.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer dtcl = new DefaultTableCellRenderer();
        dtcl.setHorizontalAlignment(SwingConstants.CENTER);

        board.setDefaultRenderer(Object.class, dtcl);
        board.getSelectionModel().addListSelectionListener(this);
        board.getColumnModel().getSelectionModel().addListSelectionListener(this);

        statusLabel.setPreferredSize(new Dimension(statusLabel.getPreferredSize().width, 40));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(board, BorderLayout.CENTER);
        contentPane.add(statusLabel, BorderLayout.SOUTH);
        pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        int centerX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - getSize().width) / 2;
        int centerY = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - getSize().height) / 2;
        setLocation(centerX, centerY);
        setVisible(true);
    }

    void setStatusMessage(String status) {
        statusLabel.setText(status);
    }

    /**
     * This has to be modified. Currently the application is stand-alone so
     * both players have to use the same computer.
     * <p/>
     * When completed, marks from the first player originates from a ListSelectionEvent
     * and is then sent to the second player. And marks from the second player is received
     * and added to the board of the first player.
     */
    public void valueChanged(ListSelectionEvent e) {

/*        try {
            if(!server.isMyTurn()){
                board.setFocusable(false);
                board.setCellSelectionEnabled(false);
            }
            else {
                board.setFocusable(true);
                board.setCellSelectionEnabled(true);
            }
        } catch (RemoteException re) {
            LOGGER.log(Level.SEVERE, re.toString());
        }*/
/*
        if (e.getValueIsAdjusting())
            return;
        int x = board.getSelectedColumn();
        int y = board.getSelectedRow();
        if (x == -1 || y == -1 || !boardModel.isEmpty(x, y))
            return;
        if (boardModel.setCell(x, y, playerMarks[currentPlayer]))
            setStatusMessage("Player " + playerMarks[currentPlayer] + " won!");

        currentPlayer = 1 - currentPlayer; // The next turn is by the other player.*/

        try {
            if (server != null && client.isMyTurn())
            {
                setMark(e.getFirstIndex(), e.getLastIndex(), myMark);
                try {
                    server.setMark(e.getFirstIndex(), e.getLastIndex());
                    client.setMyTurn(false);
                }
                catch (RemoteException rex) {
                    rex.printStackTrace();
                }
            }
        } catch (RemoteException rex) {
            rex.printStackTrace();
        }
    }

    public void clearBoard() {
        for (int i = 0; i < board.getColumnCount(); i++) {
            for (int j = 0; j < board.getRowCount(); i++) {
                board.setValueAt(" ", i, j);
            }
        }
        repaint();
    }

    public void setServer(TicTacToeRemote remote) {
        this.server = (TicTacToeRemoteImpl) remote;
    }

    public void setMark(int x, int y, char mark) {
        board.setValueAt(mark, x, y);
    }

    public void serverHandling() {
        String url = "rmi://" + ADDR + "/TicTacToeRemoteImpl";

        // Looking for server
        try {
            server = (TicTacToeRemoteImpl) Naming.lookup(url);
        }
        catch (NotBoundException nbe) {
            LOGGER.log(Level.SEVERE, "No server registered");
        }
        catch (ConnectException ce) {
            LOGGER.log(Level.SEVERE, "No RMI registry found at " + ADDR);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exploded.");
        }

        // Initializing self as client
        try {
            client = new TicTacToeRemoteImpl(this);
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exploded.");
        }

        // Initializing self as server if server not found.
        if (server == null) {
            LOGGER.log(Level.INFO, "Server not found, initializing self.");
            try {
                client.setMyTurn(true);
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            }
            myMark = 'X';
            try {
                Naming.rebind(url, client);
            } catch (RemoteException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        // Connect to server
        else {
            LOGGER.log(Level.INFO, "Lookup.");
            myMark = 'O';

            try {
                client.setMyTurn(true);
                server.setOpponentMark(myMark);
                server.setOpponent(client);
            } catch (RemoteException e) {
                LOGGER.log(Level.SEVERE, e.toString());
            }
        }
    }
}
