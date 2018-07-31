package com.uonagent.supermagazin.utils

interface FirebaseListListener {
    fun onStart()
    fun onAdd(item: ItemModel)
    fun onFinish()
}