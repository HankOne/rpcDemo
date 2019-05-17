package cn.hank.proxy;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cn.hank.pojo.Calculate;
import cn.hank.request.CalculateRpcRequest;

public class CalculateRemoteImpl implements Calculate {
	public static final int PORT = 9999;

	public int add(int a, int b) {
		System.out.println("远程调用前...");
		// 获取要调用服务的实例列表
		List<String> addressList = lookupProviders("Calculator.add");
		// 模拟负载均衡算法
		String address = chooseTarget(addressList);
		try {
			// 使用Socket进行远程通讯
			Socket socket = new Socket(address, PORT);

			// 将请求序列化
			CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

			// 将请求发给服务提供方
			objectOutputStream.writeObject(calculateRpcRequest);

			// 将响应体反序列化
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			Object response = objectInputStream.readObject();
			System.out.println("远程调用结束...");

			if (response instanceof Integer) {
				return (Integer) response;
			} else {
				throw new RuntimeException("返回数据异常");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private CalculateRpcRequest generateRequest(int a, int b) {
		CalculateRpcRequest calculateRpcRequest = new CalculateRpcRequest();
		calculateRpcRequest.setA(a);
		calculateRpcRequest.setB(b);
		calculateRpcRequest.setMethod("add");
		return calculateRpcRequest;
	}

	private String chooseTarget(List<String> providers) {
		if (null == providers || providers.size() == 0) {
			throw new IllegalArgumentException();
		}
		return providers.get(0);
	}

	public static List<String> lookupProviders(String name) {
		List<String> strings = new ArrayList<String>();
		strings.add("127.0.0.1");
		return strings;
	}

}
