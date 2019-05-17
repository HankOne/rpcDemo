package cn.hank.consumer;

import cn.hank.pojo.Calculate;
import cn.hank.proxy.CalculateRemoteImpl;

/**
 * 把RPC的逻辑封装进了CalculateRemoteImpl
 * 
 * @author hank
 */
public class ConsumerApp {
	public static void main(String[] args) {
		Calculate calculate = new CalculateRemoteImpl();
		int result = calculate.add(1, 2);
		System.out.println(result);
	}
}
