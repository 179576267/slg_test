package com.douqu.game.core.netty;

import com.douqu.game.core.e.E_ServerType;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

/**
 * Created by Administrator on 2017/3/15.
 */
public class NettyConnection
{
    private int id;

    private String ip;

    private Channel channel;

    private E_ServerType serverType;

    private Object object;

    private int receiveMsgCount;

    private int sendMsgCount;

    /**
     * 是否是服务器主动断开
     */
    private boolean serverDestroy;

    private long lastUpdateTime;

    public NettyConnection(Channel channel)
    {
        this.id = hashCode();
        this.channel = channel;

        if(channel != null)
        {
            InetSocketAddress address = ((InetSocketAddress) channel.remoteAddress());
            this.ip = address.getAddress().getHostAddress();
        }
    }

    public void sendAlert(SGCommonProto.E_NOTIFY_TYPE notifyType, String content)
    {
        if(channel == null)
            return;

        SGSystemProto.S2C_NotifyAlert.Builder response = SGSystemProto.S2C_NotifyAlert.newBuilder();
        response.setType(notifyType);
        response.setContent(content==null?"":content);
        sendMsg(SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE, response.build().toByteArray());
    }

    public void sendMsg(int msgID, byte[] data)
    {
        final ByteBuf byteBuf = Unpooled.buffer();

        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.LITTLE_ENDIAN.toString())){
            byteBuf.writeIntLE(data==null?0:data.length);//总包长
            byteBuf.writeShortLE(msgID);//长度为2
        }else{
            byteBuf.writeInt(data==null?0:data.length);//总包长
            byteBuf.writeShort(msgID);//长度为2
        }

        if(data != null)
            byteBuf.writeBytes(data);

        ChannelFuture cf = channel.writeAndFlush(byteBuf);
//        cf.addListener(new ChannelFutureListener(){
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                System.out.println("发送完成..."+byteBuf.readableBytes());
//            }
//        });

        sendMsgCount++;
    }

    public void receiveMsg()
    {
        receiveMsgCount++;
    }

    public void destroy()
    {
        if(channel == null)
            return;

        channel.close();

        object = null;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public E_ServerType getServerType() {
        return serverType;
    }

    public void setServerType(E_ServerType serverType) {
        this.serverType = serverType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getReceiveMsgCount() {
        return receiveMsgCount;
    }

    public int getSendMsgCount() {
        return sendMsgCount;
    }

    public boolean isServerDestroy() {
        return serverDestroy;
    }

    public void setServerDestroy(boolean serverDestroy) {
        this.serverDestroy = serverDestroy;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NettyConnection)) return false;

        NettyConnection that = (NettyConnection) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "NettyConnection{" +
                "id=" + id +
                ", channel=" + channel +
                ", serverType=" + serverType +
                ", serverDestroy=" + serverDestroy +
                ", object=" + object +
                '}';
    }
}
