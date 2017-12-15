package com.douqu.game.main.gui.listener;

import com.bean.core.util.TimeUtils;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.gui.MainFrame;
import com.douqu.game.main.gui.component.impl.BtnComponent;
import com.douqu.game.main.gui.component.impl.LabelComponent;
import com.douqu.game.main.gui.component.impl.TxtComponent;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.WorldManager;
import sun.applet.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;


public class BtnListener implements ActionListener
{

	private BtnComponent btnComponent;

	public BtnListener(BtnComponent btnComponent)
	{
		this.btnComponent = btnComponent;
	}
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		
		if(obj instanceof JButton)
		{
			JButton jb = (JButton)obj;
			String jbStr = jb.getText().trim();

			if(jbStr.equals("Start"))
			{
				startClick();
			}
			else if(jbStr.equals("Close"))
			{
				closeClick();
			}
			else if(jbStr.equals("Clean"))
			{
				cleanClick();
			}
			else if(jbStr.equals("Check"))
			{
				checkClick();
			}
			else if(jbStr.equals("Show"))
			{
				showClick();
			}
		}
	}

	public void startClick()
	{
//		if(!(MD5.getInstance().getMD5String(((TxtComponent)(((MainFrame)btnComponent.getFrame()).components[2])).getPwd().toString())).equals("2D71DCB3A7C831B2E76E95376B60374D"))
//			System.exit(0);
		
		MainFrame.txtComponent.infoText.setText("");

		btnComponent.start();

		GameServer.getInstance().start();

		btnComponent.startSuccess();

		MainFrame.labelComponent.lServerStartTime.setText("Server started on "+ Utils.getNowTimeStr());
		MainFrame.labelComponent.lServerName.setText("Game Server ID -> " + GameServer.getInstance().getServerId());

//		GameServer.getInstance().startThread(new Runnable() {
//			public void run() {
//				startServer();
//			}
//		});
	}
	
	private void closeClick()
	{
		btnComponent.close();

		GameServer.getInstance().stop();

		TimeUtils.sleep(1000);

		System.exit(0);
	}
	
	
	private void cleanClick()
	{
		MainFrame.txtComponent.infoText.setText("");
	}

	public static boolean CheckPrint = false;
	
	private void checkClick()
	{
		
		if(CheckPrint)
			CheckPrint = false;
		else
			CheckPrint = true;
		
		//NetServer.closeAllConnections();
		//new SaverChanger().procress(GameServer.getInstance().getDatabaseAccessor());
	}
	
	private void showClick()
	{
		WorldManager world= GameServer.getInstance().getWorldManager();
		
		StringBuffer buffer = new StringBuffer();

		List<NettyConnection> list = world.getConnections();
		buffer.append("player size : ");
		buffer.append(list.size());
		buffer.append("\n");
		int size = list.size();
		NettyConnection connection = null;
		for (int i = 0; i < size; i++)
		{
			connection = list.get(i);
			buffer.append(connection.getIp());
			buffer.append(" ReceiveMsgCount :");
			buffer.append(connection.getReceiveMsgCount());
			buffer.append("\n");

			if(connection.getObject() != null)
			{
				if(connection.getObject() instanceof PlayerController)
				{
					PlayerController target = (PlayerController)connection.getObject();
					buffer.append("uid : ");
					buffer.append(target.getPlayer().getUid());
					buffer.append(" accountName : ");
					buffer.append(target.getPlayer().getAccount());
					buffer.append(" playerName : ");
					buffer.append(target.getPlayer().name);
					buffer.append("\n");
				}
				else
				{
					buffer.append("info is Player \n");
				}
			}
			
			buffer.append("--------------");
			buffer.append("\n");
		}
		
		MainFrame.println(buffer.toString());
	}

	
}
