package com.dede.dedegame.presentation.home.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.usecase.GetNewsDetailAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_news_detail)
class NewsDetailActivity : JetActivity<NewsDetailView>() {

    companion object {

        const val EVENT_ON_NEWS_DETAIL = "event_on_news_detail"
        const val EVENT_TAP_OTHER_NEWS_ITEM = "event_tap_other_news_item"
        const val PARAM_NEWS = "news_id"

        fun launchScreen(
            context: Context?,
            idArticle: Int?
        ) {
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra("articleId", idArticle)
            context?.startActivity(intent)
        }
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        val articleId = intent.getIntExtra("articleId", -1)
        trackingOnNewsDetailScreen(PARAM_NEWS, articleId)
        getNewsById(articleId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is NewsDetailView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is NewsDetailView.GotoNewsDetailCmd -> {
                trackingTapEventNewsDetail(EVENT_TAP_OTHER_NEWS_ITEM, PARAM_NEWS, command.item.id)
                launchScreen(this@NewsDetailActivity, command.item.id)
            }
        }
    }

    private fun getNewsById(id: Int) {
        showLoading()

        val rv = GetNewsDetailAction.RV().apply {
            this.articleId = id
        }

        mActionManager.executeAction(
            GetNewsDetailAction(),
            rv,
            object : Action.SimpleActionCallback<NewsDetail>() {
                override fun onSuccess(responseValue: NewsDetail?) {
                    super.onSuccess(responseValue)
                    hideLoading()
                    responseValue?.let { responseValue ->
                        responseValue.article?.let { article ->
                            mvpView.setTitleToolbar(article.title!!)
                            mvpView.fillOwnNewsToGroup(article)
                        }
                        responseValue.relatedArticles?.let { articles ->
                            mvpView.fillOtherNewsToGroup(
                                articles
                            )
                        }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@NewsDetailActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun trackingOnNewsDetailScreen(param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_NEWS_DETAIL
            this.param = "on_screen"
            this.paramValue = "on_screen"
            this.param2 = param
            this.paramValue2 = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventNewsDetail(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@NewsDetailActivity.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""
        var param2: String = ""
        var paramValue2: String = ""

        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
            bundle.putString(param2, paramValue2)
        }
    }

}