import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;

public class Cliente extends JFrame {

    private JTextArea textArea;
    private JButton sendButton;

    private JScrollPane textScrollPane;
    private JTextField sendTextField;
    private JLabel panel;
    private JLabel jogartoalha;
    private JLabel empate;

    private ImageIcon board;
    private ImageIcon black;
    private ImageIcon green;
    private ImageIcon red;
    private JButton[] buttonsPieces = new JButton[7];

    private int turnGame;
    private int enemyP;
    private Integer[] myPieces;
    private Integer[] enemyPieces;
    private Integer[] pieces;
    private boolean piecesEnabled;
    private int placeUsed;
    private int enemyPlace;

    Integer[] win1 = {0, 1, 4};
    Integer[] win2 = {0, 2, 5};
    Integer[] win3 = {4, 5, 6};
    Integer[] win4 = {1, 2, 3};
    Integer[] win5 = {0, 3, 6};
    private List<Integer[]> winCondition = new ArrayList<Integer[]>();

    private Boolean draw ;
    private int playersDraw;

    private boolean win;
    private ChatCliente chat;
    private ClienteSocket client;

    public Cliente() {

        client = new ClienteSocket();
        chat = new ChatCliente();

        this.placeUsed = 0;
        this.enemyPlace = 0;
        this.win = false;
        this.draw = false;
        this.playersDraw = 0 ;
        this.pieces = new Integer[7];
        this.myPieces = new Integer[3];
        this.enemyPieces = new Integer[3];

        this.winCondition.add(win1);
        this.winCondition.add(win2);
        this.winCondition.add(win3);
        this.winCondition.add(win4);
        this.winCondition.add(win5);

        this.turnGame = 0;
    }

    private void initInterface() {
        this.panel = new JLabel();
        this.textArea = new JTextArea();
        this.sendButton = new JButton();
        this.textScrollPane = new JScrollPane();
        this.sendTextField = new JTextField();
        this.jogartoalha = new JLabel();
        this.empate = new JLabel();
        this.buttonsPieces[0] = new JButton();
        this.buttonsPieces[1] = new JButton();
        this.buttonsPieces[2] = new JButton();
        this.buttonsPieces[3] = new JButton();
        this.buttonsPieces[4] = new JButton();
        this.buttonsPieces[5] = new JButton();
        this.buttonsPieces[6] = new JButton();
        this.board = new ImageIcon("resources/tabu.png");
        this.black = new ImageIcon("resources/BlackBall.png");
        this.green = new ImageIcon("resources/Greenball.png");
        this.red = new ImageIcon("resources/RedBall.png");

        this.setTitle("Tsoro Yematatu  Jogador: " + client.getPlayerName());
        this.setSize(600, 700);
        this.setBackground(Color.WHITE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        Border green;
        green = BorderFactory.createLineBorder(Color.GREEN);

        panel.setIcon(board);
        this.setContentPane(panel);


        empate.setText(" NÃO AGUENTA MAIS !naoaguentomais");
        empate.setFont(new Font (Font.SERIF, Font.BOLD, 15));
        empate.setBounds(20,420,500,80);
        this.add(empate);

        jogartoalha.setText(" PARA JOGAR A TOALHA !jogartoalha");
        jogartoalha.setFont(new Font (Font.SERIF, Font.BOLD, 15));
        jogartoalha.setBounds(20,440,500,80);
        this.add(jogartoalha);

        sendTextField.setBounds(20,610,260,20);
        sendTextField.setBorder(green);
        this.add(sendTextField);

        sendButton.setText("Enviar");
        sendButton.setBackground(Color.GREEN);
        sendButton.setBounds(290,610,80,20);
        this.add(sendButton);

        textArea.setEditable(false);
        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textScrollPane.setViewportView(textArea);
        textScrollPane.setBorder(green);
        textScrollPane.setBounds(20, 500, 500, 100);
        this.add(textScrollPane);

        buttonsPieces[0].setOpaque(false);
        buttonsPieces[0].setContentAreaFilled(false);
        buttonsPieces[0].setBorderPainted(false);
        buttonsPieces[0].setFocusable(false);
        buttonsPieces[0].setIcon(black);
        buttonsPieces[0].setDisabledIcon(black);
        buttonsPieces[0].setForeground(Color.WHITE);
        buttonsPieces[0].setActionCommand("0");
        buttonsPieces[0].setBounds(258, 10, 50, 50);
        this.add(buttonsPieces[0]);

        buttonsPieces[1].setOpaque(false);
        buttonsPieces[1].setContentAreaFilled(false);
        buttonsPieces[1].setBorderPainted(false);
        buttonsPieces[1].setFocusable(false);
        buttonsPieces[1].setIcon(black);
        buttonsPieces[1].setDisabledIcon(black);
        buttonsPieces[1].setForeground(Color.BLACK);
        buttonsPieces[1].setActionCommand("1");
        buttonsPieces[1].setBounds(135, 210, 50, 50);
        this.add(buttonsPieces[1]);

        buttonsPieces[2].setOpaque(false);
        buttonsPieces[2].setContentAreaFilled(false);
        buttonsPieces[2].setBorderPainted(false);
        buttonsPieces[2].setFocusable(false);
        buttonsPieces[2].setIcon(black);
        buttonsPieces[2].setDisabledIcon(black);
        buttonsPieces[2].setForeground(Color.BLACK);
        buttonsPieces[2].setActionCommand("2");
        buttonsPieces[2].setBounds(258, 210, 50, 50);
        this.add(buttonsPieces[2]);

        buttonsPieces[3].setOpaque(false);
        buttonsPieces[3].setContentAreaFilled(false);
        buttonsPieces[3].setBorderPainted(false);
        buttonsPieces[3].setFocusable(false);
        buttonsPieces[3].setIcon(black);
        buttonsPieces[3].setDisabledIcon(black);
        buttonsPieces[3].setForeground(Color.WHITE);
        buttonsPieces[3].setActionCommand("3");
        buttonsPieces[3].setBounds(395, 210, 50, 50);
        this.add(buttonsPieces[3]);

        buttonsPieces[4].setOpaque(false);
        buttonsPieces[4].setContentAreaFilled(false);
        buttonsPieces[4].setBorderPainted(false);
        buttonsPieces[4].setFocusable(false);
        buttonsPieces[4].setIcon(black);
        buttonsPieces[4].setDisabledIcon(black);
        buttonsPieces[4].setForeground(Color.WHITE);
        buttonsPieces[4].setActionCommand("4");
        buttonsPieces[4].setBounds(46, 355, 50, 50);
        this.add(buttonsPieces[4]);

        buttonsPieces[5].setOpaque(false);
        buttonsPieces[5].setContentAreaFilled(false);
        buttonsPieces[5].setBorderPainted(false);
        buttonsPieces[5].setFocusable(false);
        buttonsPieces[5].setIcon(black);
        buttonsPieces[5].setDisabledIcon(black);
        buttonsPieces[5].setForeground(Color.WHITE);
        buttonsPieces[5].setActionCommand("5");
        buttonsPieces[5].setBounds(262, 355, 50, 50);
        this.add(buttonsPieces[5]);

        buttonsPieces[6].setOpaque(false);
        buttonsPieces[6].setContentAreaFilled(false);
        buttonsPieces[6].setBorderPainted(false);
        buttonsPieces[6].setFocusable(false);
        buttonsPieces[6].setIcon(black);
        buttonsPieces[6].setDisabledIcon(black);
        buttonsPieces[6].setForeground(Color.WHITE);
        buttonsPieces[6].setActionCommand("6");
        buttonsPieces[6].setBounds(490, 355, 50, 50);
        this.add(buttonsPieces[6]);

        this.setVisible(true);
    }

    private void initButtons() {
        ActionListener action = new ActionListener()
        {
            public void actionPerformed(ActionEvent event) {
                JButton b = (JButton) event.getSource();
                int pieceN = Integer.parseInt(b.getActionCommand());
                if (placeUsed < 3) {
                    myPieces[placeUsed] = pieceN;
                    placeUsed++;
                }
                turnGame++;
                piecesEnabled = false;
                if (turnGame > 3) {
                    verifyPlay(pieceN);
                }
                else {
                    changeButtonPiecesInicial();
                    client.sendPieceNum(pieceN);
                    client.sendNewArrayFlag(false);
                    verifyWin();
                }
                Thread buton = new Thread(new Runnable() {
                    public void run() {
                        attTurn();
                    }
                });
                buton.start();
            }
        };
        buttonsPieces[0].addActionListener(action);
        buttonsPieces[1].addActionListener(action);
        buttonsPieces[2].addActionListener(action);
        buttonsPieces[3].addActionListener(action);
        buttonsPieces[4].addActionListener(action);
        buttonsPieces[5].addActionListener(action);
        buttonsPieces[6].addActionListener(action);
    }
    private void initThreads() {
        Thread cT = new Thread(new Runnable() {
            public void run() {
                attChat();
            }
        });
        cT.start();
        if (client.getPlayerName() == 1) {
            textArea.append("\n##### JOGADOR 1 PODE JOGAR #####");
            enemyP = 2;
            piecesEnabled = true;
        }
        else {
            textArea.append("\n##### JOGADOR 2 AGUARDE SEU TURNO #####");
            enemyP = 1;
            piecesEnabled = false;

            Thread t = new Thread(new Runnable() {
                public void run() {
                    attTurn();
                }
            });
            t.start();
        }
        changeButtonPiecesInicial();
    }
    private void initChat() {
        ActionListener action = new ActionListener()
        {
            public void actionPerformed(ActionEvent event) {
                sendMsgChat();
            }
        };

        KeyListener key = new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMsgChat();
                }
            }
        };
        sendButton.addActionListener(action);
        sendTextField.addKeyListener(key);
    }
    private void attTurn() {
        int n = client.receivePieceNum();
        boolean newArray = client.receiveNewArrayFlag();
        if (n != -1) {
            textArea.append("\n##### JOGADOR " + enemyP + " COLOCOU A PEÇA NA POSIÇÃO " + n + " AGORA É SUA VEZ #####");
            if (enemyPlace < 3) {
                enemyPieces[enemyPlace] = n;
                enemyPlace++;
            }
        }
        if (turnGame <= 3 && newArray == false) {
            for (int i = 0; i < myPieces.length; i++) {
                if (myPieces[i] != null) {
                    pieces[myPieces[i]] = 1;
                }
                if (enemyPieces[i] != null) {
                    pieces[enemyPieces[i]] = 2;
                }
            }
        }
        if (newArray == true) {
            Integer[] updatedPiecesArray = client.receiveNewPieces();

            enemyPieces[0] = updatedPiecesArray[0];
            enemyPieces[1] = updatedPiecesArray[1];
            enemyPieces[2] = updatedPiecesArray[2];
            attPieces();
        }
        piecesEnabled = true;
        if (turnGame >= 3 && enemyPlace == 3) {
            changeButtonPieces();
        }
        else {
            changeButtonPiecesInicial();
        }
        if (!win) {
            verifyWin();
        }
    }

    private void changeButtonPiecesInicial() {
        for (int i = 0; i < buttonsPieces.length; i++) {
            buttonsPieces[i].setEnabled(piecesEnabled);
            if (pieces[i] != null) {
                buttonsPieces[i].setEnabled(false);
            }
        }
        setPiecesColor();
    }
    private void changeButtonPieces() {
        Integer[] changeButton = new Integer[2];
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == null) {
                changeButton[0] = i;
            }
        }
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] != null) {
                buttonsPieces[i].setEnabled(false);
            }
            if (pieces[i] == null) {
                buttonsPieces[i].setEnabled(false);
                buttonsPieces[i].setDisabledIcon(black);
            }
            if (pieces[i] != null && pieces[i] == 1) {
                changeButton[1] = i;
                for (Integer[] segment : winCondition) {
                    if (Arrays.asList(segment).containsAll(Arrays.asList(changeButton))) {
                        buttonsPieces[changeButton[1]].setEnabled(piecesEnabled);
                    }
                }
            }
        }
        setPiecesColor();
    }
    private void setPiecesColor() {
        for (int i = 0; i < enemyPieces.length; i++) {
            if (client.getPlayerName() == 1) {
                if (myPieces[i] != null) {
                    buttonsPieces[myPieces[i]].setIcon(green);
                    buttonsPieces[myPieces[i]].setDisabledIcon(green);
                }
                if (enemyPieces[i] != null) {
                    buttonsPieces[enemyPieces[i]].setDisabledIcon(red);
                }
            }
            if (client.getPlayerName() == 2) {
                if (myPieces[i] != null) {
                    buttonsPieces[myPieces[i]].setIcon(red);
                    buttonsPieces[myPieces[i]].setDisabledIcon(red);
                }
                if (enemyPieces[i] != null) {
                    buttonsPieces[enemyPieces[i]].setDisabledIcon(green);
                }
            }
        }
    }
    private void verifyWin() {
        for (Integer[] segment : winCondition) {
            if (Arrays.asList(segment).containsAll(Arrays.asList(myPieces))) {
                win = true;
                textArea.append("\n###### JOGADOR " + client.getPlayerName() + " GANHOU!!! ######");

                chat.sendMsg("$winer$");
                client.closeSocket();
            }
        }
    }
    private void verifyPlay(int bNum) {
        boolean isValid = false;
        Integer[] buttonToSwitch = new Integer[2];
        buttonToSwitch[0] = bNum;
        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i] == null) {
                buttonToSwitch[1] = i;
            }
        }
        for (Integer[] segment : winCondition) {
            if (Arrays.asList(segment).containsAll(Arrays.asList(buttonToSwitch))) {
                isValid = true;
                for (int i = 0; i < myPieces.length; i++) {
                    if (myPieces[i].equals(buttonToSwitch[0])) {
                        myPieces[i] = buttonToSwitch[1];
                    }
                }
            }
        }
        if (isValid) {
            attPieces();
            changeButtonPieces();
            client.sendPieceNum(bNum);
            client.sendNewArrayFlag(true);
            client.sendNewPieces(myPieces[0], myPieces[1], myPieces[2]);
            verifyWin();
        }
    }
    private void attPieces() {
        pieces = new Integer[7];
        for (int i = 0; i < myPieces.length; i++) {
            if (myPieces[i] != null) {
                pieces[myPieces[i]] = 1;
            }
            if (enemyPieces[i] != null) {
                pieces[enemyPieces[i]] = 2;
            }
        }
    }
    public void sendMsgChat() {
        String msg = sendTextField.getText();
        textArea.append("\nJOGADOR " + client.getPlayerName() + " -> " + msg);
        chat.sendMsg(msg);
        sendTextField.setText("");
        if (msg.equalsIgnoreCase( "!naoaguentomais")){
            draw = true;

            if(draw && playersDraw == 0){
                textArea.append("\n###### VOCÊ NÃO AGUENTA MAIS. ESPERA A RESPOSTA ######");
                playersDraw ++;

            }
            else if (draw && playersDraw == 1){
                textArea.append("\n###### VOCÊ TAMBEM NÃO AGUENTA MAIS ######");
                playersDraw ++;

            }

            if(draw && playersDraw == 2){
                textArea.append("\n###### NIGUÉM AGUENTA MAIS O JOGO ACABOU ######");
                client.closeSocket();
                piecesEnabled = false;
            }

        }
        if(msg.equalsIgnoreCase( "!vaiaguentar")){
            draw =false;
            playersDraw = 0;
        }

        if (msg.equalsIgnoreCase( "!jogartoalha")) {
            textArea.append("\n###### VOCÊ JOGOU A TOALHA. JOGADOR " + enemyP + " GANHOU!!! ######");
            client.closeSocket();
            piecesEnabled = false;
            changeButtonPiecesInicial();
        }
    }
    public void attChat() {
        String msg = "";
        while (!msg.equalsIgnoreCase( "$close$")) {
            msg = chat.reciveMsg();
            if (!msg.equalsIgnoreCase( "$winer$")) {
                textArea.append("\nJOGADOR " + enemyP + " -> " + msg);
            }
            if (msg.equalsIgnoreCase( "$winer$")) {
                textArea.append("\n###### JOGADOR " + enemyP + " GANHOU!!! ######");
                client.closeSocket();
                piecesEnabled = false;
            }
            if (msg.equalsIgnoreCase( "!naoaguentomais")){
                    draw = true;
                if(draw && playersDraw == 0){
                    textArea.append("\n###### JOGADOR " + enemyP + " NÃO AGUENTA MAIS SE VOCÊ TBM NÃO AGUENTA DIGITE !naoaguentomais  ######");
                    textArea.append("\n###### SE VOCÊ QUER CONTINUAR DIGITE !vaiaguentar ######");
                    playersDraw ++;
                }
                else if(draw && playersDraw == 1){
                    textArea.append("\n###### JOGADOR " + enemyP + " TAMBEM NÃO AGUENTA MAIS ######");
                    playersDraw ++;
                }
                if(draw && playersDraw == 2){
                    textArea.append("\n###### NIGUÉM AGUENTA MAIS O JOGO ACABOU ######");
                    client.closeSocket();
                    piecesEnabled = false;

                }

            }
            if(msg.equalsIgnoreCase( "!vaiaguentar")){
                textArea.append("\n###### JOGADOR " + enemyP + " DISSE VAI TER QUE AGUENTAR ######");
                draw =false;
                playersDraw = 0;
            }
            if (msg.equalsIgnoreCase( "!jogartoalha")) {
                textArea.append("\n###### VOCÊ GANHOU!!! JOGADOR " + enemyP + " JOGOU A TOALHA. ######");
                client.closeSocket();
                piecesEnabled = false;
            }
        }
    }
    private class ChatCliente {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        public ChatCliente() {

            try {
                socket = new Socket("localhost", 7025);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            }
            catch (IOException e) {
            }
        }
        public void sendMsg(String msg) {
            try {
                out.writeUTF(msg);
                out.flush();
            }
            catch (IOException e) {
            }
        }
        public String reciveMsg() {
            String msg = "";
            try {
                msg = in.readUTF();
            }
            catch (IOException e) {
            }
            return msg;
        }
    }

    private class ClienteSocket {
        private int playerName;
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        public ClienteSocket() {
            try {
                socket = new Socket("localhost", 6025);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                setPlayerName(in.readInt());
                System.out.println("JOGADOR " + playerName );
            }
            catch (IOException e) {
            }
        }
        public void sendPieceNum(int n) {
            try {
                out.writeInt(n);
                out.flush();
            }
            catch (IOException e) {
            }
        }
        public int receivePieceNum() {
            int piece = -1;
            try {
                piece = in.readInt();
            }
            catch (IOException e) {
            }
            return piece;
        }
        public void sendNewPieces(int piece0, int piece1, int piece2) {
            try {
                out.writeInt(piece0);
                out.writeInt(piece1);
                out.writeInt(piece2);
                out.flush();
            }
            catch (IOException e) {
            }
        }
        public Integer[] receiveNewPieces() {
            Integer[] pieces = {-1, -1, -1};
            try {
                pieces[0] = in.readInt();
                pieces[1] = in.readInt();
                pieces[2] = in.readInt();
            }
            catch (IOException e) {
            }
            return pieces;
        }
        public int getPlayerName() {
            return playerName;
        }
        public void setPlayerName(int playerName) {
            this.playerName = playerName;
        }

        public void closeSocket() {
            try {
                socket.close();
                System.out.println("CLOSED");
            }
            catch (IOException e) {
            }
        }

        public void sendNewArrayFlag(boolean newArray) {
            try {
                out.writeBoolean(newArray);
                out.flush();
            }
            catch (IOException e) {
            }
        }

        public boolean receiveNewArrayFlag() {
            boolean newArray = false;
            try {
                newArray = in.readBoolean();
            }
            catch (IOException e) {
            }
            return newArray;
        }

    }
    public static void main(String[] args) {
        Cliente c = new Cliente();
        c.initInterface();
        c.initThreads();
        c.initButtons();
        c.initChat();
    }
}
