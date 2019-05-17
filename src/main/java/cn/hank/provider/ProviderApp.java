package cn.hank.provider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cn.hank.pojo.Calculate;
import cn.hank.pojo.CalculateImpl;
import cn.hank.request.CalculateRpcRequest;

public class ProviderApp {

	public static final int PORT = 9999;
	private Calculate calculate = new CalculateImpl();

	public static void main(String[] args) throws IOException {
		new ProviderApp().run();
	}

	private void run() throws IOException {
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while (true) {
				Socket socket = listener.accept();
				try {
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
					Object object = objectInputStream.readObject();

					int result = 0;
					if (object instanceof CalculateRpcRequest) {
						CalculateRpcRequest calculateRpcRequest = (CalculateRpcRequest) object;
						if ("add".equals(calculateRpcRequest.getMethod())) {
							System.out.println("µ÷ÓÃÖÐ...");
							result = calculate.add(calculateRpcRequest.getA(), calculateRpcRequest.getB());
						} else {
							throw new UnsupportedOperationException();
						}
					}
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(new Integer(result));
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					socket.close();
				}
			}
		} finally {
			listener.close();
		}
	}
}
