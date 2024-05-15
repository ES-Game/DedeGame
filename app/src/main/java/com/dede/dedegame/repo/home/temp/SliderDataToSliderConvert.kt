package com.dede.dedegame.repo.home.temp

import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.extension.stringToEnum
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.repo.temp.home.SliderData


class SliderDataToSliderConvert : IConverter<SliderData, Slider> {
    override fun convert(source: SliderData): Slider {
        return Slider().apply {
            this.sid = source.sid
            this.image = source.image
            this.type = source.type?.stringToEnum()

        }
    }
}