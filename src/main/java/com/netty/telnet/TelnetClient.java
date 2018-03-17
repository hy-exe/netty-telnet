package com.netty.telnet;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author yin.huang
 * @date 2018年3月12日 下午2:24:47
 */
public class TelnetClient {

	EventLoopGroup workGroup = null;

	Bootstrap bootstrap = null;

	ChannelFuture future = null;

	public TelnetClient(String ip, int port) {
		// Client服务启动器
		workGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(workGroup);
		bootstrap.channel(NioSocketChannel.class);
		// 设置一个处理服务端消息和各种消息事件的类(Handler)
		bootstrap.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// 以\n为结束分隔符
				ch.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

				ch.pipeline().addLast("decoder", new StringDecoder());
				ch.pipeline().addLast("encoder", new StringEncoder());

				ch.pipeline().addLast("handler", new TelnetClientHandler());
			}
		});
		bootstrap.option(ChannelOption.SO_KEEPALIVE, false); //短连接
		// 连接
		future = bootstrap.connect(new InetSocketAddress(ip, port));
	}

	public void telnet(String command) {
		Channel channel = future.channel();
		if (!future.isSuccess()) {
			future.cause().printStackTrace();
			workGroup.shutdownGracefully();
			return;
		}

		ChannelFuture lastWriteFuture = null;

		lastWriteFuture = channel.write(command + System.getProperty("line.separator", "/n"));

		// 退出命令
		if (command.toLowerCase().equals("bye")) {
			channel.closeFuture().awaitUninterruptibly();

			if (lastWriteFuture != null) {
				lastWriteFuture.awaitUninterruptibly();
			}

			channel.close().awaitUninterruptibly();

			workGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {

		// TelnetClient telnetClient = new TelnetClient("127.0.0.1", 11211);
		// telnetClient.telnet("flush_all");
		String a = "hgebcijedg";
		String b = "gdejicbegh";
		System.out.println(a.hashCode() == b.hashCode());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("1", 1);

		List<String> list = new ArrayList<String>();
		list.add("1");
	}

}
