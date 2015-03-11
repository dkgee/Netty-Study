package com.tk.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 功能:客户端主线程
 * 描述:
 * @author jinhuatao
 * @date 2015-3-11 下午3:39:28
 */
public class Client {
	public static void main(String[] args) {
		ClientThread client = new ClientThread();
		client.start();
		
		//输入输出流
		BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
		
		try{
			//循环读取键盘输入
			String readline;
			while((readline = sin.readLine()) != null){
				if(readline.equals("bye")){
					client.close();
					System.exit(0);
				}
				
				client.send(readline);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
