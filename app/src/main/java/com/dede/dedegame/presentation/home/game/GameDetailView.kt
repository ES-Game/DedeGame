package com.dede.dedegame.presentation.home.game

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView

class GameDetailView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {
    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
//    private val newsDetailAdapter = GroupRclvAdapter()
//    private var ownNewsGroupData: OwnNewsGroupData = OwnNewsGroupData(null)
//    private var otherNewsGroupData: OtherNewsGroupData = OtherNewsGroupData(null)

    override fun onInitView() {
        super.onInitView()
        setupToolbar()
//        rcvInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        rcvInfo?.adapter = newsDetailAdapter
    }

    private fun setupToolbar() {
        val containerBack: View = findViewById(R.id.containerBack)
        val txtStartTitle: TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle: TextView = findViewById(R.id.txtCenterTitle)
        txtStartTitle.text = "Game Detail"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

//    fun fillOwnNewsToGroup(article: Article) {
//        ownNewsGroupData = OwnNewsGroupData(article)
//        newsDetailAdapter.addGroup(ownNewsGroupData)
//        ownNewsGroupData.reset(article)
//        ownNewsGroupData.show()
//    }
//
//    fun fillOtherNewsToGroup(stories: List<RelatedArticle>) {
//        otherNewsGroupData = OtherNewsGroupData(stories)
//        newsDetailAdapter.addGroup(otherNewsGroupData)
//        otherNewsGroupData.reset(stories)
//        otherNewsGroupData.show()
//    }

    class OnBackCmd() : ICommand
}


