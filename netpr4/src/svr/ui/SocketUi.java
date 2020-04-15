package svr.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.Socket;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import svr.thread.Recieve;


public class SocketUi extends JFrame {
	
	private Socket t;
	
	private Box box1, box2, box3,box4, baseBox;

	private JLabel label1, label2,label3,jd;
	
	JProgressBar progressBar;
	
	private DataInputStream dis;

    private FileOutputStream fos;
	
	private static DecimalFormat df = null;

    static {

        // 设置数字格式，保留一位有效小数

        df = new DecimalFormat("#0.0");

        df.setRoundingMode(RoundingMode.HALF_UP);

        df.setMinimumFractionDigits(1);

        df.setMaximumFractionDigits(1);

    }
    
    class Worker implements Runnable {
		// 为连入的客户端打开的套接口
		int t;
		JProgressBar progressBar;

		Worker(int t,JProgressBar progressBar) {
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
	
	public SocketUi(Socket t) throws IOException, InterruptedException {
		
		this.t=t;

		setLayout(new FlowLayout());

		//System.out.println("11111111111111111111111111111111111111");
		
		init();
		
		//System.out.println("11111111111111111111111111111111111111");
		
		(new Thread(new Recieve(t,progressBar, box2, box3, box4))).start();
		
		//System.out.println("11111111111111111111111111111111111111");

		setTitle("接收文件");

		setSize(300, 200);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);

		setVisible(true);

	}
	
	public void init() {
		
		jd = new JLabel("传输进度:");
		
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		box1 = Box.createHorizontalBox();
		box2 = Box.createHorizontalBox();
		box3 = Box.createHorizontalBox();
		box4 = Box.createHorizontalBox();
		baseBox = Box.createVerticalBox();
		
		box1.add(jd);
		box1.add(progressBar);
		
		baseBox.add(Box.createVerticalStrut(10));
		baseBox.add(box1);
		baseBox.add(Box.createVerticalStrut(15));
		baseBox.add(box2);
		baseBox.add(Box.createVerticalStrut(15));
		baseBox.add(box3);
		baseBox.add(Box.createVerticalStrut(15));
		baseBox.add(box4);
		
		add(baseBox);

		progressBar.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("当前进度值: " + progressBar.getValue() + "; " +
                        "进度百分比: " + progressBar.getPercentComplete());
            }
        });

	}
	
}
