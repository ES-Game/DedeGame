package com.dede.dedegame.presentation.home.fragments.shop

import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Product
import com.dede.dedegame.domain.model.ProductType
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment

@Layout(R.layout.fragment_shop)
class ShopFragment : JetFragment<ShopFragmentView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        dummyData()
    }

    private fun dummyData() {
        val testList = arrayListOf<Product>()
        val product1 = Product()
        product1.productType = ProductType.GOLD
        product1.quantity = 100
        product1.price = 100.0f
        testList.add(product1)
        val product2 = Product()
        product2.productType = ProductType.SILVER
        product2.quantity = 80
        product2.price = 80.0f
        testList.add(product2)
        val product3 = Product()
        product3.productType = ProductType.COPPER
        product3.quantity = 20
        product3.price = 20.0f
        testList.add(product3)
        val product4 = Product()
        product4.productType = ProductType.COPPER
        product4.quantity = 20
        product4.price = 20.0f
        testList.add(product4)
        val list: List<Product> = testList.toList()
        mvpView.showListStory(list)
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
        }
    }


}