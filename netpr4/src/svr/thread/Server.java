
package svr.thread;

 

import java.io.IOException;
import java.net.ServerSocket;

import java.net.Socket;

import javax.swing.JOptionPane;
import svr.ui.SocketUi;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;


/**

 * @author scx

 *	服务端

 */

public class Server {
	
	private static Selector selector;
	int port=8899;

	class seletor implements Runnable{
		private Selector selector;
		private int port;
		private ServerSocketChannel serverSocketChannel;

		public void selector(Selector selector, int port,ServerSocketChannel serverSocketChannel){
			this.port=port;
			this.selector=selector;
			this.serverSocketChannel=serverSocketChannel;
		}

		@Override
		public void run(){
			/**
			 * 将通道(Channel)注册到通道管理器(Selector)，并为该通道注册selectionKey.OP_ACCEPT事件
			 * 注册该事件后，当事件到达的时候，selector.select()会返回，
			 * 如果事件没有到达selector.select()会一直阻塞。
			 */
			try {
				serverSocketChannel.register(this.selector,SelectionKey.OP_ACCEPT);

				//循环处理
				while(true){
					//当注册事件到达时，方法返回，否则该方法一直阻塞
					selector.select();

					//获取监听事件
					Set<SelectionKey> selectionKeys=selector.selectedKeys();
					Iterator<SelectionKey> iterator=selectionKeys.iterator();

					//迭代处理
					while(iterator.hasNext()){
						//获取事件
						SelectionKey key=iterator.next();

						//移除事件，避免重复处理
						iterator.remove();

						//检查是否是一个就绪的可以被接受的客户端请求连接
						if(key.isAcceptable()){}

						//检查套接字是否已经准备好读数据
						else if(key.isReadable()){}
					}
				}
			} catch (ClosedChannelException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}


		}
	}
	
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
			//创建通道管理器selector
			selector=Selector.open();

			//创建通道ServerSocketChannel
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

			//将通道设置为非阻塞
			serverSocketChannel.configureBlocking(false);

			//将ServerSocketChannel对应的ServerSocket绑定到指定端口
			ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.bind(new InetSocketAddress(port));

			Socket socket=null;

			JOptionPane.showMessageDialog(null, "服务器已启动!请连接...");

			//循环监听客户端的连接
//			ServerSocket ss=new ServerSocket(8899);

			while(true){

				socket=serverSocket.accept();

				//为每个客户开启一个线程

				(new Thread(new Worker(socket))).start();


			}

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}


	}

//	public void NIO() throws IOException {
//		//创建通道管理器Selector
//		selector=	Selector.open();
//
//		//创建通道ServerSocketChannel
//		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
//
//		//将通道设置为非阻塞
//		serverSocketChannel.configureBlocking(false);
//
//		//将ServerSocketChannel对应的ServerSocket绑定到指定端口
//
//		ServerSocket serverSocket=serverSocketChannel.socket();
//		serverSocket,bind(new InetSocketAddress(8899));
//
//		ServerSocket ss=new ServerSocket(8899);
//	}
 

	

}
