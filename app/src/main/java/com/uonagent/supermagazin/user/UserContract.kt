package com.uonagent.supermagazin.user

import com.uonagent.supermagazin.utils.Contract
import com.uonagent.supermagazin.utils.UserViewType

interface UserContract : Contract {

    interface View : Contract.View {
        fun showErrorMessage(message: String)
        fun setLoadingLayout()
        fun setIdleLayout()
        fun closeAndBackToLogin()
        fun getListItemArray(): MutableList<ListItemModel>?
        fun setListItemArray(listItemArray: MutableList<ListItemModel>)
        fun getItemUid(): String?
        fun makeFieldsClickable()
        fun makeFieldsUnclickable()
        fun getItemFromRepo(item: ListItemModel?)
        fun getViewType(): UserViewType?
        fun setAdminItemPermissions()
        fun setAdminListPermissions()
        fun setUserItemPermissions()
        fun setUserListPermissions()
    }

    interface Presenter : Contract.Presenter {
        fun onActivityStarted()
        fun onDestroy()
        fun sendFullListUpdateRequest()
        fun onLogOutPressed()
        fun onItemClick()
        fun setAccessPermissions()
    }
}