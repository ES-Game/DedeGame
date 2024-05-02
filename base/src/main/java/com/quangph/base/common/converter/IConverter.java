package com.quangph.base.common.converter;

import androidx.annotation.NonNull;

/**
 * Created by Pham Hai Quang on 1/14/2019.
 */
public interface IConverter<S, D> {
    D convert(@NonNull S source);
}
