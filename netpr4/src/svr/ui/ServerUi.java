
package svr.ui;

 

import java.awt.FlowLayout;

import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

 

import javax.swing.JButton;

import javax.swing.JFrame;

import client.Server;

/**

 * 开启服务端界面

 */

public class ServerUi extends JFrame implements ActionListener{

	private JButton startServer_btn;
	private JButton endServer_btn;
	private Server startService;
	
	public ServerUi(){
		
		setLayout(new FlowLayout());
		
		startServer_btn=new JButton("开启服务");
		endServer_btn=new JButton("关闭服务");
		
		add(startServer_btn);
		add(endServer_btn);

		setTitle("服务端");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		startServer_btn.addActionListener(this);
		endServer_btn.addActionListener(this);

	}

	public static void main(String[] args) {
		new ServerUi();
	}

	@Override

	public void actionPerformed(ActionEvent e) {

		// TODO Auto-generated method stub

		if(e.getSource()==startServer_btn){
			
			if(startService==null){

				/*
				 * 在线程中开启服务器  避免使用main线程 服务器一直开启 
				 * main线程一直阻塞  无法对其它事物进行处理
				 */
				new startServerThread().start();

			}

		}

		//退出服务器

		if(e.getSource()==endServer_btn){
			
			startService=null;
			System.exit(0);
			
		}

	}

	private class startServerThread extends Thread{

		@Override

		public void run() {

			// TODO Auto-generated method stub

			startService=new Server();

		}

	}

}
