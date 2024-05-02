package com.quangph.base.common.converter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pham Hai Quang on 11/11/2019.
 */
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
