package com.uonagent.supermagazin.user

import com.uonagent.supermagazin.utils.Contract
import com.uonagent.supermagazin.utils.ItemModel
import com.uonagent.supermagazin.utils.UserViewType

interface UserContract : Contract {

    interface View : Contract.View {
        fun showErrorMessage(message: String)
        fun setLoadingLayout()
        fun setIdleLayout()
        fun closeAndBackToLogin()
        fun getListItemArray(): MutableList<ItemModel>?
        fun setListItemArray(itemArray: MutableList<ItemModel>)
        fun getItemUid(): String?
        fun makeFieldsClickable()
        fun makeFieldsUnclickable()
        fun getItemFromRepo(item: ItemModel?)
        fun getViewType(): UserViewType?
        fun setAdminItemPermissions()
        fun setAdminListPermissions()
        fun setUserItemPermissions()
        fun setUserListPermissions()
        fun replaceWithOrderView(item: ItemModel?)
        fun getSelectedItemForOrder(): ItemModel?
    }

    interface Presenter : Contract.Presenter {
        fun onActivityStarted()
        fun onDestroy()
        fun sendFullListUpdateRequest()
        fun onLogOutPressed()
        fun onItemClick()
        fun setAccessPermissions()
        fun makeOrder()
    }
}