package com.pep.dubsdk.media;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppThreadPool {
	private static ExecutorService mSingleThreadPool;

	/**
	 * 顺序执行任务
	 */
	public static void executeOrderTask(Runnable task) {
		// 判断是否被关闭
		if (mSingleThreadPool == null || mSingleThreadPool.isShutdown()) {
			mSingleThreadPool = Executors.newSingleThreadExecutor();
		}
		mSingleThreadPool.execute(task);
	}

	/**
	 * 关闭线程池
	 */
	public static void closeThreadPool() {
		if (mSingleThreadPool != null && !mSingleThreadPool.isShutdown()) {
			mSingleThreadPool.shutdownNow();
			mSingleThreadPool = null;
		}
	}
}
