package org.wenchen.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class MqttClient {
    private final String host;
    private final int port;

    public MqttClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new MqttClient("127.0.0.1", 1883).start();
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                            pipeline.addLast("decoder", new MqttDecoder());
                            pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                            // 添加字符串编解码器
                            // pipeline.addLast("decoder", new StringDecoder());
                            // pipeline.addLast("encoder", new StringEncoder());

                            // 添加 MQTT 编码器
                            // pipeline.addLast(MqttEncoder.INSTANCE);
                            // 自定义处理器
                            pipeline.addLast(new MqttClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("MQTT 客户端成功连接到服务器: " + host + ":" + port);
            Channel channel = future.channel();//.closeFuture().sync().channel();
            channel.writeAndFlush("hello world");
            ChannelFuture channelFuture = channel.closeFuture();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.info("MQTT 客户端已关闭");
                }
            });

        } finally {
            group.shutdownGracefully();
            System.out.println("MQTT 客户端已关闭");
        }
    }
}

class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private static final Logger log = LoggerFactory.getLogger(MqttServer.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("正在发送 CONNECT 消息...");

        MqttConnectMessage connectMessage = new MqttConnectMessage(
                new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnectVariableHeader("MQTT", 4, true, true, false, 0, false, false, 60),
                new MqttConnectPayload("mqtt_client", "test", "密码".getBytes(), "用户名", "密码".getBytes())
        );

        ctx.writeAndFlush(connectMessage);
        System.out.println("CONNECT 消息已发送，等待服务器响应...");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws InterruptedException {
        MqttMessageType messageType = msg.fixedHeader().messageType();
        switch (messageType) {
            case CONNACK -> handleConnAck(ctx);
            case PUBACK -> handlePubAck();
            case PUBLISH -> handlePublish(ctx, (MqttPublishMessage) msg);
            default -> System.out.println("收到未处理的消息类型: " + messageType);
        }
    }

    private void handlePublish(ChannelHandlerContext ctx, MqttPublishMessage publishMessage) {
        String topic = publishMessage.variableHeader().topicName();
        String payload = publishMessage.payload().toString(io.netty.util.CharsetUtil.UTF_8);
        log.info("收到发布消息，主题: {}, 内容: {}", topic, payload);

        // 处理收到的发布消息，进行业务逻辑处理
        // 比如，可以广播给其他订阅该主题的客户端

        MqttPubAckMessage pubAckMessage = new MqttPubAckMessage(
                new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(publishMessage.variableHeader().packetId())
        );

        ctx.writeAndFlush(pubAckMessage);
        ctx.writeAndFlush("你好999999");
        log.info("发布确认消息已发送 (PUBACK)");
    }

    private void handleConnAck(ChannelHandlerContext ctx) throws InterruptedException {
        System.out.println("收到 CONNACK 消息：连接成功！");

        // 发送 PUBLISH 消息
        System.out.println("正在发送 PUBLISH 消息...");
        Long cor = 0L;
        while (cor++ <= 1L) {
            String topic = "test/topic";
            String payload = "你好，MQTT！" + Math.random() * 100;
            MqttPublishMessage publishMessage = new MqttPublishMessage(
                    new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0),
                    new MqttPublishVariableHeader(topic, 1),
                    io.netty.buffer.Unpooled.copiedBuffer(payload, io.netty.util.CharsetUtil.UTF_8)
            );
            ctx.writeAndFlush(publishMessage);
            System.out.println("PUBLISH 消息已发送，主题: " + topic + "，内容: " + payload);
            Thread.sleep(1000);
        }
    }

    private void handlePubAck() {
        System.out.println("收到 PUBACK 消息：服务器已确认消息发布。");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("发生异常: " + cause.getMessage());
        cause.printStackTrace();
        ctx.close();
    }
}
