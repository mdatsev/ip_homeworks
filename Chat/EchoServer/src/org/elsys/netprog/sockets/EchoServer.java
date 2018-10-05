package org.elsys.netprog.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ChatHandler extends Thread {
	private ChatSocket socket;
	private ArrayList<ChatSocket> sockets;
	
	public ChatHandler(ChatSocket socket, ArrayList<ChatSocket> sockets) throws IOException {
		this.socket = socket;
		this.sockets = sockets;
	}
	
	@Override
	public void run() {
	    String userInput;
	    try {
			while ((userInput = socket.in.readLine()) != null)
			{
				for(ChatSocket s : sockets) {
					if(s != socket)
						s.out.println(userInput);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ChatSocket {
	public Socket socket;
	public PrintWriter out;
	public BufferedReader in;
	
	public ChatSocket(Socket socket) throws IOException {
		this.socket = socket;
	    out = new PrintWriter(socket.getOutputStream(), true);
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
}

public class EchoServer {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(10001);
		    ArrayList<ChatSocket> clientSockets = new ArrayList<>();
		    for(;;)
		    {
		    	ChatSocket clientSocket = new ChatSocket(serverSocket.accept());
		    	clientSockets.add(clientSocket);
		    	System.out.println("client connected");
		    	ChatHandler handler = new ChatHandler(clientSocket, clientSockets);
		    	handler.start();
		    }
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			
			System.out.println("Server closed");
		}
	}

}
