package com.quangph.pattern.spec;

/**
 * Created by Pham Hai QUANG on 9/13/2016.
 */
public abstract class AbstractSpecification<T> implements ICompoundSpec<T> {

    @Override
    public ICompoundSpec<T> and(ICompoundSpec<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    @Override
    public ICompoundSpec<T> or(ICompoundSpec<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

    @Override
    public ICompoundSpec<T> not(ICompoundSpec<T> specification) {
        return new NotSpecification<T>(specification);
    }
}
