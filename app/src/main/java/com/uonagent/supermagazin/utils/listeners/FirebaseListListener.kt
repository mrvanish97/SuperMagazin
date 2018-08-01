package com.uonagent.supermagazin.utils.listeners

import com.uonagent.supermagazin.utils.models.ItemModel

interface FirebaseListListener {
    fun onStart()
    fun onAdd(item: ItemModel)
    fun onFinish()
}