package org.wenchen.demo.netty.demo.company;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomMQTTClient {

    private static final String BROKER_HOST = "106.14.139.85";
    private static final int BROKER_PORT = 1883;
    private static final String TOPIC = "example/topic";
    private static final String CLIENT_ID = "NettyMQTTClient";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "Aa@123456";

    private Channel channel;

    public static void main(String[] args) {
        CustomMQTTClient client = new CustomMQTTClient();
        client.start();
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("mqttEncoder", MqttEncoder.INSTANCE);
                            pipeline.addLast("mqttDecoder", new MqttDecoder());
                            pipeline.addLast("mqttHandler", new SimpleChannelInboundHandler<MqttMessage>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
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
                            pipeline.addLast("white", new ChannelOutboundHandlerAdapter() {
                                @Override
                                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                    super.write(ctx, msg, promise);
                                }
                            });
                            pipeline.addLast("read", new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    super.channelRead(ctx, msg);
                                }
                            });
                        }
                    });

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
            group.shutdownGracefully();
        }
    }

    private void sendConnect() {
        MqttConnectPayload payload = new MqttConnectPayload(
                CLIENT_ID, TOPIC, "测试测试", USERNAME, PASSWORD
        );
        MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(
                "MQTT", 4, true, true, false, 0, false, false, 60
        );
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 10);
        MqttMessage connectMessage = new MqttConnectMessage(fixedHeader, variableHeader, payload);

        channel.writeAndFlush(connectMessage);
        System.out.println("发送 CONNECT 报文");
    }

    private void subscribeToTopic(String topic) {
        MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 10);
        MqttMessageIdVariableHeader variableHeader = MqttMessageIdVariableHeader.from(1);
        MqttSubscribePayload payload = new MqttSubscribePayload(
                java.util.Collections.singletonList(new MqttTopicSubscription(topic, MqttQoS.AT_LEAST_ONCE))
        );

        MqttMessage subscribeMessage = new MqttSubscribeMessage(fixedHeader, variableHeader, payload);
        channel.writeAndFlush(subscribeMessage);
        System.out.println("订阅主题: " + topic);
    }

    private void startPublishing(String topic) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                String messageContent = "Hello from NettyMQTTClient at " + System.currentTimeMillis();
                byte[] payload = messageContent.getBytes();

                MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0);
                MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader(topic, 1);

                MqttPublishMessage publishMessage = new MqttPublishMessage(fixedHeader, variableHeader, io.netty.buffer.Unpooled.wrappedBuffer(payload));
                channel.writeAndFlush(publishMessage);
                System.out.println("发送消息: " + messageContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS); // 每 10 秒发布一次消息
    }
}
