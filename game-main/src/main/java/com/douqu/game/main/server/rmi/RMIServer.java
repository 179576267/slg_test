package com.douqu.game.main.server.rmi;

import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.i.IMainServer;
import com.douqu.game.core.i.IServer;
import com.douqu.game.main.gui.MainFrame;
import com.douqu.game.main.util.PrintUtils;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.MessageFormat;

/**
* Created by bean
* 2017-03-24 12:26.
*/
public class RMIServer implements IServer{

    Logger logger = Logger.getLogger(RMIServer.class);

    @Override
    public void start()
    {
        try {
            IMainServer gameManager = new MainServerImpl();

            LocateRegistry.createRegistry(ConfigFactory.RMI_SEVER_PORT);

            String url = MessageFormat.format(ConfigFactory.RMI_SERVER_URL,ConfigFactory.RMI_SEVER_HOST ,ConfigFactory.RMI_SEVER_PORT+"", ConfigFactory.RMI_SERVER_NAME);

            Naming.bind(url, gameManager);

            PrintUtils.info(logger, "Start RMI Server Success ------ 【" + url + "】");
        } catch (RemoteException e) {
            System.out.println("创建远程对象发生异常！");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("发生重复绑定对象异常！");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("发生URL畸形异常！");
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void stop()
    {
        try {

            String url = MessageFormat.format(ConfigFactory.RMI_SERVER_URL,ConfigFactory.RMI_SEVER_HOST ,ConfigFactory.RMI_SEVER_PORT+"", ConfigFactory.RMI_SERVER_NAME);

            Naming.unbind(url);

            logger.info("Stop RMI Server Success ------ " + url);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new RMIServer().start();
    }

}
