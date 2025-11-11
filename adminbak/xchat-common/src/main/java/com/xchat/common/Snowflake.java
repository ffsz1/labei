package com.xchat.common;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;


public class Snowflake {

	public static final int NODE_SHIFT = 10;
	public static final int SEQ_SHIFT = 12;

	public static final short MAX_NODE = 1024;
	public static final short MAX_SEQUENCE = 4096;

	private AtomicInteger sequence = new AtomicInteger(0);
	private AtomicLong referenceTime = new AtomicLong(0L);

	private int workID;

	private ReentrantLock lock = new ReentrantLock();

	public Snowflake() {
		int workID = Integer.parseInt("2");
		if (workID < 0 || workID > MAX_NODE) {
			throw new IllegalArgumentException(String.format("node must be between %s and %s", 0, MAX_NODE));
		}
		this.workID = workID;
	}

	public long next() {
		long currentTime = System.currentTimeMillis();
		long counter;
		lock.lock();
		try {
			if (currentTime < referenceTime.get()) {
				throw new RuntimeException(
						String.format("Last referenceTime %s is after reference time %s", referenceTime, currentTime));
			} else if (currentTime > referenceTime.get()) {
				this.sequence.set(0);
			} else {
				if (this.sequence.shortValue() < Snowflake.MAX_SEQUENCE) {
					this.sequence.incrementAndGet();
				} else {
					throw new RuntimeException("Sequence exhausted at " + this.sequence);
				}
			}
			counter = this.sequence.get();
			referenceTime.set(currentTime);
		} finally {
			lock.unlock();
		}
		return currentTime << NODE_SHIFT << SEQ_SHIFT | workID << SEQ_SHIFT | counter;
	}

	public static void main(String args[]){
		Snowflake Snowflake=new Snowflake();
		System.out.println(Snowflake.next());
	}
}
