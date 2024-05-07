package com.dede.dedegame.presentation.home

import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity
import com.dede.dedegame.R

@Layout(R.layout.activity_home)
class HomeActivity : JetActivity<HomeView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        requestFragmentManager()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
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
    }