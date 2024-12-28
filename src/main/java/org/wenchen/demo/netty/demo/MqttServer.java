package org.wenchen.demo.netty.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttServer {
    private static final Logger log = LoggerFactory.getLogger(MqttServer.class);
    private final int port;

    public MqttServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        new MqttServer(1883).start(); // 默认 MQTT 端口
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
                            // 添加 MQTT 解码器和编码器
                            pipeline.addLast("decoder", new MqttDecoder());
                            pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                            pipeline.addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    if (msg instanceof MqttMessage mqttMessage) {
                                        MqttMessageType messageType = mqttMessage.fixedHeader().messageType();
                                        log.info("收到消息，类型: {}", messageType);
                                    }
                                    ctx.fireChannelRead(msg);
                                }
                            });
                            // 添加字符串编解码器
                            // pipeline.addLast("decoder", new StringDecoder());
                            // pipeline.addLast("encoder", new StringEncoder());
                            // 自定义处理器
                            pipeline.addLast(new MqttServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("MQTT 服务器启动成功，监听端口: {}", port);
            // 关闭等待
            future.channel().closeFuture().sync();
//            future.sync().channel().closeFuture().addListener(closeFuture -> {
//                bossGroup.shutdownGracefully();
//                workerGroup.shutdownGracefully();
//                log.info("MQTT 服务器已关闭");
//            });
        } finally {
            if(bossGroup.isShuttingDown()) {
                bossGroup.shutdownGracefully();
            }
            if(workerGroup.isShuttingDown()) {
                workerGroup.shutdownGracefully();
            }
            log.info("MQTT 服务器已关闭");
        }
    }
}

class MqttServerHandler extends SimpleChannelInboundHandler<MqttMessage> {
    private static final Logger log = LoggerFactory.getLogger(MqttServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
        MqttMessageType messageType = msg.fixedHeader().messageType();
        log.info("收到消息，类型: {}", messageType);

        switch (messageType) {
            case CONNECT -> handleConnect(ctx, (MqttConnectMessage) msg);
            case PUBLISH -> handlePublish(ctx, (MqttPublishMessage) msg);
//            case DISCONNECT -> handleDisconnect(ctx, (MqttDisconnectMessage) msg);
//            case SUBSCRIBE -> handleSubscribe(ctx, (MqttSubscribeMessage) msg);
//            case UNSUBSCRIBE -> handleUnsubscribe(ctx, (MqttUnsubscribeMessage) msg);
            default -> log.warn("未处理的消息类型: {}", messageType);
        }
    }

    private void handleConnect(ChannelHandlerContext ctx, MqttConnectMessage connectMessage) {
        log.info("客户端尝试连接，地址: {}", ctx.channel().remoteAddress());
        log.info("客户端标识: {}", connectMessage.payload().clientIdentifier());

        // 进行连接认证或者更多逻辑（如认证检查）
        boolean isAuthenticated = true;  // 这里可以替换为实际的认证逻辑

        MqttConnAckMessage connAckMessage;
        if (isAuthenticated) {
            // 认证通过
            connAckMessage = new MqttConnAckMessage(
                    new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false)
            );
        } else {
            // 认证失败
            connAckMessage = new MqttConnAckMessage(
                    new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                    new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USER_NAME_OR_PASSWORD, false)
            );
        }

        ctx.writeAndFlush(connAckMessage);
        log.info("连接响应已发送 (CONNACK)，认证结果: {}", isAuthenticated ? "成功" : "失败");
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

        MqttPublishMessage response = new MqttPublishMessage(
                new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 0),
                new MqttPublishVariableHeader(topic, 1),
                io.netty.buffer.Unpooled.copiedBuffer(payload, io.netty.util.CharsetUtil.UTF_8)
        );
        ctx.writeAndFlush(response);
        System.out.println("PUBLISH 消息已发送，主题: " + topic + "，内容: " + payload);

        ctx.writeAndFlush(pubAckMessage);
        log.info("发布确认消息已发送 (PUBACK)");
    }

//    private void handleDisconnect(ChannelHandlerContext ctx, MqttDisconnectMessage disconnectMessage) {
//        log.info("客户端主动断开连接，地址: {}", ctx.channel().remoteAddress());
//        // 在这里可以进行清理工作，移除连接等
//    }

//    private void handleSubscribe(ChannelHandlerContext ctx, MqttSubscribeMessage subscribeMessage) {
//        // 处理订阅消息
//        log.info("收到订阅消息，客户端: {}, 主题: {}", ctx.channel().remoteAddress(), subscribeMessage.payload().topicSubscriptions());
//
//        // 返回订阅确认
//        MqttSubAckMessage subAckMessage = new MqttSubAckMessage(
//                new MqttFixedHeader(MqttMessageType.SUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
//                MqttMessageIdVariableHeader.from(subscribeMessage.variableHeader().packetId()),
//                new MqttSubAckPayload(MqttQoS.AT_MOST_ONCE)
//        );
//        ctx.writeAndFlush(subAckMessage);
//        log.info("订阅确认消息已发送 (SUBACK)");
//    }

//    private void handleUnsubscribe(ChannelHandlerContext ctx, MqttUnsubscribeMessage unsubscribeMessage) {
//        // 处理取消订阅消息
//        log.info("收到取消订阅消息，客户端: {}, 主题: {}", ctx.channel().remoteAddress(), unsubscribeMessage.payload().topics());
//
//        // 返回取消订阅确认
//        MqttUnsubAckMessage unsubAckMessage = new MqttUnsubAckMessage(
//                new MqttFixedHeader(MqttMessageType.UNSUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
//                MqttMessageIdVariableHeader.from(unsubscribeMessage.variableHeader().packetId())
//        );
//        ctx.writeAndFlush(unsubAckMessage);
//        log.info("取消订阅确认消息已发送 (UNSUBACK)");
//    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("客户端连接已建立，地址: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("客户端连接已断开，地址: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("发生异常，连接: {}", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
}
