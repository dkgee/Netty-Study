# nio  非阻塞编程
从jdk1.4开始，NIO API作为一个基于缓存区，并能提供非阻塞(non-blocking) IO操作的API被引入。
NIO所在的包为nio,其中n表示non-blocking.但是实际上我们可以把其理解为nio=net + io,因为NIO
包实现了网络通信和I/O的联合功能
  java.nio-----java.net
            |--java.io

