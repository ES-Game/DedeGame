package com.quangph.base.viewbinder;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Created by QuangPH on 6/15/2016.
 */
public class ViewBinder {

    /**
     *
     * @param v
     * @param obj contain v
     */
    public static void bind(View v, Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        ViewFinder finder = new ViewFinder.ByView(v);
        for (Field field : fields) {
            if (View.class.isAssignableFrom(field.getType())) {
                setFieldValue(field, obj, finder);
            }
        }
    }

    public static void bind(Activity activity) {
        Field[] fields = activity.getClass().getDeclaredFields();
        ViewFinder finder = new ViewFinder.ByActivity(activity);
        for (Field field : fields) {
            if (View.class.isAssignableFrom(field.getType())) {
                setFieldValue(field, activity, finder);
            }
        }
    }

    public static void bind(View view) {
        bind(view, view);
    }

    public static void bind(Activity activity, Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        ViewFinder finder = new ViewFinder.ByActivity(activity);
        for (Field field : fields) {
            if (View.class.isAssignableFrom(field.getType())) {
                setFieldValue(field, obj, finder);
            }
        }
    }

    private static void setFieldValue(Field field, Object obj, ViewFinder finder) {
        try {
            Id ano= field.getAnnotation(Id.class);
            if (ano != null) {
                field.setAccessible(true);
                field.set(obj, finder.findViewById(ano.value()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void compoundView(ViewGroup container) {
        Class<?> clazz = getAnnoSuperClass(container.getClass());
        Annotation ano = clazz.getAnnotation(Layout.class);
        if (ano != null) {
            int layout = ((Layout)ano).value();
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            inflater.inflate(layout, container, true);
            bind(clazz, container);
        }
    }

    private static void bind(Class<?> parentView, View view) {
        Field[] fields = parentView.getDeclaredFields();
        ViewFinder finder = new ViewFinder.ByView(view);
        for (Field field : fields) {
            if (View.class.isAssignableFrom(field.getType())) {
                setFieldValue(field, view, finder);
            }
        }
    }

    /**
     * Get layout id. Each activity, fragment or android.View must has a view layout to present UI.
     * Ideal: activity/fragment/view declared a @Layout annotation at the head, and then call
     * ViewBinder.getViewLayout to find layout id automatically.
     * @param view
     * @return
     */
    public static int getViewLayout(Object view) {
        /*Class<?> clazz = view.getClass();
        Layout ano = clazz.getAnnotation(Layout.class);
        if (ano != null) {
            return ano.value();
        } else {
            throw new RuntimeException("Must declare Layout anotation at the head of activity class");
        }*/

        Class<?> clazz = view.getClass();
        while (clazz != null) {
            Layout ano = clazz.getAnnotation(Layout.class);
            if (ano != null) {
                return ano.value();
            } else {
                clazz = clazz.getSuperclass();
            }
        }
        throw new RuntimeException("Must declare Layout anotation at the head of activity class");
    }

    public static int getViewLayout(Class<?> viewClass) {
        Layout ano = viewClass.getAnnotation(Layout.class);
        if (ano != null) {
            return ano.value();
        } else {
            throw new RuntimeException("Must declare Layout anotation at the head of activity class");
        }
    }

    public static void bindAttributesForView(View view, AttributeSet attrs, int[] styleable) {
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, styleable);
        bindAttrsForClass(view.getClass(), view, a);
        a.recycle();
    }

    public static void bindAttributesForCompoundView(View view, AttributeSet attrs, int[] styleable) {
        Class<?> clazz = getAnnoSuperClass(view.getClass());
        TypedArray a = view.getContext().obtainStyledAttributes(attrs, styleable);
        bindAttrsForClass(clazz, view, a);
        a.recycle();
    }

    private static void bindAttrsForClass(Class clazz, View v, TypedArray a){
        for(Field field : clazz.getDeclaredFields()){
            if (field.isAnnotationPresent(Attrs.class)) {
                Attrs attrs = field.getAnnotation(Attrs.class);
                if (attrs != null) {
                    setAttrsVal(v, field, a, attrs);
                }
            }
        }
    }

    private static void setAttrsVal(View v, Field field, TypedArray a, Attrs attrs) {
        field.setAccessible(true);
        try {
            switch (attrs.type()) {
                case Attrs.DIMENSION:
                    field.setFloat(v, a.getDimension(attrs.index(), Float.valueOf(attrs.defVal())));
                    break;
                case Attrs.COLOR:
                    field.setInt(v, a.getColor(attrs.index(), Color.parseColor(attrs.defVal())));
                    break;
                case Attrs.INTEGER:
                    field.setInt(v, a.getInt(attrs.index(),
                            Integer.valueOf(attrs.defVal())));
                    break;
                case Attrs.FLOAT:
                    field.setFloat(v, a.getFloat(attrs.index(),
                            Float.valueOf(attrs.defVal())));
                    break;
                case Attrs.BOOLEAN:
                    field.setBoolean(v, a.getBoolean(attrs.index(),
                            Boolean.valueOf(attrs.defVal())));
                    break;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getAnnoSuperClass(Class<?> clazz) {
        Class superclass = clazz;
        while (superclass != null) {
            if (isCompound(superclass)) {
                return superclass;
            }

            superclass = superclass.getSuperclass();
        }

        return clazz;
    }

    private static boolean isCompound(Class<?> clazz) {
        Layout ano = clazz.getAnnotation(Layout.class);
        return ano != null;
    }
}
