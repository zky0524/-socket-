package svr.thread;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.Socket;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import svr.ui.SocketUi;

public class Recieve implements Runnable{
	Socket t=null;
	JProgressBar progressBar;
	private DataInputStream dis;
    private FileOutputStream fos;
    private Box box2,box3,box4;
	private JLabel label1, label2,label3;
	private static DecimalFormat df = null;
    static {
        // 设置数字格式，保留一位有效小数
        df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
    }
    
    class Worker1 implements Runnable {
		// 为连入的客户端打开的套接口
		int t;
		JProgressBar progressBar;

		Worker1(int t,JProgressBar progressBar) {
			this.t = t;
			this.progressBar = progressBar;
		}

		@Override
		public void run() {
			try {
				Dimension d = progressBar.getSize();
                Rectangle rect = new Rectangle(0, 0, d.width, d.height);
                //progressBar.setValue(jd);
                progressBar.setValue(t);
                progressBar.paintImmediately(rect);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
    class Worker2 implements Runnable {

		Worker2() {
		}

		@Override
		public void run() {
			try {
				box2.add(label1);
		        box3.add(label2);
		        box4.add(label3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
    
	public  Recieve(Socket t,JProgressBar progressBar,Box box2,Box box3,Box box4) {
		this.t = t;
		this.progressBar = progressBar;
		this.box2=box2;
		this.box3=box3;
		this.box4=box4;
	}
	
	@Override
	public void run(){
		try {
			dis = new DataInputStream(t.getInputStream());
	        // 文件名和长度
	        String fileName = dis.readUTF();
	        long fileLength = dis.readLong();
	        File directory = new File("D:\\FTCache");
	        if(!directory.exists()) {
	            directory.mkdir();
	        }

	        File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
	        fos = new FileOutputStream(file);

	        // 开始接收文件
	        byte[] bytes = new byte[1024];
	        int length = 0;
	        long progress = 0;
		    while((length = dis.read(bytes, 0, bytes.length)) != -1) {
				   fos.write(bytes, 0, length);
				   fos.flush();
				   progress += length;
				   int t=(int) (100*progress/fileLength);
	               (new Thread(new Worker1(t,progressBar))).start();
	               Thread.sleep(1);
			}
	        label1 = new JLabel("文件接收成功:");
	        label2 = new JLabel("文件名: "+fileName);
	        label3 = new JLabel("文件大小: "+getFormatFileSize(fileLength));
	        (new Thread(new Worker2())).start();
	        Thread.sleep(1);
	        System.out.println("======== 文件接收成功 [File Name：" + fileName + "] [Size：" + getFormatFileSize(fileLength) + "] ========");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	
	
	private String getFormatFileSize(long length) {
        double size = ((double) length) / (1 << 30);
        if(size >= 1) {
            return df.format(size) + "GB";
        }
        size = ((double) length) / (1 << 20);
        if(size >= 1) {
            return df.format(size) + "MB";
        }
        size = ((double) length) / (1 << 10);
        if(size >= 1) {
            return df.format(size) + "KB";
        }
        return length + "B";
    }
}
