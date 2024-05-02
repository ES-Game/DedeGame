package com.quangph.base.common.bus;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.quangph.base.dispatcher.BaseDispatcher;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by quangph on 12/30/2019.
 */
public class AppBus extends BaseDispatcher<IAppEvent> {

    private static AppBus sInstance;
    private List<IAppEventHandler> mHandlerList = new ArrayList<>();
    private Handler mHandler = new EventHandler(this);

    public static AppBus getInstance() {
        if (sInstance == null) {
            synchronized (AppBus.class) {
                if (sInstance == null) {
                    sInstance = new AppBus();
                    sInstance.start();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void handleEvent(IAppEvent event) throws Exception {
        mHandler.sendMessage(mHandler.obtainMessage(EventHandler.HANDLE_EVENT, event));
    }

    public void register(IAppEventHandler handler) {
        mHandlerList.add(handler);
    }

    public void unregister(IAppEventHandler handler) {
        mHandlerList.remove(handler);
    }


    private static class EventHandler extends Handler {
        static final int HANDLE_EVENT = 2;

        WeakReference<AppBus> weakBus;

        EventHandler(AppBus bus) {
            weakBus = new WeakReference<>(bus);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            AppBus bus = weakBus.get();
            if (bus == null) {
                return;
            }
            if (msg.what == HANDLE_EVENT) {
                for (IAppEventHandler handler : bus.mHandlerList) {
                    try {
                        handler.handle((IAppEvent) msg.obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
