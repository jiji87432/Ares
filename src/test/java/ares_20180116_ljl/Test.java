/**
 *
 */
package ares_20180116_ljl;

import java.util.Map;
import java.util.Set;

import com.pay.risk.ares.cdl.constant.RedisConstants;
import com.riskutil.redis.RedisUtil;

/**
 * @Description: 这里用一句话描述这个类的作用
 * @see: Test 此处填写需要参考的类
 * @version 2018年1月22日 上午11:31:57
 * @author longfei.li
 */
public class Test {
	public static void main(String[] args) {
		String pa5MinHash = "_PAMINHASH";
		String pa5MinSet = "_PAMINSET";
		RedisUtil jedis = null;
		jedis = new RedisUtil(RedisConstants.REDIS_CDL);
		Map<String, String> hashPA5 = jedis.hgetAll(pa5MinHash);
		Set<String> pa5MinCus = jedis.smembers(pa5MinSet);
		pa5MinCus.add("aaa");
		System.out.println(hashPA5 == null);
		System.out.println(pa5MinCus == null);
		hashPA5.put("aaa", "aaa");
		System.out.println(hashPA5.toString());
		System.out.println(pa5MinCus.toString());
	}
}
