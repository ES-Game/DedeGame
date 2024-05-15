package com.dede.dedegame.domain.model.home


import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class Home(): KParcelable {

    var articles: List<Article>? = null
    var comingGames: List<ComingGame>? = null
    var openedGames: List<OpenedGame>? = null
    var sliders: List<Slider>? = null

    constructor(parcel: Parcel) : this() {
        articles = parcel.createTypedArrayList(Article.CREATOR) ?: ArrayList()
        comingGames = parcel.createTypedArrayList(ComingGame.CREATOR) ?: ArrayList()
        openedGames = parcel.createTypedArrayList(OpenedGame.CREATOR) ?: ArrayList()
        sliders = parcel.createTypedArrayList(Slider.CREATOR) ?: ArrayList()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(articles)
        parcel.writeTypedList(comingGames)
        parcel.writeTypedList(openedGames)
        parcel.writeTypedList(sliders)

    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::Home)
    }
}