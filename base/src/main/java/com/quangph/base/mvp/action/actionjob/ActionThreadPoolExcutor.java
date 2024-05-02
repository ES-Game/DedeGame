package com.quangph.base.mvp.action.actionjob;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by QuangPH on 2020-03-23.
 */
public class ActionThreadPoolExcutor extends ThreadPoolExecutor{

    private List<ActionJob> mJobList = new CopyOnWriteArrayList<>();
    private boolean isInterupted = false;

    public ActionThreadPoolExcutor() {
        super(1, 20, 3, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof ActionCallable) {
            ActionFutureTask<T> futureTask = new ActionFutureTask<T>(callable);
            futureTask.job = ((ActionCallable<T>) callable).job;
            mJobList.add(futureTask.job);
            return futureTask;
        } else {
            return super.newTaskFor(callable);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (isInterupted) return;

        if (r instanceof ActionFutureTask) {
            if (t == null) {
                ActionJob job = ((ActionFutureTask) r).job;
                try {
                    job.onResult(((ActionFutureTask) r).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    job.onException(e);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    job.onException(e.getCause());
                }
            }
        }
    }

    public<T> void doActionJob(final ActionJob<T> job) {
        submit(new ActionCallable<T>(job));
    }

    public void stop() {
        isInterupted = true;
        Iterator<ActionJob> iterator = mJobList.iterator();
        while (iterator.hasNext()) {
            ActionJob next = iterator.next();
            next.stop();
        }
        mJobList.clear();
        mJobList = null;
    }


    private class ActionFutureTask<T> extends FutureTask<T> {

        public ActionJob<T> job;

        public ActionFutureTask(Callable<T> callable) {
            super(callable);
        }
    }

    private class ActionCallable<T> implements Callable<T> {

        ActionJob<T> job;

        ActionCallable(ActionJob<T> job) {
            this.job = job;
        }

        @Override
        public T call() throws Exception {
            return this.job.submitJob();
        }
    }
}
