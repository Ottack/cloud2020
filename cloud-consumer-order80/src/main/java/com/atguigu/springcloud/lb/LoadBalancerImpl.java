package com.atguigu.springcloud.lb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class LoadBalancerImpl implements LoadBalancer {

	private AtomicInteger atomicInteger = new AtomicInteger(0);

	@Override
	public ServiceInstance getInstances(List<ServiceInstance> instanceList) {
		int nextIndex = getAndIncrement()%instanceList.size();
		return instanceList.get(nextIndex);
	}

	public final int getAndIncrement() {
		int current;
		int next;
		do {
			current = atomicInteger.get();
			next = current >= Integer.MAX_VALUE ? 0 : current + 1;
		} while (!atomicInteger.compareAndSet(current, next));
		log.info("*****第几次访问： "+ next);
		return next;
	}
}
