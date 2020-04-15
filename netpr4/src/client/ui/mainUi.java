package client.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

 


 
public class mainUi extends JFrame implements ActionListener {
	
	private Box box1, box2, box3, baseBox;
	private JLabel ip, port;
	private JTextField ip_txt;
	private JTextField port_txt;
	private JButton connect_btn;

	public mainUi() {

		setLayout(new FlowLayout());
		init();
		
		setTitle("client");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);

	}

 

	public void init() {

		ip = new JLabel("ip地址:");
		port = new JLabel("端口号:");
		
		ip_txt = new JTextField(10);
		port_txt = new JTextField(10);
		connect_btn = new JButton("连接");

		box1 = Box.createHorizontalBox();
		box2 = Box.createHorizontalBox();
		box3 = Box.createHorizontalBox();

		box1.add(ip);
		box1.add(Box.createHorizontalStrut(25));
		box1.add(ip_txt);
		
		box2.add(port);
		box2.add(Box.createHorizontalStrut(25));
		box2.add(port_txt);

		box3.add(connect_btn);
		box3.add(Box.createHorizontalStrut(25));

		baseBox = Box.createVerticalBox();
		
		baseBox.add(Box.createVerticalStrut(25));
		baseBox.add(box1);
		baseBox.add(Box.createVerticalStrut(25));
		baseBox.add(box2);
		baseBox.add(Box.createVerticalStrut(25));
		baseBox.add(box3);
		baseBox.add(Box.createVerticalStrut(25));

		add(baseBox);

		connect_btn.addActionListener(this);
	}

 

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==connect_btn){
			try {
				Socket ClientSocket=new Socket(ip_txt.getText(), Integer.parseInt(port_txt.getText()));
				
				this.dispose();
				new uploadUi(ClientSocket);

			//	}

			} catch (UnknownHostException e1) {

				// TODO Auto-generated catch block
			///	this.dispose();

				JOptionPane.showMessageDialog(null, "服务端未开启！");

			} catch (IOException e1) {

				// TODO Auto-generated catch block
			//	this.dispose();

				JOptionPane.showMessageDialog(null, "服务端未开启！");

			}

		}

	}

 

	

}
