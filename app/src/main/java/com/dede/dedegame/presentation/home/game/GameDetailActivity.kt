package com.dede.dedegame.presentation.home.game

import android.content.Context
import android.content.Intent
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.news.NewsDetail
import com.dede.dedegame.domain.usecase.GetNewsDetailAction
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_game_detail)
class GameDetailActivity : JetActivity<GameDetailView>() {

    companion object {
        fun launchScreen(
            context: Context?,
            gameId: Int?
        ) {
            val intent = Intent(context, GameDetailActivity::class.java)
            intent.putExtra("gameId", gameId)
            context?.startActivity(intent)
        }
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        val gameId = intent.getIntExtra("gameId", -1)
        getGameDetailById(gameId)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is GameDetailView.OnBackCmd -> {
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

    private fun getGameDetailById(id: Int) {
//        showLoading()
//
//        val rv = GetNewsDetailAction.RV().apply {
//            this.articleId = id
//        }
//
//        mActionManager.executeAction(
//            GetNewsDetailAction(),
//            rv,
//            object : Action.SimpleActionCallback<NewsDetail>() {
//                override fun onSuccess(responseValue: NewsDetail?) {
//                    super.onSuccess(responseValue)
//                    hideLoading()
//                    responseValue?.let {responseValue ->
//                        responseValue.article?.let { article -> mvpView.fillOwnNewsToGroup(article) }
//                        responseValue.relatedArticles?.let { articles -> mvpView.fillOtherNewsToGroup(articles) }
//                    }
//                }
//
//                override fun onError(e: ActionException) {
//                    super.onError(e)
//                    hideLoading()
//                }
//            })
    }
}