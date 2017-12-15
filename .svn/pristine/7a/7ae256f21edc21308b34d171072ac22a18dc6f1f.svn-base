package com.douqu.game.main.job;


import com.douqu.game.core.controller.PlayerController;

/**
 * 需要确定的
 */
public class ConfirmJob
{
	protected int lifeTime;
	
	protected String name;
	
	protected PlayerController source;

	public ConfirmJob(int lifeTime)
	{
		this.lifeTime = lifeTime;
	}

	public void confirm(boolean accepted)
	{

	}

	
	public boolean isAlive()
	{
		return lifeTime > 0;
	}
	
	public void setLifeTime(int lifeTime)
	{
		this.lifeTime = lifeTime;
	}
	
	public int getLifeTime()
	{
		return lifeTime;
	}
	
	public void cancel()
	{
		lifeTime = 0;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public PlayerController getSource()
	{
		return source;
	}

	public void setSource(PlayerController source)
	{
		this.source = source;
	}


	
	
	
}
