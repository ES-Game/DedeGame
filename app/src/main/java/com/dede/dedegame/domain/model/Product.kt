package com.dede.dedegame.domain.model

import com.dede.dedegame.R

enum class ProductType(val value: String) {
    GOLD("gold"),
    SILVER("silver"),
    COPPER("copper")
}

class Product {
    var productType: ProductType = ProductType.GOLD
    var name: String? = null
    var quantity: Int? = null
    var price: Float? = null

    fun getProductImage(): Int {
        return when (productType) {
            ProductType.GOLD -> R.drawable.ic_gold
            ProductType.SILVER -> R.drawable.ic_silver
            ProductType.COPPER -> R.drawable.ic_copper
        }
    }

    fun getProductName(): String? {
        return when (productType) {
            ProductType.GOLD -> "Gói vàng"
            ProductType.SILVER -> "Gói bạc"
            ProductType.COPPER -> "Gói đồng"
        }
    }
}