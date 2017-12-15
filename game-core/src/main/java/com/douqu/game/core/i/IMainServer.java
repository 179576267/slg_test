package com.douqu.game.core.i;

import com.douqu.game.core.entity.battle.BattleDetail;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.DataFactory;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Author : Bean
 * 2017-07-07 9:55
 */
public interface IMainServer extends Remote {

   public void battleStart(int serverId, BattleDetail battleInfo) throws RemoteException;

   public void battleEnd(int serverId, BattleDetail battleInfo) throws Exception;

   public void updateStatus(int serverId, String playerIndex, String status) throws RemoteException;

   public Player getOnlinePlayer(String playerIndex) throws RemoteException;

   public Player getOfflinePlayer(String playerIndex) throws RemoteException;

   public DataFactory getDataFactory() throws RemoteException;
}
