package com.lph.dr.distribute_redis.util;


import com.alibaba.fastjson.JSON;
import org.apache.commons.collections4.map.LRUMap;

import java.util.*;


/**
 * 统计元素的频率
 * @author: lph
 * @date:  2019/9/12 9:16
 * @version V1.0
 */
public class CountDuplicatedList {

  public static void main(String[] args) {

	List<String> list = new ArrayList();
	list.add("b");
	list.add("a");
	list.add("c");
	list.add("d");
	list.add("b");
	list.add("c");
	list.add("a");
	list.add("a");
	list.add("1");
	list.add("a");

	System.out.println("\n例子 1 -统计'a'出现的频率");
	System.out.println("a : " + Collections.frequency(list, "a"));

	System.out.println("\n例子 2 - 统计每一个元素出现的频率");
	//将List转换为Set
	Set<String> uniqueSet = new HashSet<>(list);
	for (String temp : uniqueSet) {
		System.out.println(temp + ": " + Collections.frequency(list, temp));
	}

	System.out.println("\n例子 3 - 用Map统计每个元素出现的频率");
	Map<String, Integer> map = new HashMap<>();

	for (String temp : list) {
		Integer count = map.get(temp);
		map.put(temp, (count == null) ? 1 : count + 1);
	}
	printMap(map);

	System.out.println("\nSorted Map");
	Map treeMap = new TreeMap<>(map);
	printMap(treeMap);


	// LRU进行定期淘汰算法
	  LRUMap<Integer, Integer> lruMap = new LRUMap<Integer, Integer>(3);
	  lruMap.put(1, 1);
	  lruMap.put(2, 2);
	  lruMap.put(3, 3);
	  lruMap.forEach((k, v) ->{
          System.err.println("key: " + k + "value: " +v);
      });
	  lruMap.get(2);
      lruMap.put(4, 4);
      System.err.println("\n lru操作后");
      lruMap.forEach((k, v) ->{
          System.err.println("key: " + k + "value: " +v);
      });
  }

  public static void printMap(Map<String, Integer> map){

	for (Map.Entry entry : map.entrySet()) {
		System.out.println("Key : " + entry.getKey() + " Value : "
			+ entry.getValue());
	}

  }

}