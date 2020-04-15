
package client;

 

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import svr.ui.SocketUi;


public class Server {
	
	
	
	class Worker implements Runnable {
		// 为连入的客户端打开的套接口
		Socket t;
        

		Worker(Socket t) {
			this.t = t;
		}

		@Override
		public void run() {
			try {
				new SocketUi(t);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	
	public Server(){

		try {
			ServerSocket ss=new ServerSocket(8899);
			Socket socket=null;
			JOptionPane.showMessageDialog(null, "服务器已启动!请连接...");
			//循环监听客户端的连接
			while(true){
				socket=ss.accept();
				//为每个客户开启一个线程
				(new Thread(new Worker(socket))).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
