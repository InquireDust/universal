package org.wenchen.demo.netty.demo.company;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * CustomMQTTClient 类用于创建和管理一个MQTT客户端，该客户端可以连接到MQTT代理（broker）并进行消息发布和接收
 */
public class CustomMQTTClient {

    // MQTT代理的主机地址
    private static final String BROKER_HOST = "106.14.139.85";
    // MQTT代理的端口号
    private static final int BROKER_PORT = 1883;
    // MQTT客户端要订阅的主题
    private static final String TOPIC = "example/topic";
    // MQTT客户端的唯一标识符
    private static final String CLIENT_ID = "NettyMQTTClient";
    // 连接MQTT代理时使用的用户名
    private static final String USERNAME = "admin";
    // 连接MQTT代理时使用的密码
    private static final String PASSWORD = "Aa@123456";

    // 用于与MQTT代理通信的网络通道
    private Channel channel;

    /**
     * 主函数，用于创建并启动CustomMQTTClient实例
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        CustomMQTTClient client = new CustomMQTTClient();
        client.start();
    }

    /**
     * 启动客户端，连接到MQTT Broker并进行消息通信
     */
    public void start() {
        // 创建EventLoopGroup以处理I/O操作
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 初始化Bootstrap以配置客户端
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            // 获取ChannelPipeline以添加处理器
                            ChannelPipeline pipeline = ch.pipeline();
                            // 添加MQTT编码器
                            pipeline.addLast("mqttEncoder", MqttEncoder.INSTANCE);
                            // 添加MQTT解码器
                            pipeline.addLast("mqttDecoder", new MqttDecoder());
                            // 添加处理MQTT消息的处理器
                            pipeline.addLast("mqttHandler", new SimpleChannelInboundHandler<MqttMessage>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
                                    // 处理接收到的PUBLISH消息
                                    if (msg.fixedHeader().messageType() == MqttMessageType.PUBLISH) {
                                        MqttPublishMessage publishMessage = (MqttPublishMessage) msg;
                                        String topic = publishMessage.variableHeader().topicName();
                                        String payload = publishMessage.payload().toString(io.netty.util.CharsetUtil.UTF_8);
                                        System.out.println("收到消息，主题: " + topic + "，内容: " + payload);

                                        // 自动确认消息
                                        MqttMessage pubAckMessage = MqttMessageFactory.newMessage(
                                                new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_LEAST_ONCE, false, 0),
                                                MqttMessageIdVariableHeader.from(publishMessage.variableHeader().packetId()),
                                                null
                                        );
                                        ctx.writeAndFlush(pubAckMessage);
                                    }
                                }
                            });
                            // 添加自定义的出站处理器
                            pipeline.addLast("white", new ChannelOutboundHandlerAdapter() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    super.write(ctx, msg, promise);
                                }
                            });
                            // 添加自定义的入站处理器
                            pipeline.addLast("read", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    });
    
            // 连接到MQTT Broker
            ChannelFuture future = bootstrap.connect(BROKER_HOST, BROKER_PORT).sync();
            this.channel = future.channel();
            System.out.println("成功连接到 MQTT Broker: " + BROKER_HOST);
    
            // 发送 CONNECT 报文
            sendConnect();
    
            // 订阅主题
            subscribeToTopic(TOPIC);
    
            // 定时任务发布消息
            startPublishing(TOPIC);
    
            // 等待通道关闭
            channel.closeFuture().sync();
    
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭EventLoopGroup以释放资源
            group.shutdownGracefully();
        }
    }

    /**
     * 发送连接请求到MQTT服务器
     * 此方法构造MQTT CONNECT报文，并通过通道发送到服务器
     * CONNECT报文包含客户端ID、主题、用户名和密码等信息
     */
    private void sendConnect() {
        // 创建CONNECT报文的负载部分，包含客户端ID、主题、测试消息、用户名和密码
        MqttConnectPayload payload = new MqttConnectPayload(
                CLIENT_ID, TOPIC, "测试测试", USERNAME, PASSWORD
        );
        // 创建CONNECT报文的可变头部，指定MQTT协议版本、连接标志等
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(
                "MQTT", 4, true, true, false, 0, false, false, 60
        );
        // 创建CONNECT报文的固定头部，设置报文类型、QoS等级和保留位等
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 10);
        // 组合固定头部、可变头部和负载创建完整的CONNECT报文
        MqttMessage connectMessage = new MqttConnectMessage(fixedHeader, variableHeader, payload);
    
        // 通过通道发送CONNECT报文到服务器
        channel.writeAndFlush(connectMessage);
        // 打印发送CONNECT报文的信息
        System.out.println("发送 CONNECT 报文");
    }
    
    /**
     * 订阅指定的主题
     * 此方法构造MQTT SUBSCRIBE报文，并通过通道发送以订阅指定的主题
     * 
     * @param topic 要订阅的主题名称
     */
    private void subscribeToTopic(String topic) {
        // 创建SUBSCRIBE报文的固定头部，设置报文类型、QoS等级和保留位等
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 10);
        // 创建SUBSCRIBE报文的可变头部，包含报文ID
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(1);
        // 创建SUBSCRIBE报文的负载部分，包含要订阅的主题和QoS等级
        MqttSubscribePayload payload = new MqttSubscribePayload(
                java.util.Collections.singletonList(new MqttTopicSubscription(topic, MqttQoS.AT_LEAST_ONCE))
        );
    
        // 组合固定头部、可变头部和负载创建完整的SUBSCRIBE报文
        MqttMessage subscribeMessage = new MqttSubscribeMessage(fixedHeader, variableHeader, payload);
        // 通过通道发送SUBSCRIBE报文到服务器
        channel.writeAndFlush(subscribeMessage);
        // 打印订阅主题的信息
        System.out.println("订阅主题: " + topic);
    }

    /**
     * 开始发布消息到指定主题
     * 此方法使用定时任务定期生成消息，并将其发布到MQTT代理
     * 
     * @param topic MQTT主题，消息将发布到该主题
     */
    private void startPublishing(String topic) {
        // 创建一个单线程的定时调度池
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        // 定期执行消息发布任务，初始延迟为0毫秒，之后每10秒执行一次
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // 构造消息内容，包含发送时间
                String messageContent = "Hello from NettyMQTTClient at " + System.currentTimeMillis();
                // 将消息内容转换为字节数组
                byte[] payload = messageContent.getBytes();
                
                // 创建MQTT消息的固定头部，设置消息类型为PUBLISH，QoS等级为至少一次
                MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0);
                // 创建MQTT消息的可变头部，设置主题和消息ID
                MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(topic, 1);
                
                // 创建并组装MQTT发布消息
                MqttPublishMessage publishMessage = new MqttPublishMessage(fixedHeader, variableHeader, io.netty.buffer.Unpooled.wrappedBuffer(payload));
                // 将消息写入并冲刷到通道，以确保消息被发送
                channel.writeAndFlush(publishMessage);
                // 打印发送的消息内容
                System.out.println("发送消息: " + messageContent);
            } catch (Exception e) {
                // 打印异常堆栈跟踪信息
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS); // 每 1 毫秒发布一次消息
    }
}
