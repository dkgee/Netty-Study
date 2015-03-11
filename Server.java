package com.tk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

/**
 * 功能:接收多个客户端请求的服务器程序
 * 描述:服务端使用非阻塞模式监听多个客户端的连接和发送来的消息，在收到消息后根据消息命令来处理不同的业务逻辑
 *     然后回复给客户端，客户端通过控制台输入的字符串发送给服务端。
 * @author jinhuatao
 * @date 2015-3-11 下午2:10:09
 */
public class Server {
	public static void main(String[] args) {
		Selector selector = null;
		ServerSocketChannel server = null;
		try{
			//创建一个Selector
			selector = Selector.open();
			
			//创建Socket
			server = ServerSocketChannel.open();
			
			//启动端口监听
			InetSocketAddress ip = new InetSocketAddress(12345);
			server.socket().bind(ip);
			
			server.configureBlocking(false);
			server.register(selector, SelectionKey.OP_ACCEPT);
			
			//监听事件
			while(true){
				//监听事件
				selector.select();
				//事件来源列表
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					//删除当前事件
					it.remove();
					
					//判断事件类型
					if(key.isAcceptable()){
						//连接事件
						ServerSocketChannel server2 = 
								(ServerSocketChannel)key.channel();
						SocketChannel channel = server2.accept();
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
						
						System.out.println("Client connect:"
								+channel.socket().getInetAddress().getHostName()
								+":"+channel.socket().getPort());
					}else if(key.isReadable()){
						//读取数据事件
						SocketChannel channel = (SocketChannel) key.channel();
						
						//读取数据
						CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
						ByteBuffer buffer = ByteBuffer.allocate(50);
						channel.read(buffer);
						buffer.flip();
						String msg = decoder.decode(buffer).toString();
						System.out.println("Receive:"+msg);
						
						//写入数据
						CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
						channel.write(encoder.encode(CharBuffer.wrap("server "+msg)));
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			//关闭
			try{
				selector.close();
				server.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
