package org.wenchen.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.*;
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
                            // 添加 MQTT 解码器和编码器
                            pipeline.addLast("decoder", new MqttDecoder());
                            pipeline.addLast("encoder", MqttEncoder.INSTANCE);
                            // 自定义处理器
                            pipeline.addLast(new MqttServerHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("MQTT 服务器启动成功，监听端口: {}", port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
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
            case DISCONNECT -> log.info("客户端断开连接: {}", ctx.channel().remoteAddress());
            default -> log.warn("未处理的消息类型: {}", messageType);
        }
    }

    private void handleConnect(ChannelHandlerContext ctx, MqttConnectMessage connectMessage) {
        log.info("客户端尝试连接，地址: {}", ctx.channel().remoteAddress());
        log.info("客户端标识: {}", connectMessage.payload().clientIdentifier());

        MqttConnAckMessage connAckMessage = new MqttConnAckMessage(
                new MqttFixedHeader(MqttMessageType.CONNACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                new MqttConnAckVariableHeader(MqttConnectReturnCode.CONNECTION_ACCEPTED, false)
        );

        ctx.writeAndFlush(connAckMessage);
        log.info("连接响应已发送 (CONNACK)");
    }

    private void handlePublish(ChannelHandlerContext ctx, MqttPublishMessage publishMessage) {
        String topic = publishMessage.variableHeader().topicName();
        String payload = publishMessage.payload().toString(io.netty.util.CharsetUtil.UTF_8);
        log.info("收到发布消息，主题: {}, 内容: {}", topic, payload);

        MqttPubAckMessage pubAckMessage = new MqttPubAckMessage(
                new MqttFixedHeader(MqttMessageType.PUBACK, false, MqttQoS.AT_MOST_ONCE, false, 0),
                MqttMessageIdVariableHeader.from(publishMessage.variableHeader().packetId())
        );

        ctx.writeAndFlush(pubAckMessage);
        log.info("发布确认消息已发送 (PUBACK)");
    }

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
