package com.uonagent.supermagazin.utils

import com.uonagent.supermagazin.user.ListItemModel

interface FirebaseListListener {
    fun onStart()
    fun onAdd(item: ListItemModel)
}