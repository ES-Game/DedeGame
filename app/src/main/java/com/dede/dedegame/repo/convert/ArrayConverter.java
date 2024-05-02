package com.dede.dedegame.repo.convert;

import java.util.ArrayList;
import java.util.List;

public class ArrayConverter<S, D> implements IConverter<S[], List<D>> {

    private IConverter<S, D> mConverter;

    public ArrayConverter(IConverter converter) {
        mConverter = converter;
    }

    @Override
    public List<D> convert(S[] s) {
        List<D> result = new ArrayList<>();
        if (s != null) {
            for (int i = 0; i < s.length; i++) {
                result.add(mConverter.convert(s[i]));
            }
        }
        return result;
    }
}
