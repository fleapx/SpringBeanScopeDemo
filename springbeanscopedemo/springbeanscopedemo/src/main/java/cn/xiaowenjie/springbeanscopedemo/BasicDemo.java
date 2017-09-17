package cn.xiaowenjie.springbeanscopedemo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class BasicDemo {

	private static final int SIZE = 10;

	public static void main(String[] args) {

		List<Integer> list = new ArrayList<>(SIZE);

		Random r = new Random();

		for (int i = 0; i < SIZE; i++) {
			list.add(r.nextInt(100));
		}

		System.out.println(list);

		deleteOdd1(list);

		System.out.println(list);
	}

	public static void deleteOdd1(List<Integer> list) {

		for (int i = 0; i < list.size(); i++) {
			if (isOdd(list.get(i))) {
				list.remove(i);
			}
		}
	}

	private static boolean isOdd(int i) {
		return i % 2 == 0;
	}

	public static void deleteOdd2(List<Integer> list) {
		for (Iterator iterator = list.listIterator(); iterator.hasNext();) {
			Integer num = (Integer) iterator.next();
			if (isOdd(num)) {
				iterator.remove();
			}
		}
	}
}
