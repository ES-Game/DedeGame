package com.quangph.base.tracking;

import android.app.Activity;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Pham Hai Quang on 4/10/2019.
 */
public abstract class Tracker<T> {
    private static final String TAG = "Tracker";

    private T mTrackerConfig;

    protected abstract ITrackerBuilder onCreateTrackerBuilder();

    public ITrackerBuilder start() {
        return onCreateTrackerBuilder();
    }

    public T createTrackerConfig(final Class<T> configClass, final Activity src) {
        if (mTrackerConfig != null) {
            return mTrackerConfig;
        }

        Object object = Proxy.newProxyInstance(src.getClassLoader(),
                new Class[]{configClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ITrackerBuilder builder = onCreateTrackerBuilder();
                String screenLabel = getScreenLabel(method);
                if (screenLabel == null) {
                    screenLabel = getScreenLabel(configClass);
                }
                if (screenLabel != null) {
                    builder.screen(src, screenLabel);
                }

                TrackerEvent eventAno = method.getAnnotation(TrackerEvent.class);
                if (eventAno != null) {
                    builder.event(eventAno.value());
                }

                if (args != null) {
                    Annotation[][] paramAnnos = method.getParameterAnnotations();
                    for (int i = 0; i < args.length; i++) {
                        String trackerKey = getTrackerValue(paramAnnos[i]);
                        if (trackerKey != null) {
                            builder.put(trackerKey, args[i]);
                        } else if (isPayload(paramAnnos[i])) {
                            builder.payload(args[i]);
                        }
                    }
                }
                builder.track();
                return null;
            }
        });
        mTrackerConfig = (T) object;
        return mTrackerConfig;
    }

    private String getTrackerValue(Annotation[] paramAnnos) {
        String trackerVal = null;
        for (Annotation anno : paramAnnos) {
            if (anno instanceof TrackerValue) {
                trackerVal = ((TrackerValue) anno).value();
                break;
            }
        }
        return trackerVal;
    }

    private String getScreenLabel(Class<T> trackerConfigClass) {
        TrackerScreen screenAnno = trackerConfigClass.getAnnotation(TrackerScreen.class);
        if (screenAnno != null) {
            return screenAnno.value();
        }
        return "";
    }

    private boolean isPayload(Annotation[] paramAnnos) {
        for (Annotation anno : paramAnnos) {
            if (anno instanceof TrackerPayload) {
                return true;
            }
        }
        return false;
    }

    /**
     * In case screen label is config as an annotation of func. In this case, screen label will
     * replace the screen label which is config at the top of config class
     * @param method
     * @return
     */
    private String getScreenLabel(Method method) {
        TrackerScreen anno = method.getAnnotation(TrackerScreen.class);
        if (anno != null) {
            return anno.value();
        }
        return null;
    }
}
