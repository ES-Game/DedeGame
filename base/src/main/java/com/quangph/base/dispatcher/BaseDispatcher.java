package com.quangph.base.dispatcher;

import android.util.Log;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by Pham Hai Quang on 9/23/2019.
 */
public abstract class BaseDispatcher<E> implements IDispatcher<E> {
    private final ThreadLocal<Deque<E>> mQueue =
            new ThreadLocal<Deque<E>>() {
                @Override
                protected Deque<E> initialValue() {
                    return new LinkedList<>();
                }
            };

    private final ThreadLocal<Boolean> mDispatching =
            new ThreadLocal<Boolean>() {
                @Override
                protected Boolean initialValue() {
                    return false;
                }
            };

    private final ThreadLocal<Boolean> mStartStop = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    protected abstract void handleEvent(E event) throws Exception;

    @Override
    public void start() {
        mStartStop.set(true);
        nextEvent(mQueue.get());
        Log.e(getClass().getSimpleName(), "Start queue");
    }

    @Override
    public void stop() {
        mStartStop.set(false);
    }

    @Override
    public int size() {
        Deque<E> queueForThread = mQueue.get();
        if (queueForThread == null) {
            return 0;
        } else {
            return queueForThread.size();
        }
    }

    @Override
    public void dispatch(E event) {
        Deque<E> queueForThread = mQueue.get();
        queueForThread.offer(event);
        nextEvent(queueForThread);
    }

    @Override
    public void dispatchAtFrontOfQueue(E event) {
        Deque<E> queueForThread = mQueue.get();
        queueForThread.offerFirst(event);
        nextEvent(queueForThread);
    }

    @Override
    public void dispatchToEndOfQueue(E event) {
        Deque<E> queueForThread = mQueue.get();
        queueForThread.offerLast(event);
        nextEvent(queueForThread);
    }

    private void nextEvent(Deque<E> queueForThread) {
        Boolean hasStart = mStartStop.get();
        if (hasStart == null || !hasStart) return;

        Boolean hasDispatching = mDispatching.get();
        if (hasDispatching == null || !hasDispatching) {
            mDispatching.set(true);
            try {
                E nextEvent;
                while ((nextEvent = queueForThread.poll()) != null) {
                    handleEvent(nextEvent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mDispatching.remove();
                mQueue.remove();
            }
        }
    }
}
