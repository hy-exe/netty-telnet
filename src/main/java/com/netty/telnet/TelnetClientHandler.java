package com.netty.telnet;

import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yin.huang
 * @date 2018年3月12日 下午2:35:49
 */
public class TelnetClientHandler extends SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(io.netty.channel.ChannelHandlerContext arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(arg0, arg1);
	}
}
