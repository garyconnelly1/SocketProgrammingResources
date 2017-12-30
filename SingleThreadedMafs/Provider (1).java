

import java.io.*;
import java.net.*;
public class Provider{
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	Provider(){}
	void run()
	{
		try{
			//1. creating a server socket
			providerSocket = new ServerSocket(2004, 10);
			//2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from " + connection.getInetAddress().getHostName());
			//3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			//4. The two parts communicate via the input and output streams
			do{
				try{
					sendMessage("The following are the options\n Enter ADD to add two number\n Enter SQUARE to get the number square\n Finished to terminate\n");

					message = (String)in.readObject();
					System.out.println(message);
					if(message.equalsIgnoreCase("ADD"))
					{
						System.out.println("In the add sequence");
						sendMessage("Please enter num1");
						message = (String)in.readObject();
						int num1 = Integer.parseInt(message);

						sendMessage("Please enter num2");
						message = (String)in.readObject();
						int num2 = Integer.parseInt(message);

						sendMessage(""+(num1+num2));
					}
					else if(message.equalsIgnoreCase("SQUARE"))
					{

					}
					else
					{
						sendMessage(message);
					}




				}
				catch(ClassNotFoundException classnot){
					System.err.println("Data received in unknown format");
				}
			}while(!message.equals("FINISHED"));
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				providerSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	public static void main(String args[])
	{
		Provider server = new Provider();
		while(true){
			server.run();
		}
	}
}
