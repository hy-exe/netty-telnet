package com.netty.telnet;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

/**
 * @author yin.huang
 * @date 2018年3月12日 下午2:35:49
 */
public class TelnetClientHandler extends SimpleChannelUpstreamHandler {

  private static final Logger logger = Logger.getLogger(TelnetClientHandler.class.getName());

  public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
    if ((e instanceof ChannelStateEvent)) {
      logger.info(e.toString());
    }
    super.handleUpstream(ctx, e);
  }

  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
    System.err.println(e.getMessage());
  }

  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
    logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());

    e.getChannel().close();
  }
}
