package com.douqu.game.core.util;

import com.bean.core.util.MD5Utils;
import com.douqu.game.core.protobuf.SGCommonProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/11/9.
 */
public class Utils {
    public static final int ONT_DAT = 24 * 60 * 60 * 1000;


//    public static boolean isErrorValue(String value)
//    {
//        return StringUtils.isEmpty(value) || "0".equals(value);
//    }


    public static <T> T getMaxGameObject(List<T> list, Comparator comparator)
    {
        if(list == null || list.size() == 0)
            return null;

        if(list.size() == 1)
            return list.get(0);

        return (T) Collections.max(list, comparator);
    }

    public static <T> T getMinGameObject(List<T> list, Comparator comparator)
    {
        if(list == null || list.size() == 0)
            return null;

        if(list.size() == 1)
            return list.get(0);

        return (T) Collections.min(list, comparator);
    }

    public static boolean isNullOrEmpty(String text){
        return text == null || text.equals("") || text.replaceAll(" ","").length() == 0;
    }

    public static boolean isErrorValue(String value)
    {
        return "0".equals(value) || isNullOrEmpty(value);
    }


    public static <T>boolean arrayContainsValue(T[] arr, T obj){
        boolean has = false;
        for(T t : arr){
            if(t == obj){
                has = true;
                break;
            }
        }
        return has;
    }




    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new CopyOnWriteArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }

    /**
     * 获得内网IP
     * @return 内网IP
     */
    public static String getIntranetIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    /**
     * 获得外网IP
     * @return 外网IP
     */
    public static String getInternetIp(){
        try{
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            Enumeration<InetAddress> addrs;

            String INTRANET_IP = getIntranetIp();
            while (networks.hasMoreElements())
            {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    ip = addrs.nextElement();

                    if (ip != null
                            && ip instanceof Inet4Address
                            && ip.isSiteLocalAddress()
                            && !ip.getHostAddress().equals(INTRANET_IP))
                    {
                        return ip.getHostAddress();
                    }
                }
            }

            // 如果没有外网IP，就返回内网IP
            return INTRANET_IP;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }



    public static String getChannelSuff(SGCommonProto.E_CHANNEL_TYPE channel)
    {
        String name = "";
        if(channel == null)
            name = SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_UNKNOW.name();
        else
            name = channel.name();
        return name.substring(name.lastIndexOf("_")+1);
    }

    /**
     * 根据平台生成账号
     * @param account
     * @param channel
     * @return
     */
    public static String createAccount(String account, SGCommonProto.E_CHANNEL_TYPE channel)
    {
        return getChannelSuff(channel) + "_" + account;
    }

    /**
     * 根据平台生成账号
     * @param account
     * @param channel
     * @return
     */
    public static String createPlayerIndex(String account, SGCommonProto.E_CHANNEL_TYPE channel)
    {
        return getChannelSuff(channel) + "_" + account + "_" + System.currentTimeMillis() / 1000;
    }


    /**
     * md5比较
     * @param param 原始参数　
     * @param md5Param 需要转md5再比较的参数
     * @return
     */
    public static boolean verifyMD5(String param, String md5Param)
    {
        return param.toUpperCase().equals(MD5Utils.encodeUppercase(md5Param));
    }

    public static String getRootPath()
    {
        return Utils.class.getResource("/").getPath();
    }

    public static String getBasePath()
    {
        return System.getProperty("user.dir") + "/";
    }

    public static Integer getRandomNum(int num)
    {
        int r = 9;
        int a = 1;
        switch (num){
            case 1 : r = 9;      a = 1;      break;
            case 2 : r = 90;     a = 10;     break;
            case 3 : r = 900;    a = 100;    break;
            case 4 : r = 9000;   a = 1000;   break;
            case 5 : r = 90000;  a = 10000;  break;
            case 6 : r = 900000; a = 100000; break;
        }

        return (int) (Math.random() * r) + a;
    }

    public static String createUUID(int num)
    {
        String[] strs = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s",
                "t","u","v","w","x","y","z","0","1","2","3","4","5","6","7","8","9"};
//        String time = System.currentTimeMillis()+"";
        StringBuffer sb = new StringBuffer();
        int r = 0;
        for (int i = 0; i < num; i++)
        {
            r = (int) (Math.random()*strs.length);
            sb.append(strs[r]);
        }
//        sb.append(time.substring(time.length()-3));
        return sb.toString();
    }



    public static byte[] byteBufToBytes(ByteBuf byteBuf)
    {
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        return bytes;
    }


//    public static byte[] byteBufToBytes(ByteBuf byteBuf)
//    {
//        byte[] bytes = new byte[byteBuf.readableBytes()];
//        for(int i = 0; i < bytes.length; i++)
//        {
//            bytes[i] = byteBuf.readByte();
//        }
//
//        return bytes;
//    }
    // java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2)
    {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] shortToByteArray(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }


    public static String getFormatTime(String format, long time)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }


    public static String getNowTimeStr()
    {
        return getFormatTime("yyyy-MM-dd HH:mm:ss",getNowTime());
    }

    public static boolean inDays(Date date, int days)
    {
        date = date == null ?new Date() :date;
        return date.getTime() < getNowTime() + days * ONT_DAT;

    }

    public static long getNowTime()
    {
        return System.currentTimeMillis();
    }
    /*
	 * 从ByteBuf中获取信息 使用UTF-8编码返回
	 */
    public static String getMessage(ByteBuf buf) {
//        System.out.println("总长度:"+buf.readableBytes());
//        int len = buf.readInt();
//        System.out.println("读取数据长度:"+len);
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ByteBuf getSendByteBuf(String message) throws UnsupportedEncodingException {

        byte[] req = message.getBytes("UTF-8");
        ByteBuf pingMessage = Unpooled.buffer();
//        pingMessage.writeInt(req.length);
        pingMessage.writeBytes(req);

        return pingMessage;
    }



    public static List<Integer> getRandomInteger(int num, int maxValue){
        if(num == 0 || maxValue == 0 || num > maxValue){
            return null;
        }
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        while(list.size() < num){
            int product = random.nextInt(maxValue);
            if(list.contains(product)){
                continue;
            }
            else {
                list.add(product);
            }
        }
        return list;
    }


    public static <T> List<T> arrayToList(T[] data){
        List<T> list = new ArrayList<T>();
        if(data != null){
            for(T t : data){
                list.add(t);
            }
        }
        return list;
    }


    public static <T> void listToArray(List<T> list, T[] data){
        if(list == null || data == null || list.size() != data.length){
            return;
        }

        int size = list.size();
        for(int i = 0; i < size; i++){
            data[i] = list.get(i);
        }
    }


    /**
     * 获取两个时间的间隔天数
     * @param timeNow
     * @param timeAfter
     * @return
     */
    public static int getBetweenDay(long timeNow, long timeAfter){
        if(timeAfter < timeNow){
            return 0;
        }
        long between = (timeAfter - timeNow) / 1000;//除以1000是为了转换成秒
        int day = (int) (between / (24 * 3600));
        day = between % (24 * 3600) > 0 ? day + 1: day;
        return day;
    }

}
