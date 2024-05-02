package com.quangph.base.common.converter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuangPH on 2020-01-13.
 */
public class ListConverter<S, D> implements IConverter<List<S>, List<D>> {

    private IConverter<S, D> mConverter;

    public ListConverter(IConverter converter) {
        mConverter = converter;
    }

    @Override
    public List<D> convert(@NonNull List<S> source) {
        List<D> result = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            result.add(mConverter.convert(source.get(i)));
        }
        return result;
    }
}
