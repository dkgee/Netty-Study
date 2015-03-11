package com.tk.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;

/**
 * 功能:读取键盘的输入给服务端发送消息，同时监听服务端的消息
 * 描述:
 * @author jinhuatao
 * @date 2015-3-11 下午3:06:39
 */
public class ClientThread extends Thread{
	private CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	private CharsetEncoder encoder = Charset.forName("UTf-8").newEncoder();
	
	private Selector selector = null;
	private SocketChannel socket = null;
	private SelectionKey clientKey = null;
	
	/**
	 * 启动客户端
	 */
	public ClientThread() {
		try{
			//创建一个Selector
			selector = Selector.open();
			//创建Socket并注册
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			clientKey = socket.register(selector, SelectionKey.OP_CONNECT);
			
			//连接到远程地址
			InetSocketAddress ip = new InetSocketAddress("localhost",12345);
			socket.connect(ip);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取事件
	 * @author jinhuatao
	 * @date 2015-3-11下午3:18:36
	 */
	@Override
	public void run() {
		try{
			//监听事件
			while(true){
				selector.select();//---------监听事件
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while(it.hasNext()){
					SelectionKey key = it.next();
					//删除当前事件
					it.remove();
					
					//判断事件类型
					if(key.isConnectable()){
						//连接事件
						SocketChannel channel = (SocketChannel) key.channel();
						if(channel.isConnectionPending())
							channel.finishConnect();
						channel.register(selector, SelectionKey.OP_READ);
						System.out.println("Connect to Server Success!");
					}else if(key.isReadable()){
						//读取数据事件
						SocketChannel channel = (SocketChannel) key.channel();
						
						//读取数据
						ByteBuffer buffer = ByteBuffer.allocate(50);
						channel.read(buffer);
						buffer.flip();
						String msg = decoder.decode(buffer).toString();
						System.out.println("Receive:"+msg);
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}finally{
			//关闭
			try{
				selector.close();
				socket.close();
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 发送消息
	 * @author jinhuatao
	 * @date 2015-3-11下午3:35:28
	 * @param msg void
	 */
	public void send(String msg){
		try{
			SocketChannel client = (SocketChannel) clientKey.channel();
			client.write(encoder.encode(CharBuffer.wrap(msg)));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 关闭客户端
	 * @author jinhuatao
	 * @date 2015-3-11下午3:38:09 void
	 */
	public void close(){
		try{
			selector.close();
			socket.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
