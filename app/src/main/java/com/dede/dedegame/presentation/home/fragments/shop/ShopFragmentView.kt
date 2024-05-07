package com.dede.dedegame.presentation.home.fragments.shop

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Product
import com.dede.dedegame.presentation.common.GridSpacingItemDecoration
import com.dede.dedegame.presentation.home.fragments.shop.groups.ListPackageGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class ShopFragmentView(context: Context?, attrs: AttributeSet?) :
    BaseConstraintView(context, attrs) {

    private var rcvPackages: RecyclerView? = null

    private val contentAdapter = GroupRclvAdapter()
    private val listPackageGroupData = ListPackageGroupData(null)

    override fun onInitView() {
        super.onInitView()

        rcvPackages = findViewById(R.id.rcvPackages)
        rcvPackages!!.layoutManager =
            GridLayoutManager(context, 3)
        rcvPackages!!.adapter = contentAdapter
        rcvPackages?.addItemDecoration(
            GridSpacingItemDecoration(
                3,
                20,
                true
            )
        )
        contentAdapter.addGroup(listPackageGroupData)
        listPackageGroupData.onClickStoryItem = object : ListPackageGroupData.OnClickStoryItem {
            override fun onClickStoryItem(item: Product) {
                mPresenter.executeCommand(GotoStoryDetailCmd(item))
            }
        }

    }

    fun showListStory(data: List<Product>) {
        listPackageGroupData.reset(data)
        listPackageGroupData.show()
    }

    class GotoStoryDetailCmd(val item: Product) : ICommand
}


