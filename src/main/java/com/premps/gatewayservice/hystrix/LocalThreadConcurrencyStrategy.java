package com.premps.gatewayservice.hystrix;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariable;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableLifecycle;
import com.netflix.hystrix.strategy.properties.HystrixProperty;
import com.premps.gatewayservice.interceptor.UserContextHolder;

public class LocalThreadConcurrencyStrategy extends HystrixConcurrencyStrategy {

	private HystrixConcurrencyStrategy existingConcurrencyStrategy;

	public LocalThreadConcurrencyStrategy(HystrixConcurrencyStrategy existingConcurrencyStrategy) {
		this.existingConcurrencyStrategy = existingConcurrencyStrategy;
	}

	@Override
	public BlockingQueue<Runnable> getBlockingQueue(int maxQueueSize) {

		return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.getBlockingQueue(maxQueueSize)
				: super.getBlockingQueue(maxQueueSize);
	}

	@Override
	public <T> HystrixRequestVariable<T> getRequestVariable(HystrixRequestVariableLifecycle<T> rv) {
		return existingConcurrencyStrategy != null ? existingConcurrencyStrategy.getRequestVariable(rv)
				: super.getRequestVariable(rv);
	}

	@Override
	public ThreadPoolExecutor getThreadPool(final HystrixThreadPoolKey threadPoolKey,
			HystrixProperty<Integer> corePoolSize, HystrixProperty<Integer> maximumPoolSize,
			HystrixProperty<Integer> keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {

		return existingConcurrencyStrategy != null
				? existingConcurrencyStrategy.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime,
						unit, workQueue)
				: super.getThreadPool(threadPoolKey, corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	@Override
	public <T> Callable<T> wrapCallable(Callable<T> callable) {
		return existingConcurrencyStrategy != null
				? existingConcurrencyStrategy
						.wrapCallable(new DelegatingUserContexCallable<T>(callable, UserContextHolder.getUserContext()))
				: super.wrapCallable(new DelegatingUserContexCallable<T>(callable, UserContextHolder.getUserContext()));
	}
}
