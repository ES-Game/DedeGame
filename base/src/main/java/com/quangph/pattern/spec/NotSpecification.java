package com.quangph.pattern.spec;

/**
 * Created by Pham Hai QUANG on 9/13/2016.
 */
public class NotSpecification<T> extends AbstractSpecification<T> {
    private ISpecification<T> spec1;

    public NotSpecification(final ISpecification<T> spec1) {
        this.spec1 = spec1;
    }

    @Override
    public boolean isSatisfiedBy(T t) {
        return !spec1.isSatisfiedBy(t);
    }
}
