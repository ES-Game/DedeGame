package com.quangph.pattern.spec;

public interface ICompoundSpec<T> extends ISpecification<T> {
    ICompoundSpec<T> and(ICompoundSpec<T> specification);
    ICompoundSpec<T> or(ICompoundSpec<T> specification);
    ICompoundSpec<T> not(ICompoundSpec<T> specification);
}
