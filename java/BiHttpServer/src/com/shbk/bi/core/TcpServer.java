package com.shbk.bi.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class TcpServer {
	
	public static void run(int port) throws IOException, ClosedChannelException {
		Selector selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverChannel.socket();
		serverSocket.bind(new InetSocketAddress(port));
		serverSocket.setReuseAddress(true);
	    serverChannel.configureBlocking(false);
	    SelectionKey serverKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);  
	    serverKey.attach(new ServerHandler());  
	    
	    while (true) {  
	        if (0 == selector.select(30)) {
	        	continue;
	        }
	  
	        for (Iterator<SelectionKey> itor = selector.selectedKeys().iterator(); itor.hasNext();) {  
	            SelectionKey key = (SelectionKey) itor.next();  
	            itor.remove();
	            
	            Handler handler = (Handler) key.attachment();  
	            handler.execute(selector, key);  
	        }  
	    }  
	}
}
