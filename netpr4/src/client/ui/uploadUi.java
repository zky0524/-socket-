package client.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


 


 

/**

 * @author scx

 *	客户端登录界面

 */

public class uploadUi extends JFrame implements ActionListener {
	
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

	private static final long serialVersionUID = -8830003248247613172L;
	
	private Socket s;

	private JFileChooser fileChooser;
	
	JProgressBar progressBar;

	private JButton upload_btn, choose_btn;

	private JTextField path_txt;

	public  uploadUi(Socket s) {
		
		this.s=s;

		init();

		setTitle("upload");

		setSize(300, 200);

		setLocationRelativeTo(null);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setResizable(false);

		setVisible(true);

	}

 

	public void init() {

		setLayout(new FlowLayout());

		choose_btn = new JButton("Choose");
		upload_btn = new JButton("Upload");
		
		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		
		fileChooser = new JFileChooser();
		path_txt = new JTextField(20);
		
		

		add(path_txt);
		add(choose_btn);
		add(upload_btn);
		add(progressBar);
		
		 progressBar.addChangeListener(new ChangeListener() {
	            @Override
	            public void stateChanged(ChangeEvent e) {
	                System.out.println("当前进度值: " + progressBar.getValue() + "; " +
	                        "进度百分比: " + progressBar.getPercentComplete());
	            }
	        });
		// while(true) {
			 choose_btn.addActionListener(this);
			 upload_btn.addActionListener(this);
		 //}
		

	}

 

	@Override

	public void actionPerformed(ActionEvent e) {
		//while(true) {
			if(e.getSource()==choose_btn){

				int state = fileChooser.showSaveDialog(null);
				if (state == 0) {
					String pathChoose = fileChooser.getSelectedFile().getPath();
					path_txt.setText(pathChoose);
				}
			}
			if (e.getSource() == upload_btn) {
				if(path_txt.getText().trim()==null||"".equals(path_txt.getText().trim())){
					JOptionPane.showMessageDialog(null, "请先选择文件再上传！！");
					return ;
				}
				try {
					uploadFile();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
	//	}
	}
	
	private void uploadFile() throws IOException, InterruptedException {
		File file = null;
		FileInputStream fis = null;
		DataOutputStream dos = null;
		//BufferedInputStream bis = null;
		String path = path_txt.getText().trim();
		file = new File(path);
		//String fname = path.substring(path.lastIndexOf("\\") + 1);
		try {
			fis = new FileInputStream(file);
			dos = new DataOutputStream(s.getOutputStream());
			
			dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();
			
			byte[] bytes = new byte[1024];
            int length = 0;
            long progress = 0;
            while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                dos.write(bytes, 0, length);
                dos.flush();
                progress += length;
                int t=(int) (100*progress/file.length());
                (new Thread(new Worker(t,progressBar))).start();
                Thread.sleep(1);
            }
            System.out.println("======== 文件传输成功 ========");
		}finally {
			if (dos != null)
				try {
					dos.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			if (fis != null)
				try {
					fis.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
		}
	}

 

	

}
