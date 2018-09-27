package org.elsys.netprog.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;

class ReaderWriter extends Thread {

	private BufferedReader in;
	private PrintWriter out;
	
	ReaderWriter(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void run() {
	    String userInput;
	    try {
			while ((userInput = in.readLine()) != null)
			{
			    out.println(userInput);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

public class SocketsClient {

	public static void main(String[] args) throws IOException {
		Socket echoSocket = null;
		try {
		    echoSocket = new Socket("localhost", 10001);
		    PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
		            new InputStreamReader(echoSocket.getInputStream()));
		    BufferedReader stdIn = new BufferedReader(
		            new InputStreamReader(System.in));
		    
		    ReaderWriter sender = new ReaderWriter(stdIn, out);
		    sender.start();
		    ReaderWriter reciever = new ReaderWriter(in, new PrintWriter(System.out, true));
		    reciever.start();
		    sender.join();
		    reciever.join();
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		} finally {
			if (echoSocket != null && !echoSocket.isClosed()) {
				echoSocket.close();
			}
		}
	}

}
