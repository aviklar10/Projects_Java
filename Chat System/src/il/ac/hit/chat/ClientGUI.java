package il.ac.hit.chat;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientGUI implements StringConsumer, StringProducer
{
	public static ClientGUI Application;
	private ConnectionProxy cp; 
	private JFrame frame;
	private JPanel upPanel,centerPanel,downPanel;
	private JButton connectBtn,disconnectBtn,SendBtn;
	private JTextArea bodyText;
	private JLabel portLbl,hostLbl,MessageLbl,usernameLbl;
	private JTextField portTxt,hostTxt,MessageTxt,usernameTxt;
	private JScrollPane scrollpane;
	
	public ClientGUI()
	{
		connectBtn = new JButton("Connect!");
		SendBtn = new JButton("Sent!");
		SendBtn.setEnabled(false);
		disconnectBtn = new JButton("Disconnct!");
		disconnectBtn.setEnabled(false);
		
		portLbl = new JLabel("Port: ");
		hostLbl = new JLabel("Host/IP: ");
		MessageLbl = new JLabel("Message: ");
		usernameLbl = new JLabel("User Name: ");
		
		portTxt = new JTextField("", 10);
		hostTxt = new JTextField("", 10);
		MessageTxt = new JTextField("", 150);
		MessageTxt.setEnabled(false);
		usernameTxt = new JTextField("", 15);
		
		bodyText = new JTextArea("Wellcome to the chat room... =]\n");
		bodyText.setEditable(false);
		scrollpane = new JScrollPane(bodyText);
		
		upPanel = new JPanel(new GridLayout(1, 8));
		centerPanel = new JPanel(new BorderLayout());
		downPanel = new JPanel(new BorderLayout());
		
		upPanel.add(hostLbl);
		upPanel.add(hostTxt);
		upPanel.add(portLbl);
		upPanel.add(portTxt);
		upPanel.add(usernameLbl);
		upPanel.add(usernameTxt);
		upPanel.add(connectBtn);
		upPanel.add(disconnectBtn);
		
		centerPanel.add(scrollpane);
		
		downPanel.add(MessageLbl,BorderLayout.WEST);
		downPanel.add(MessageTxt,BorderLayout.CENTER);
		downPanel.add(SendBtn,BorderLayout.EAST);
		
		frame = new JFrame("Chat room");
		frame.setLayout(new BorderLayout());
		frame.add(upPanel,BorderLayout.NORTH);
		frame.add(centerPanel,BorderLayout.CENTER);
		frame.add(downPanel, BorderLayout.SOUTH);
		
		upPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		upPanel.setBackground(new Color(0, 162, 232));
		centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		centerPanel.setBackground(new Color(0, 162, 232));
		downPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		downPanel.setBackground(new Color(0, 162, 232));
	}
	
	private void setActions()
	{
		connectBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (hostTxt.getText().equals("") || portTxt.getText().equals(""))
					cp = new ConnectionProxy();
				else
					cp = new ConnectionProxy(hostTxt.getText(), Integer.parseInt(portTxt.getText()));
				addConsumer(cp);
				if (cp.isConnected())
				{
					bodyText.append("Connection established =]\n");
					if (usernameTxt.getText().equals(""))
					{
						usernameTxt.setText("guest");
					}
					cp.consume(usernameTxt.getText());
					connectBtn.setEnabled(false);
					usernameTxt.setEnabled(false);
					MessageTxt.setEnabled(true);
					SendBtn.setEnabled(true);
					disconnectBtn.setEnabled(true);
					hostTxt.setEnabled(false);
					portTxt.setEnabled(false);
				}
				else
				{
					cp = null;
					bodyText.append("Connection failed...\n");
				}
			}
		});
		
		disconnectBtn.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				cp.consume("***LEFT***");
				removeConsumer(Application);
				bodyText.append("You left the room...\n");
				connectBtn.setEnabled(true);
				usernameTxt.setEnabled(true);
				MessageTxt.setEnabled(false);
				SendBtn.setEnabled(false);
				disconnectBtn.setEnabled(false);
				hostTxt.setEnabled(true);
				portTxt.setEnabled(true);
			}
		});
		
		SendBtn.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (!MessageTxt.getText().equals(""))
				{
					cp.consume(MessageTxt.getText());
					MessageTxt.setText("");
				}
			}
		});
		
		
		
	}
	
	public void Show()
	{
		setActions();
		frame.addWindowListener(new WindowAdapter()
		{

			@Override
			public void windowClosing(WindowEvent e)
			{
				if (cp != null)
					removeConsumer(cp);
				System.exit(0);
			}
			
		});
		
		frame.setSize(1000, 600);
		frame.setVisible(true);
	}
	
	public static void main(String args[])
	{
		EventQueue.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				Application = new ClientGUI();
				Application.Show();
			}
		});
		
	}

	@Override
	public void addConsumer(StringConsumer sc)
	{
		cp = (ConnectionProxy) sc;
		cp.addConsumer(Application);
		cp.start();
	}

	@Override
	public void removeConsumer(StringConsumer sc)
	{
		cp.removeConsumer(Application);
		cp= null;
	}

	@Override
	public void consume(String str)
	{
		bodyText.append(str);
		bodyText.append("\n");
	}
}
