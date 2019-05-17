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
		System.out.println("Զ�̵���ǰ...");
		// ��ȡҪ���÷����ʵ���б�
		List<String> addressList = lookupProviders("Calculator.add");
		// ģ�⸺�ؾ����㷨
		String address = chooseTarget(addressList);
		try {
			// ʹ��Socket����Զ��ͨѶ
			Socket socket = new Socket(address, PORT);

			// ���������л�
			CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

			// �����󷢸������ṩ��
			objectOutputStream.writeObject(calculateRpcRequest);

			// ����Ӧ�巴���л�
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			Object response = objectInputStream.readObject();
			System.out.println("Զ�̵��ý���...");

			if (response instanceof Integer) {
				return (Integer) response;
			} else {
				throw new RuntimeException("���������쳣");
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
