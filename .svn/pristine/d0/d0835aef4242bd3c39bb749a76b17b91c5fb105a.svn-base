//package test;
//
//import java.io.IOException;
//
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//
///**
// * Represents a connection with a queue
// * @author syntx
// *
// */
//public abstract class EndPoint{
//
//    protected Channel channel;
//    protected Connection connection;
//    protected String endPointName;
//
//    public EndPoint(String endpointName) throws IOException{
//         this.endPointName = endpointName;
//
//         //创建链接工厂
//         ConnectionFactory factory = new ConnectionFactory();
//
//         //默认链接的主机名,RabbitMQ-Server安装在本机，所以可以直接用127.0.0.1
//         factory.setHost("localhost");
//         factory.setUsername("admin");
//         factory.setPassword("80932817");
//
//         //创建链接
//         connection = factory.newConnection();
//
//         //创建信息管道
//         channel = connection.createChannel();
//
//         //进行信息声明        1.队列名2.是否持久化，3是否局限与链接，4不再使用是否删除，5其他的属性
//         channel.queueDeclare(endpointName, false, false, false, null);
//    }
//
//
//    /**
//     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
//     * @throws IOException
//     */
//     public void close() throws IOException{
//         this.channel.close();
//         this.connection.close();
//     }
//}