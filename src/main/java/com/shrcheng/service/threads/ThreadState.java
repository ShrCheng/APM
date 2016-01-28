package com.shrcheng.service.threads;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.TimeUnit;

/**
 * thread manager
 * 
 * @author shicheng
 * @version 1.0
 */
public class ThreadState implements Runnable {
    
    private final ThreadMXBean threadMXBean;
    private ThreadName threadName;
    
    public ThreadState(ThreadMXBean threadMXBean, ThreadName threadName) {
        this.threadMXBean = threadMXBean;
        this.threadName = threadName;
    }
    
    public void run() {
        long[] allThreadIds = this.threadMXBean.getAllThreadIds();
        ThreadInfo[] threadInfos = this.threadMXBean.getThreadInfo(allThreadIds, 0);
        for (ThreadInfo thread : threadInfos) {
            try {
                if (thread != null) {
                    System.out.println(thread.getThreadId());
                    this.update(thread);
                    // ((ThreadTracker)this.threads.get(Long.valueOf(thread.getThreadId()))).update(thread);
                } else {
                    // Agent.LOG.finer("ThreadStateSampler: Skipping null
                    // thread.");
                }
            } catch (Exception e) {
                // Agent.LOG.log(Level.FINEST, e, e.getMessage(), new
                // Object[0]);
            }
        }
    }
    
    public static void main(String[] args) {
        new ThreadState(ManagementFactory.getThreadMXBean(), new ThreadName()).run();
    }
    
//    private class ThreadTracker {
//        
        private long lastThreadTotalCpuTime = -1L;
        private long lastThreadUserTime = -1L;
        private long lastThreadSystemTime = -1L;
        private long lastBlockedTime = -1L;
        private long lastWaitedTime = -1L;
        private long lastBlockedCount = -1L;
        private long lastWaitedCount = -1L;
//        
//        public ThreadTracker() {
//        }
        
        public void update(ThreadInfo thread) {
            String threadName = ThreadState.this.threadName.getThreadName(thread.getThreadName());
            String threadState = "Threads/SummaryState/" + thread.getThreadState().toString() + "/Count:" + 1.0F;
            String summaryState = "Threads/SummaryState/" + thread.getThreadState().toString() + "/Count:" + 1.0F;
            if (ThreadState.this.threadMXBean.isThreadCpuTimeEnabled()) {
                long totalCpuTime = ThreadState.this.threadMXBean.getThreadCpuTime(thread.getThreadId());
                long userCpuTime = ThreadState.this.threadMXBean.getThreadUserTime(thread.getThreadId());
                long systemCpuTime = totalCpuTime - userCpuTime;
                System.out.println("Threads/TotalTime/" + threadName + "/CpuTime" + totalCpuTime
                        + this.lastThreadTotalCpuTime + TimeUnit.NANOSECONDS);
                System.out.println("Threads/Time/CPU/" + threadName + "/UserTime" + userCpuTime
                        + this.lastThreadUserTime + TimeUnit.NANOSECONDS);
                System.out.println("Threads/Time/CPU/" + threadName + "/SystemTime" + systemCpuTime
                        + this.lastThreadSystemTime + TimeUnit.NANOSECONDS);
            }
            System.out.println(threadName);
            System.out.println(threadState);
            System.out.println(summaryState);
            if (ThreadState.this.threadMXBean.isThreadContentionMonitoringEnabled()) {
                System.out.println("Threads/Time/State/" + threadName + "/WaitedTime" + thread.getWaitedTime()
                        + this.lastWaitedTime + TimeUnit.MILLISECONDS);
                System.out.println("Threads/Time/State/" + threadName + "/BlockedTime" + thread.getBlockedTime()
                        + this.lastBlockedTime + TimeUnit.MILLISECONDS);
            }
            System.out.println(
                    "Threads/Count/" + threadName + "/BlockedCount" + thread.getBlockedCount() + this.lastBlockedCount);
            System.out.println(
                    "Threads/Count/" + threadName + "/WaitedCount" + thread.getWaitedCount() + this.lastWaitedCount);
        }
//        
//    }
    
}
