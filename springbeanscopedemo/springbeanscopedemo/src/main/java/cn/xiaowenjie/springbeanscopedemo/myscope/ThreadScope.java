package cn.xiaowenjie.springbeanscopedemo.myscope;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ThreadScope implements Scope {

	public static final String THREAD = "thread";
	
	private static final ThreadLocal threadLocal = new ThreadLocal() {
		protected Object initialValue() {
			return new HashMap();
		}
	};

	@Override
	public Object get(String name, ObjectFactory objectFactory) {
		Map scope = (Map) threadLocal.get();
		Object object = scope.get(name);
		System.out.println("ThreadScope.get(), name=" + name);
		if (object == null) {
			object = objectFactory.getObject();
			scope.put(name, object);
		}
		return object;
	}

	@Override
	public Object remove(String name) {
		Map scope = (Map) threadLocal.get();
		return scope.remove(name);
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
	}

	@Override
	public String getConversationId() {
		return null;
	}

	@Override
	public Object resolveContextualObject(String arg0) {
		return null;
	}

}
