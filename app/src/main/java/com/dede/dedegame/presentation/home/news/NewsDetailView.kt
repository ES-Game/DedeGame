package com.dede.dedegame.presentation.home.news

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.news.Article
import com.dede.dedegame.domain.model.news.RelatedArticle
import com.dede.dedegame.presentation.home.news.groups.OtherNewsGroupData
import com.dede.dedegame.presentation.home.news.groups.OwnNewsGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class NewsDetailView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {
    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
    private val containerBack by lazy { findViewById<View>(R.id.containerBack) }
    private val txtStartTitle by lazy { findViewById<TextView>(R.id.txtStartTitle) }
    private val txtCenterTitle by lazy { findViewById<TextView>(R.id.txtCenterTitle) }
    private val newsDetailAdapter = GroupRclvAdapter()
    private var ownNewsGroupData: OwnNewsGroupData = OwnNewsGroupData(null)
    private var otherNewsGroupData: OtherNewsGroupData = OtherNewsGroupData(null)

    override fun onInitView() {
        super.onInitView()
        setupToolbar()
        rcvInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvInfo?.adapter = newsDetailAdapter

        otherNewsGroupData.onClickListener = object : OtherNewsGroupData.OnClickListener {
            override fun onClickOtherNews(item: RelatedArticle) {
                mPresenter.executeCommand(GotoNewsDetailCmd(item))
            }
        }
        newsDetailAdapter.addGroup(ownNewsGroupData)
    }

    private fun setupToolbar() {
        txtStartTitle.text = "News Detail"
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    fun setTitleToolbar(title: String) {
        txtStartTitle.text = title
    }

    fun fillOwnNewsToGroup(article: Article) {
        ownNewsGroupData.reset(article)
        ownNewsGroupData.show()
    }

    fun fillOtherNewsToGroup(stories: List<RelatedArticle>) {
        newsDetailAdapter.addGroup(otherNewsGroupData)
        otherNewsGroupData.reset(stories)
        otherNewsGroupData.show()
    }

    class OnBackCmd() : ICommand
    class GotoNewsDetailCmd(val item: RelatedArticle) : ICommand
}


