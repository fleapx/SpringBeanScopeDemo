package cn.xiaowenjie.springbeanscopedemo;

import java.util.HashMap;
import java.util.LinkedHashMap;

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

	@GetMapping("/test1")
	public HashMap<String, Object> test1(HttpSession session) {

		System.out.println(sessionBean1.hashCode());
		System.out.println(sessionBean1.getClass());
		System.out.println();

		HashMap<String, Object> data = new LinkedHashMap<>();

		data.put("sessionid", session.getId());
		data.put("sessionbean.count", sessionBean1.invokeCount());
		data.put("controller.count", count++);
		data.put("threadBean.count", threadBean1.invokeCount());

		return data;
	}

}
