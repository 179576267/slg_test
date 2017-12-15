package com.douqu.game.main.event;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by bean on 2017/7/18.
 */
public class EventManager {

    private List listeners;

    public void addListener(EventListener listener)
    {
        if(listeners == null)
            listeners = new ArrayList<>();

        Class cls = listener.getClass();

        listeners.remove(listener);

        listeners.add(listener);
    }

    public void removeListener(EventListener listener)
    {
        if(listeners == null)
            return;

        listeners.remove(listener);
    }


    public void notifyListeners(Object source)
    {
        SaveEvent myEvent = null;
        for (Object obj : listeners)
        {
            System.out.println(obj);
            if (obj instanceof SavePlayerListener)
            {
                myEvent = new SaveEvent(source,"i:"+obj);
                ((SavePlayerListener)obj).saveEvent(myEvent);
            }
        }
    }

    public void savePlayer(Object source)
    {
        System.out.println("保存玩家信息");

        notifyListeners(source);
    }

    public static void main(String[] args) {
        EventManager saveManager = new EventManager();
        SavePlayerListener listener = new SavePlayerListener();
        saveManager.addListener(listener);

//        saveManager.savePlayer(new Player());
    }
}
