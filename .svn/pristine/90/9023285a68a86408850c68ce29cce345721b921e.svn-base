package com.douqu.game.main.gui.component.impl;

import com.douqu.game.main.gui.MainFrame;
import com.douqu.game.main.gui.component.i.Component;
import com.douqu.game.main.gui.listener.BtnListener;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;


public class BtnComponent extends Component
{

	private Container container;
	
	public void setJFrame(JFrame frame)
	{
		super.frame = frame;
		container = frame.getContentPane();
	}

	private JButton bStart;
	
	private JButton bCheck;
	
	private JButton bClose;
	
	private JButton bClean;
	
	private JButton bShow;
	
	public BtnComponent()
	{
		Font font = new Font("TimesRoman",Font.BOLD,15);
		
		bStart = new JButton("Start");
		bStart.setFont(font);
		bStart.setBounds(20,MainFrame.HEIGHT-80,85,30);
		
		bCheck = new JButton("Check");
		bCheck.setBounds(120,MainFrame.HEIGHT-80,85,30);
		bCheck.setFont(font);
		
		bClose = new JButton("Close");
		bClose.setBounds(220,MainFrame.HEIGHT-80,85,30);
		bClose.setFont(font);
			
		bClean = new JButton("Clean");
		bClean.setFont(font);
		bClean.setBounds(320,MainFrame.HEIGHT-80,85,30);
		
		bShow = new JButton("Show");
		bShow.setFont(font);
		bShow.setBounds(420,MainFrame.HEIGHT-80,85,30);

		init();
	}
	
	public void addToFrame()
	{
		bStart.addActionListener(new BtnListener(this));
		bCheck.addActionListener(new BtnListener(this));
		bClose.addActionListener(new BtnListener(this));
		bClean.addActionListener(new BtnListener(this));
		bShow.addActionListener(new BtnListener(this));
		
		container.add(bStart);
		container.add(bCheck);
		container.add(bClose);
		container.add(bClean);
		container.add(bShow);
	}

	public void init()
	{
		bStart.setEnabled(true);
		bCheck.setEnabled(false);
		bClose.setEnabled(false);
		bClean.setEnabled(false);
		bShow.setEnabled(false);
	}


	public void close()
	{
		bStart.setEnabled(false);
		bCheck.setEnabled(false);
		bClose.setEnabled(false);
		bClean.setEnabled(false);
		bShow.setEnabled(false);
	}

	public void start()
	{
		bStart.setEnabled(false);
	}

	public void startSuccess()
	{
		bStart.setEnabled(false);
		bCheck.setEnabled(true);
		bClose.setEnabled(true);
		bClean.setEnabled(true);
		bShow.setEnabled(true);
	}
	
	
	public JButton getBStart()
	{
		return bStart;
	}

	public JButton getBCheck()
	{
		return bCheck;
	}

	public JButton getBClose()
	{
		return bClose;
	}

	public JButton getBClean()
	{
		return bClean;
	}

	public JButton getBShow()
	{
		return bShow;
	}
	
//	public JButton getBVisible()
//	{
//		return bVisible;
//	}

	public JFrame getFrame()
	{
		return frame;
	}
}
