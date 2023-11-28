
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor
{
    private ServerSocket cliente;
    private ServerSocket chat;
    private int numP;
    private SocketCliente player1;
    private SocketCliente player2;
    private SocketChat player1Chat;
    private SocketChat player2Chat;
    private boolean p1ChangeFlag;
    private boolean p2ChangeFlag;
    private int p1NumPieceButton;
    private int p2NumPieceButton;
    private int p1ChangePieces0;
    private int p1ChangePieces1;
    private int p1ChangePieces3;
    private int p2ChangePieces0;
    private int p2ChangePieces1;
    private int p2ChangePieces2;

    public Servidor() {
        System.out.println("Iniciando Server");
        numP = 0;
        try {
            cliente = new ServerSocket(6025);
            chat = new ServerSocket(7025);
        }
        catch (IOException e) {
        }
    }

    private class SocketCliente implements Runnable {
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private int playerName;
        public SocketCliente(Socket cliente, int id) {
            socket = cliente;
            playerName = id;
            try {
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
            }
            catch (IOException e) {
            }
        }
        @Override
        public void run() {
            try {
                out.writeInt(playerName);
                out.flush();
                while (true) {
                    if (playerName == 1) {
                        p1NumPieceButton = in.readInt();
                        player2.sendPieceNum(p1NumPieceButton);
                        p1ChangeFlag = in.readBoolean();
                        player2.sendNewArrayFlag(p1ChangeFlag);
                        if (p1ChangeFlag == true) {
                            p1ChangePieces0 = in.readInt();
                            p1ChangePieces1 = in.readInt();
                            p1ChangePieces3 = in.readInt();
                            player2.sendNewPieces(p1ChangePieces0, p1ChangePieces1, p1ChangePieces3);
                        }
                    }
                    else {
                        p2NumPieceButton = in.readInt();
                        player1.sendPieceNum(p2NumPieceButton);
                        p2ChangeFlag = in.readBoolean();
                        player1.sendNewArrayFlag(p2ChangeFlag);

                        if (p2ChangeFlag == true) {
                            p2ChangePieces0 = in.readInt();
                            p2ChangePieces1 = in.readInt();
                            p2ChangePieces2 = in.readInt();

                            player1.sendNewPieces(p2ChangePieces0, p2ChangePieces1, p2ChangePieces2);
                        }
                    }
                }
            }
            catch (IOException e) {
                player1.closeSocket();
                player2.closeSocket();
            }
        }
        public void closeSocket() {
            try {
                socket.close();
                System.out.println("CLOSED");
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
        public void sendNewArrayFlag(boolean newArray) {
            try {
                out.writeBoolean(newArray);
                out.flush();
            }
            catch (IOException e) {
            }
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
    }
    public void acceptConnections() {
        try {
            System.out.println("Aguardando Conectar...");
            while (numP < 2) {
                numP++;
                Socket s = cliente.accept();
                SocketCliente cliente = new SocketCliente(s, numP);
                Thread tCliete = new Thread(cliente);
                tCliete.start();
                Socket sc = chat.accept();
                SocketChat chat = new SocketChat(sc, numP);
                Thread tChat = new Thread(chat);
                tChat.start();
                if (numP == 1) {
                    player1 = cliente;
                    player1Chat = chat;
                }
                else {
                    player2 = cliente;
                    player2Chat = chat;
                }
                System.out.println("Jogador " + numP + " está conectado!");
            }
            System.out.println("Conexão Feita");
        }
        catch (IOException e) {
        }
    }
    private class SocketChat implements Runnable {
        private Socket chat;
        private DataInputStream in;
        private DataOutputStream out;
        private int playerName;
        public SocketChat(Socket s, int id) {
            chat = s;
            playerName = id;
            try {
                in = new DataInputStream(chat.getInputStream());
                out = new DataOutputStream(chat.getOutputStream());
            }
            catch (IOException e) {
            }
        }
        public void sendMsg(String message) {
            try {
                out.writeUTF(message);
                out.flush();
            }
            catch (IOException e) {
            }
        }
        public void closeSocket() {
            try {
                chat.close();
                System.out.println("CLOSED");
            }
            catch (IOException e) {
            }
        }
        @Override
        public void run() {
            try {
                String msg = "";
                while (!msg.equalsIgnoreCase("$close$")) {
                    msg = in.readUTF();

                    if (playerName == 1) {
                        player2Chat.sendMsg(msg);
                    }
                    else {
                        player1Chat.sendMsg(msg);
                    }
                }
                player1Chat.closeSocket();
                player2Chat.closeSocket();
            }
            catch (IOException e) {
                player1Chat.closeSocket();
                player2Chat.closeSocket();
            }
        }
    }
    public static void main(String[] args) {
        Servidor server = new Servidor();
        server.acceptConnections();
    }
}
