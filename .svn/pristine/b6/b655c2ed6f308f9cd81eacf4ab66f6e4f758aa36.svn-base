//package test;
//
//import java.io.IOException;
//import java.io.Serializable;
//
//import org.apache.commons.lang.SerializationUtils;
//
//
///**
// * The producer endpoint that writes to the queue.
// * @author syntx
// *
// */
//public class Producer extends EndPoint{
//
//	public Producer(String endPointName) throws IOException{
//		super(endPointName);
//	}
//
//	public void sendMessage(Serializable object) throws IOException {
//		//发送消息
//		// 在RabbitMQ中，消息是不能直接发送到队列，它需要发送到交换器（exchange）中。
//		// 第一参数空表示使用默认exchange，第二参数表示发送到的queue，第三参数是发送的消息是（字节数组）
//	    channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
//	}
//}