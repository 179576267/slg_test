package com.douqu.game.main.gui.job;

import com.bean.core.util.TimeUtils;
import com.douqu.game.main.gui.MainFrame;
import com.douqu.game.main.gui.component.impl.LabelComponent;
import com.douqu.game.main.GameServer;
import sun.applet.Main;


public class UpdateFrame implements Runnable
{
	
	private MainFrame mainFrame;
	
	private LabelComponent labelComponent;
	
	public UpdateFrame(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		labelComponent = MainFrame.labelComponent;
	}

	public void run()
	{
		int hourTimer = 0;
		int hour = 0;
		
		while(true)
		{
			TimeUtils.sleep(100);
			
			if(!GameServer.getInstance().isRunning())
				continue;
			
			StringBuffer conn1 = new StringBuffer(25);
			conn1.append("Connection:   ");
			conn1.append(GameServer.getInstance().getWorldManager().getPlayerCount());
			conn1.append(" / ");
			conn1.append(GameServer.getInstance().getWorldManager().getConnectionCount());

			labelComponent.lPlayerCount.setText(conn1.toString());
			
			labelComponent.lMaxMem.setText("Memory: "+ Runtime.getRuntime().totalMemory() / 1024 /1024+" mb / " +
					Runtime.getRuntime().maxMemory() / 1024 /1024+" mb");

			labelComponent.lCurrMem.setText("Quick Memory: " + Runtime.getRuntime().freeMemory() / 1024 /1024+" mb");
			
			
			hourTimer++;
			if(hourTimer>=3600)
			{
				hour++;
				labelComponent.lServerTime.setText("Timer:  "+(hour/24)+" day "+(hour%24)+" hour");
				hourTimer = 0;
			}
			
/*			if(showMessage)
			{

			}*/
		}
	}

}
