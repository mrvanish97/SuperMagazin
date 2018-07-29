package com.uonagent.supermagazin.user

import com.uonagent.supermagazin.utils.Contract

interface UserContract : Contract {

    interface View : Contract.View {
        fun showErrorMessage(message: String)
        fun setLoadingLayout()
        fun setIdleLayout()
        fun closeAndBackToLogin()
        fun getListItemArray(): MutableList<ListItemModel>?
        fun setListItemArray(listItemArray: MutableList<ListItemModel>)
    }

    interface Presenter : Contract.Presenter {
        fun onActivityStarted()
        fun onDestroy()
        fun sendFullListUpdateRequest()
    }
}