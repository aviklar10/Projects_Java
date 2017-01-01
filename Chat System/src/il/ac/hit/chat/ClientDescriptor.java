package il.ac.hit.chat;

public class ClientDescriptor implements StringConsumer, StringProducer
{
	private MessageBoard mb;
	private String userName;
	
	public ClientDescriptor()
	{
		userName="";
	}

	@Override
	public void addConsumer(StringConsumer sc)
	{
		mb=(MessageBoard) sc;
	}

	@Override
	public void removeConsumer(StringConsumer sc)
	{
		mb.removeConsumer(sc);
	}

	@Override
	public void consume(String str)
	{
		if (userName.equals(""))
		{
			userName = str;
			mb.consume(str + " has join the chat...");
		}
		else
		{
			if (str.equals("***LEFT***"))
			{
				mb.consume(userName + " has left the room...");
			}
			else
				{
					mb.consume(userName + " says: " + str);
				}
		}
	}

}
