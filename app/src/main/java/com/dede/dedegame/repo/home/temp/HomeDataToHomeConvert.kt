package com.dede.dedegame.repo.home.temp

import com.dede.dedegame.repo.convert.ListConverter
import com.dede.dedegame.repo.convert.IConverter
import com.dede.dedegame.domain.model.Author
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.Tag
import com.dede.dedegame.domain.model.home.Article
import com.dede.dedegame.domain.model.home.ComingGame
import com.dede.dedegame.domain.model.home.Home
import com.dede.dedegame.domain.model.home.OpenedGame
import com.dede.dedegame.domain.model.home.Slider
import com.dede.dedegame.repo.temp.home.ArticleData
import com.dede.dedegame.repo.temp.home.ComingGameData
import com.dede.dedegame.repo.temp.home.HomeData
import com.dede.dedegame.repo.temp.home.OpenedGameData
import com.dede.dedegame.repo.temp.home.SliderData


class HomeDataToHomeConvert: IConverter<HomeData, Home> {
    override fun convert(source: HomeData): Home {
        return Home().apply {
            this.articles = source.articles?.let {
                ListConverter<ArticleData, Article>(ArticleDataToArticleConvert()).convert(
                    it
                )
            }
            this.comingGames = source.comingGames?.let {
                ListConverter<ComingGameData, ComingGame>(ComingGamesDataToComingGamesConvert()).convert(
                    it
                )
            }
            this.openedGames = source.openedGames?.let {
                ListConverter<OpenedGameData, OpenedGame>(OpenedGamesDataToOpenedGamesConvert()).convert(
                    it
                )
            }
            this.sliders = source.sliders?.let {
                ListConverter<SliderData, Slider>(SliderDataToSliderConvert()).convert(
                    it
                )
            }
        }
    }
}