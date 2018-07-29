package com.uonagent.supermagazin.user

data class ListItemModel(
        var title: String = "",
        var cost: Double = Double.NaN,
        var description: String = "",
        var photo: ByteArray? = null,
        var uid: String = ""
)