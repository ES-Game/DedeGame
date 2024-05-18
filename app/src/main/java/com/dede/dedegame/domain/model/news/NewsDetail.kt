package com.dede.dedegame.domain.model.news

import android.os.Parcel
import com.dede.dedegame.extension.parcel.KParcelable
import com.dede.dedegame.extension.parcel.parcelableCreator

class NewsDetail() : KParcelable {
    var article: Article? = null
    var relatedArticles: List<RelatedArticle>? = null

    constructor(parcel: Parcel) : this() {
        article = parcel.readTypedObject(Article.CREATOR)
        relatedArticles = parcel.createTypedArrayList(RelatedArticle.CREATOR)
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(article, flags)
        if (android.os.Build.VERSION.SDK_INT >= 34) {
            dest.writeTypedList(relatedArticles, flags)
        } else {
            dest.writeTypedList(relatedArticles)
        }
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::NewsDetail)
    }
}