# netty-telnet
使用netty实现的Telnet命令
主要使用netty进行服务器通信。

使用实例：
TelnetClient telnetClient = new TelnetClient("127.0.0.1", 11211); //连接服务器
telnetClient.telnet("flush_all"); //执行服务器命令
