package com.quangph.pattern.spec;

/**
 * Created by Pham Hai QUANG on 9/13/2016.
 */
public class AndSpecification<T> extends AbstractSpecification<T> {

    private ISpecification<T> spec1;
    private ISpecification<T> spec2;

    public AndSpecification(final ISpecification<T> spec1, final ISpecification<T> spec2) {
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    @Override
    public boolean isSatisfiedBy(T t) {
        return spec1.isSatisfiedBy(t) && spec2.isSatisfiedBy(t);
    }
}
