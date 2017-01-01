package il.ac.hit.chat;

import java.util.*;

public class MessageBoard implements StringConsumer, StringProducer
{
	private Vector<ConnectionProxy> connections = new Vector<ConnectionProxy>();
	
	@Override
	public void addConsumer(StringConsumer sc)
	{
		connections.add((ConnectionProxy) sc);
	}

	@Override
	public void removeConsumer(StringConsumer sc)
	{
		connections.remove(sc);
	}

	@Override
	public void consume(String str)
	{
		for (int i = 0 ; i < connections.size() ; i++)
		{
			if (connections.get(i).isConnected())
			{
				connections.get(i).consume(str);
			}
		}
	}
}
