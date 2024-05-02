package com.dede.dedegame.repo.convert;

import androidx.annotation.NonNull;

public interface IConverter<S, D> {
    D convert(@NonNull S source);
}
