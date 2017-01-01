package il.ac.hit.chat;

import java.io.*;
import java.net.*;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer
{
	private boolean isConnected = false;
	@SuppressWarnings("unused")
	private String hostName;
	@SuppressWarnings("unused")
	private int port;
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	private DataInputStream dataRec;
	private DataOutputStream dataSend;
	private StringConsumer client;
	
	public ConnectionProxy(Socket socket)
	{	
		try
		{
			this.socket = socket;
			in = socket.getInputStream();
			dataRec = new DataInputStream(in);
			out = socket.getOutputStream();
			dataSend = new DataOutputStream(out);
			client = null;
			if (this.socket != null) isConnected = true;
			else isConnected = false;
		} 
		catch (IOException e){}	
	}

	public ConnectionProxy(String hostName, int port)
	{
		try
		{
			this.hostName = hostName;
			this.port = port;
			this.socket = new Socket(hostName, port);
			in = socket.getInputStream();
			dataRec = new DataInputStream(in);
			out = socket.getOutputStream();
			dataSend = new DataOutputStream(out);
			client = null;
			if (this.socket != null) isConnected = true;
			else isConnected = false;
			
		} 
		catch (UnknownHostException e){} 
		catch (IOException e){}
	}

	public ConnectionProxy()
	{
		try
		{
			this.socket = new Socket("127.0.0.1", 1300);
			in = socket.getInputStream();
			dataRec = new DataInputStream(in);
			out = socket.getOutputStream();
			dataSend = new DataOutputStream(out);
			client = null;
			if (this.socket != null) isConnected = true;
			else isConnected = false;
		} 
		catch (UnknownHostException e){}
		catch (IOException e){}
	}
	
	public boolean isConnected()
	{
		return isConnected;
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{ 
					client.consume(dataRec.readUTF()); 
			} 
			catch (IOException e)
			{
				removeConsumer(client);
			}
			catch (NullPointerException e) {}
		}
	}

	@Override
	public void addConsumer(StringConsumer sc)
	{
		client = sc;
	}

	@Override
	public void removeConsumer(StringConsumer sc)
	{
		if (in!=null) try {in.close();} catch (IOException e){e.printStackTrace();}
		if (out!=null) try {out.close();} catch (IOException e){e.printStackTrace();}
		if (dataRec!=null) try {dataRec.close();} catch (IOException e){e.printStackTrace();}
		if (dataSend!=null) try {dataSend.close();} catch (IOException e){e.printStackTrace();}
		if (socket!=null) try {socket.close();} catch (IOException e){e.printStackTrace();}
		socket = null;
		client = null;
		isConnected = false;
	}

	@Override
	public void consume(String str)
	{
		try
		{
			dataSend.writeUTF(str);
		} 
		catch (IOException e){e.printStackTrace();}
	}

}
