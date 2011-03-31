package me.saac.i.ai;
import java.net.*;
import java.io.*;
import java.util.regex.*;


public class Client {
    static int portOffset = 7000;
    static int inputLength = 124;
    static Pattern inputPrompt = Pattern.compile("your move?");
    static int playerNo;
    static Player player;
    static boolean useImprovedOpponentModel;
    
    public static void main(String[] args) throws Exception {

	// take the player number from command line and connect to the appropriate
	// TCP port
	playerNo = Integer.parseInt(args[0].trim());
	int port = portOffset + playerNo;
	
	Socket socket = new Socket("localhost", port);
	BufferedReader in = 
	    new BufferedReader(new InputStreamReader(socket.getInputStream()));
	PrintWriter out = 
	    new PrintWriter(socket.getOutputStream(), true);
	

	useImprovedOpponentModel = args.length > 1 && "--improved".matches(args[1]);
	player = new Player(useImprovedOpponentModel);
	
	out.print("My name is " + player.name);
	out.flush();

	char[] cbuf = new char[inputLength];
	String input;

	// loop to take input and process it
	while(true) {
		try {
			if (in.ready()) {
			        // take input
				in.read(cbuf, 0, inputLength);
				input = new String(cbuf);
				input = input.trim();
				player.receive(input);
				
				// if we're being prompted for an action
				// send the result of getAction()
				if(inputPrompt.matcher(input).find()) {
					out.print(player.getAction(input));
					out.flush();

				// if input is "SHUTDOWN", close the TCP socket and exit
				} else if (input.equals("SHUTDOWN")) {
					socket.close();
					System.exit(0);
				}
			}
		} catch(Exception e) {
		        // if there's an exception, print stack trace and exit
			System.out.println("Caught exception: ");
			e.printStackTrace();
			socket.close();
			System.exit(0);
		}
	}
    }

}