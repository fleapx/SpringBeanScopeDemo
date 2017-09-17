package cn.xiaowenjie.springbeanscopedemo;

public class StringDemo {

	public static void main(String[] args) {
		String s = "";

		for (int i = 0; i < 100; i++) {
			s += String.valueOf(i);
		}

		System.out.println(s);
	}
}
