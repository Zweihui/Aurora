package com.zwh.mvparms.eyepetizer.mvp.ui.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadSerialQueue;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Administrator on 2017/11/14 0014.
 */

public class DownloadSerialQueue {
    private final Object operationLock = new Object();
    private final BlockingQueue<BaseDownloadTask> mTasks = new LinkedBlockingQueue<>();
    private final HandlerThread mHandlerThread;
    private final Handler mHandler;

    private final static int WHAT_NEXT = 1;
    public final static int ID_INVALID = 0;

    volatile BaseDownloadTask workingTask;
    final SerialFinishCallback finishCallback;
    volatile boolean paused = false;

    public DownloadSerialQueue() {
        mHandlerThread = new HandlerThread("SerialDownloadManager");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper(), new SerialLoop());
        finishCallback = new SerialFinishCallback(new WeakReference<>(this));
        sendNext();
    }

    /**
     * Enqueues the given task sometime in the serial queue. If the {@code task} is in the head of
     * the serial queue, the {@code task} will be started automatically.
     */
    public void enqueue(BaseDownloadTask task) {
        try {
            mTasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pause the queue.
     *
     * @see #resume()
     */
    public void pause() {
        synchronized (finishCallback) {
            if (paused) {
                FileDownloadLog.w(this, "require pause this queue(remain %d), but " +
                        "it has already been paused", mTasks.size());
                return;
            }

            paused = true;
            if (workingTask != null) {
                workingTask.removeFinishListener(finishCallback);
                workingTask.pause();
                paused = false;
                sendNext();
            }
        }

    }

    /**
     * Resume the queue if the queue is paused.
     *
     * @see #pause()
     */
    public void resume() {
        synchronized (finishCallback) {
            if (!paused) {
                FileDownloadLog.w(this, "require resume this queue(remain %d), but it is" +
                        " still running", mTasks.size());
                return;
            }

            paused = false;
            if (workingTask == null) {
                sendNext();
            } else {
                workingTask.addFinishListener(finishCallback);
                workingTask.start();
            }
        }
    }

    /**
     * Returns the identify of the working task, if there is task is working, you will receive
     * {@link #ID_INVALID}.
     *
     * @return the identify of the working task
     */
    public int getWorkingTaskId() {
        return workingTask != null ? workingTask.getId() : ID_INVALID;
    }

    /**
     * Get the count of tasks which is waiting on this queue.
     *
     * @return the count of waiting tasks on this queue.
     */
    public int getWaitingTaskCount() {
        return mTasks.size();
    }

    /**
     * Attempts to stop the working task, halts the processing of waiting tasks, and returns a list
     * of the tasks that were awaiting execution. These tasks are drained (removed) from the task
     * queue upon return from this method.
     */
    public List<BaseDownloadTask> shutdown() {
        if (workingTask != null) {
            pause();
        }

        final List<BaseDownloadTask> unDealTaskList = new ArrayList<>();
        mTasks.drainTo(unDealTaskList);
        mHandler.removeMessages(WHAT_NEXT);
        mHandlerThread.interrupt();
        mHandlerThread.quit();

        return unDealTaskList;
    }


    private class SerialLoop implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_NEXT:
                    try {
                        synchronized (finishCallback) {
                            if (paused) break;
                            workingTask = mTasks.take();
                            workingTask.addFinishListener(finishCallback)
                                    .start();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            return false;
        }
    }

    private static class SerialFinishCallback implements BaseDownloadTask.FinishListener {
        private final WeakReference<DownloadSerialQueue> mQueueWeakReference;

        SerialFinishCallback(WeakReference<DownloadSerialQueue> queueWeakReference) {
            this.mQueueWeakReference = queueWeakReference;
        }

        @Override
        public synchronized void over(BaseDownloadTask task) {
            task.removeFinishListener(this);

            if (mQueueWeakReference == null) {
                return;
            }

            final DownloadSerialQueue queue = mQueueWeakReference.get();
            if (queue == null) {
                return;
            }

            queue.workingTask = null;
            if (queue.paused) {
                return;
            }
            queue.sendNext();
        }
    }

    private void sendNext() {
        mHandler.sendEmptyMessage(WHAT_NEXT);
    }

}
