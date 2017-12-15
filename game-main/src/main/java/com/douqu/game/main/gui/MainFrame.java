package com.douqu.game.main.gui;

import com.douqu.game.main.gui.component.i.Component;
import com.douqu.game.main.gui.component.impl.BtnComponent;
import com.douqu.game.main.gui.component.impl.LabelComponent;
import com.douqu.game.main.gui.component.impl.TxtComponent;
import com.douqu.game.main.gui.job.UpdateFrame;
import com.douqu.game.main.GameServer;


import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MainFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int TIME_CLEAR_CONSOLE = 2;
	
	public static final int WIDTH = 800;
	
	public static final int HEIGHT = 600;
	
	public static BtnComponent btnComponent;

	public static LabelComponent labelComponent;

	public static TxtComponent txtComponent;

	public MainFrame()
	{
		setSize(WIDTH, HEIGHT);
		setLocation(300, 100);
		setResizable(false);
		
		initWindowListener();
		
		setTitle("Server Manager");
		setUndecorated(false);
		
		setForeground(Color.BLACK);

		initContainer();
		initComponent();
		initPool();
		initJob();
	}

	private void initWindowListener()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowListener()
		{
			public void windowActivated(WindowEvent e)
			{

			}

			public void windowClosed(WindowEvent e)
			{

			}

			public void windowClosing(WindowEvent e)
			{
			   int res = JOptionPane.showConfirmDialog(null,"Are you sure EXIT？","EXIT",JOptionPane.OK_CANCEL_OPTION);
			   
			   if(res == JOptionPane.OK_OPTION)
			   {
				   if(GameServer.getInstance().isRunning())
				   {
						GameServer.getInstance().stop();
				   }

				   System.exit(0);
			   }
			}

			public void windowDeactivated(WindowEvent e)
			{
				   MainFrame mf = (MainFrame)e.getSource();
				   mf.setVisible(true);
			}

			public void windowDeiconified(WindowEvent e)
			{

			}

			public void windowIconified(WindowEvent e)
			{

			}

			public void windowOpened(WindowEvent e)
			{

			}});
	}

	private void initContainer()
	{
		Container container = getContentPane();
		container.setLayout(null);
		container.setBackground(new Color(19,71,100));
	}

	private void initComponent()
	{
		btnComponent = new BtnComponent();
		btnComponent.setJFrame(this);
		btnComponent.addToFrame();
		
		labelComponent = new LabelComponent();
		labelComponent.setJFrame(this);
		labelComponent.addToFrame();
		
		txtComponent = new TxtComponent();
		txtComponent.setJFrame(this);
		txtComponent.addToFrame();
	}
	
	public static void println(Object obj)
	{
		if(txtComponent == null || txtComponent.infoText == null)
			return;

		try
		{
			String line = System.getProperty("line.separator");
			try
			{
				txtComponent.infoText.append(new String(obj.toString().getBytes(),"UTF-8") + line);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		catch(NullPointerException e)
		{
//			System.out.println(obj);
		}
	}
	
	private void initPool()
	{
//		threadPool = new ThreadPool(10, 1);
//		threadPool.init();
	}
	
	private void initJob()
	{
		UpdateFrame uf = new UpdateFrame(this);
		new Thread(uf,"UpdateFrame").start();
	}


	public void showFrame()
	{	
		setVisible(true);
	}
	
//	public ThreadPool getThreadPool()
//	{
//		return threadPool;
//	}

	public static void main(String[] args) {
		new MainFrame().showFrame();
	}
	
}
