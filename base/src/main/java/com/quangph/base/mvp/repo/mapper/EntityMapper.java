package com.quangph.base.mvp.repo.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Convert from entity to model
 * Created by Pham Hai Quang on 8/12/2019.
 */
public class EntityMapper<S> {

    private S mSrc;
    private List<S> mListSrc;
    private IValideFieldName mValidator = new DefaultValidName();

    public static <S> EntityMapper<S> from(S source) {
        return new EntityMapper<S>(source);
    }

    public static <S> EntityMapper<S> from(List<S> sourceList) {
        return new EntityMapper<S>(sourceList);
    }

    private EntityMapper(S src) {
        mSrc = src;
    }

    private EntityMapper(List<S> listSrc) {
        mListSrc = listSrc;
    }

    public<D> D to(Class<D> destClass) {
        Class<S> sClass = (Class<S>) mSrc.getClass();
        Field[] srcFields = sClass.getDeclaredFields();
        Field[] destFields = destClass.getDeclaredFields();
        return initInstance(mSrc, destClass, srcFields, destFields);
    }

    public <D> List<D> toList(Class<D> destClass) {
        List<D> result = new ArrayList<>();
        if (mListSrc == null || mListSrc.isEmpty()) {
            return  result;
        }

        S first = mListSrc.get(0);
        Class<S> sClass = (Class<S>) first.getClass();
        Field[] srcFields = sClass.getDeclaredFields();
        Field[] destFields = destClass.getDeclaredFields();
        for (S src : mListSrc) {
            result.add(initInstance(src, destClass, srcFields, destFields));
        }
        return result;
    }

    private<D> D initInstance(S srcObj, Class<D> destClass, Field[] srcFields, Field[] destFields) {
        D destInstance = createInstance(destClass);
        if (destInstance == null) {
            throw new RuntimeException("Can not create instance of class: " + destClass.getCanonicalName());
        }

        try {
            for (Field f : srcFields) {
                Field dest = findDestinationField(f, destFields);
                if (dest != null) {
                    boolean s = f.isAccessible();
                    boolean d = dest.isAccessible();
                    f.setAccessible(true);
                    dest.setAccessible(true);
                    dest.set(destInstance, f.get(srcObj));
                    f.setAccessible(s);
                    dest.setAccessible(d);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return destInstance;
    }

    private<D> D createInstance(Class<D> dClass) {
        try {
            Constructor<D> defaultConstructor = dClass.getConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field findDestinationField(Field sourceField, Field[] destFields) {
        for (Field f : destFields) {
            if (mValidator.valid(sourceField.getName(), f.getName())) {
                return f;
            }
        }
        return null;
    }


    private static class DefaultValidName implements IValideFieldName {

        @Override
        public boolean valid(String srcName, String destName) {
            return srcName.equals(destName);
        }
    }
}
