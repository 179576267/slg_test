package com.douqu.game.core.container;


import com.douqu.game.core.controller.PlayerController;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
* 主角容器   容在区域房间战斗
* @author bean
*
*/
public class PlayerContainer implements Serializable
{
	protected static final long serialVersionUID = 1L;

	protected List<PlayerController> playerList = new CopyOnWriteArrayList();


	public List getPlayerList()
	{
		return playerList;
	}


	public PlayerController getPlayerByIndex(int index)
	{
		return (PlayerController)playerList.get(index);
	}


	public int getPlayerCount()
	{
		return playerList.size();
	}

	public synchronized void addPlayer(PlayerController target)
	{
		playerList.add(target);
	}

	public synchronized void removePlayer(PlayerController target)
	{
		playerList.remove(target);
	}

	public PlayerController getPlayer(String playerIndex)
	{
		for(PlayerController playerController : playerList)
		{
			if(playerController.getObjectIndex().equals(playerIndex))
				return playerController;
		}

		return null;
	}

	/**
	 * 转发容器内所有的消息
	 * @param
	 */
	public void dispatchMsg(int code, byte[] data)
	{
		for(PlayerController playerController : playerList)
		{
			playerController.sendMsg(code, data);
		}
	}


	public void update(long currentMillis)
	{
		for(PlayerController playerController : playerList)
		{
			playerController.update(currentMillis);
		}
	}





}
