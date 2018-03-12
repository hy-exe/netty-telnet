package com.netty.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

/**
 * @author yin.huang
 * @date 2018年3月12日 下午2:24:47
 */
public class TelnetClient {

  ClientBootstrap bootstrap = null;

  ChannelFuture   future    = null;

  public TelnetClient(String ip, int port) {
    // Client服务启动器
    bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    // 设置一个处理服务端消息和各种消息事件的类(Handler)
    bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());
    // 连接
    future = bootstrap.connect(new InetSocketAddress(ip, port));
  }

  public void telnet(String command) {
    Channel channel = future.awaitUninterruptibly().getChannel();
    if (!future.isSuccess()) {
      future.getCause().printStackTrace();
      bootstrap.releaseExternalResources();
      return;
    }

    ChannelFuture lastWriteFuture = null;

    lastWriteFuture = channel.write(command + System.getProperty("line.separator", "/n"));

    // 退出命令
    if (command.toLowerCase().equals("bye")) {
      channel.getCloseFuture().awaitUninterruptibly();

      if (lastWriteFuture != null) {
        lastWriteFuture.awaitUninterruptibly();
      }

      channel.close().awaitUninterruptibly();

      bootstrap.releaseExternalResources();
    }
  }

  public static void main(String[] args) {

    TelnetClient telnetClient = new TelnetClient("192.168.1.200", 11211);
    telnetClient.telnet("flush_all");

  }

}
