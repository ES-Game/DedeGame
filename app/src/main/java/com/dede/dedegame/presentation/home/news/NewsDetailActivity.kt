package com.dede.dedegame.presentation.home.news

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.usecase.GetNewsDetailAction
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_news_detail)
class NewsDetailActivity : JetActivity<NewsDetailView>() {

    companion object {
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
        getNewsById(articleId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is NewsDetailView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is NewsDetailView.GotoNewsDetailCmd -> {
                launchScreen(this@NewsDetailActivity, command.item.id)
                finish()
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
}