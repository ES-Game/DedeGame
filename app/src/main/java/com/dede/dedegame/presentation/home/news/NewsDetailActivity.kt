package com.dede.dedegame.presentation.home.news

import android.content.Context
import android.content.Intent
import android.util.Log
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.usecase.GetNewsDetailAction
import com.dede.dedegame.domain.usecase.GetStoryDetailAction
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
//            is HomeView.SubmitBookCmd -> {
//                val book = Book().apply {
//                    this.title = command.bookTitle
//                    this.displayTitle = command.bookTitle
//                    this.author = command.bookAuthor
//                    this.publisher = command.bookPublisher
//                    this.description = command.bookDes
//                }
//
//                val intent = Intent(this, ListBookActivity::class.java).apply {
//                    this.action = ListBookActivity.EXTRA_BOOK_ADDED_ACTION
//                    this.putExtra(ListBookActivity.EXTRA_BOOK_ADDED_KEY, book)
//                    this.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                }
//
//                startActivity(intent)
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
                    responseValue?.let {responseValue ->
                        responseValue.article?.let { article -> mvpView.fillOwnNewsToGroup(article) }
                        responseValue.relatedArticles?.let { articles -> mvpView.fillOtherNewsToGroup(articles) }
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                }
            })
    }
}