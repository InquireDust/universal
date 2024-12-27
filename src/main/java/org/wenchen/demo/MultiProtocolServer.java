//package org.wenchen.demo;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.http.HttpServerCodec;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpRequestDecoder;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator;
//import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
//import io.netty.handler.codec.mqtt.MqttDecoder;
//import io.netty.handler.codec.mqtt.MqttEncoder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class MultiProtocolServer {
//    private static final Logger log = LoggerFactory.getLogger(MultiProtocolServer.class);
//    private final int port;
//
//    public MultiProtocolServer(int port) {
//        this.port = port;
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        new MultiProtocolServer(8888).start();  // 设置为服务器监听端口
//    }
//
//    public void start() throws InterruptedException {
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        try {
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .childHandler(new ChannelInitializer<Channel>() {
//                        @Override
//                        protected void initChannel(Channel ch) throws Exception {
//                            ChannelPipeline pipeline = ch.pipeline();
//
//                            // 添加不同协议的处理器
//
//                            // HTTP 协议处理器
//                            pipeline.addLast("http-codec", new HttpServerCodec());
//                            pipeline.addLast("http-aggregator", new HttpObjectAggregator(65536));
//                            pipeline.addLast("http-handler", new HttpRequestHandler());
//
//                            // WebSocket 协议处理器
//                            pipeline.addLast("websocket-aggregator", new WebSocketFrameAggregator(65536));
//                            pipeline.addLast("websocket-handler", new WebSocketHandler());
//                            pipeline.addLast("websocket-protocol-handler", new WebSocketServerProtocolHandler("/ws"));
//
//                            // MQTT 协议处理器
//                            pipeline.addLast("mqtt-decoder", new MqttDecoder());
//                            pipeline.addLast("mqtt-encoder", MqttEncoder.INSTANCE);
//                            pipeline.addLast("mqtt-handler", new MqttServerHandler());
//                        }
//                    });
//
//            ChannelFuture future = bootstrap.bind(port).sync();
//            log.info("服务器启动成功，监听端口: {}", port);
//            future.channel().closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//            log.info("服务器已关闭");
//        }
//    }
//}
