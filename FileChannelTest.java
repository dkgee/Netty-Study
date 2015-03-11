package com.tk.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 功能:内存映射
 * 描述:适合相对大的文件映射到内存中才是值得的
 * 	1，内存映射I/O是对Channel缓存区技术的改进。当传输大量的数据时，内存映射I/O速度相对较快，是因为
 * 它使用虚拟内存把文件传输到进程的地址空间中。
 *  2，映射内存也称为共享内存，因此可以用于相关进程(均映射同一文件)之间的整块数据传输，这些进程甚至可以
 * 不位于同一个系统上，只要每个都可以访问同一文件即可。
 *  3，当对FileChannel执行映射操作，把文件映射到内存中时，得到的是一个连接到文件字节缓存区，这种映射
 * 的结果是，当输入缓存区的内容时，数据将出现在文件中，当读取缓存区时，相当于得到文件中的数据。
 * 
 * @author jinhuatao
 * @date 2015-3-11 下午4:57:57
 */
public class FileChannelTest {
	public static void main(String[] args) {
		try{
			FileChannel fc1 = new FileInputStream("D:/demo/test01.txt").getChannel();
			FileChannel fc2 = new FileOutputStream("D:/demo/test02.txt").getChannel();
			
			//映射第一个文件到内存
			MappedByteBuffer buffer = 
					fc1.map(FileChannel.MapMode.READ_ONLY, 0, fc1.size());
			//写入到第二个文件
			fc2.write(buffer);
			fc1.close();
			fc2.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
