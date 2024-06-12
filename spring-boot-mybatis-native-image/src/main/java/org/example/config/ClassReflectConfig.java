package org.example.config;

import cn.hutool.core.util.ClassUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;




/**
 * 反射将所有项目类扫描加入到服务, 大力出奇迹的操作,感觉不太合适,不过先让服务跑起来
 *
 * @author PC
 *
 */
@Component
public class ClassReflectConfig {

	static boolean begin = true;

	private Boolean scanclass=true;

	@Autowired
	private ThreadPoolTaskExecutor executorService;

	@PostConstruct
	public void init() {

		if (scanclass) {
			System.err.println("配置文件下 scanclass 开启了生成反射类");
		} else {
			System.err.println("配置文件下 scanclass 关闭了生成反射类");
		}

		synchronized (ClassReflectConfig.class) {
			if (begin && scanclass) {
				begin = false;
				executorService.submit(() -> {
					{
						// 扫描系统第二级开始的包
						String packageName = ClassReflectConfig.class.getPackageName();
						String proPackageName = packageName.substring(0,
								packageName.indexOf(".", packageName.indexOf(".") + 1));

						// 可以在这个地方,添加除了服务以外其他的包,将会加入反射,以供graalvm生成配置
						List<String> asList = Arrays.asList(proPackageName);

						for (String spn : asList) {
							try {
								Set<Class<?>> doScan = ClassUtil.scanPackage(spn);
								for (Class clazz : doScan) {
									handlerClass(clazz);
								}
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		}


	}

	private void handlerClass(Class clazz) {
		if (clazz.equals(ClassReflectConfig.class)) {
			// 跳过自己,避免形成循环
			return;
		}

		executorService.submit(() -> {
			try {
				System.err.println("反射注入:" + clazz.getName());
				// 生成所有的构造器
				Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
				// 找到无参构造器然后实例化
				Constructor declaredConstructor = clazz.getDeclaredConstructor();
				declaredConstructor.setAccessible(true);
				Object newInstance = declaredConstructor.newInstance();
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					try {
						// 实例化成功,那么调用一下
						method.setAccessible(true);
						// graalvm必须需要声明方法
						method.invoke(newInstance);
					} catch (Throwable e) {
					}
				}
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					try {
						field.setAccessible(true);
						field.getType();
						String name = field.getName();
						field.get(newInstance);

					} catch (Throwable e) {
					}
				}
				System.err.println("反射注入完成:" + clazz.getName());
			} catch (Throwable e) {
			}
		});
	}

}
