package com.dede.dedegame.repo.convert;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
public class ListConverter<S, D> implements IConverter<List<S>, List<D>> {

    private IConverter<S, D> mConverter;

    public ListConverter(IConverter<S, D> converter) {
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
