package com.quangph.pattern.spec;

/**
 * Validate pattern
 * Created by Pham Hai QUANG on 9/13/2016.
 */
public interface ISpecification<T> {
    boolean isSatisfiedBy(T t);

}
