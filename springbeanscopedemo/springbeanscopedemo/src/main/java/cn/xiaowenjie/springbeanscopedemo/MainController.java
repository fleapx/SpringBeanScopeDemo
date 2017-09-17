package cn.xiaowenjie.springbeanscopedemo;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xiaowenjie.springbeanscopedemo.beans.SessionBean1;
import cn.xiaowenjie.springbeanscopedemo.beans.ThreadBean1;

@RestController
public class MainController {

	private int count;

	@Autowired
	SessionBean1 sessionBean1;

	@Autowired
	ThreadBean1 threadBean1;

	@Autowired
	HttpServletRequest request;

	@Autowired
	MainServices services;

	@GetMapping("/test1")
	public HashMap<String, Object> test1(HttpSession session) {

		System.out.println(sessionBean1.hashCode());
		System.out.println(sessionBean1.getClass());
		System.out.println();

		HashMap<String, Object> data = new LinkedHashMap<>();

		data.put("controller.count", count++);

		data.put("sessionid", session.getId());
		data.put("sessionbean.count", sessionBean1.invokeCount());
		data.put("threadBean.count", threadBean1.invokeCount());

		data.put("services.count", services.invokeCount());

		// FIXME 每次都会新建一个bean
		data.put("services.count2", services.invokeCount2());

		data.put("services.class", services.getClass());

		return data;
	}

	@GetMapping("/test2")
	public HashMap<String, Object> test2(HttpSession session) {
		HashMap<String, Object> data = new LinkedHashMap<>();

		data.put("classname", request.getClass());

		return data;
	}

	@GetMapping("/test3")
	public HashMap<String, Object> test2(@Autowired MainServices s) {
		HashMap<String, Object> data = new LinkedHashMap<>();

		data.put("services.count", s.invokeCount());
		data.put("services.count2", s.invokeCount2());
		data.put("services", s.hashCode());
		data.put("services.class", s.getClass());

		return data;
	}

}
