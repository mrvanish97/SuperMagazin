package com.uonagent.supermagazin.user

import android.support.v4.app.DialogFragment
import com.uonagent.supermagazin.user.dialogs.DialogCreator
import com.uonagent.supermagazin.utils.Contract
import com.uonagent.supermagazin.utils.enums.ItemEditFields
import com.uonagent.supermagazin.utils.models.ItemModel
import com.uonagent.supermagazin.utils.enums.UserViewType

interface UserContract : Contract {

    interface View : Contract.View {
        fun setLoadingLayout()
        fun setIdleLayout()
        fun closeAndBackToLogin()
        fun getListItemArray(): MutableList<ItemModel>?
        fun setListItemArray(itemArray: MutableList<ItemModel>)
        fun getItem(): ItemModel?
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
        fun getSelectedItem(): ItemModel?
        fun getSelectedItemForRemoveUid(): String?
        fun showDialog(dialog: DialogFragment)
        fun getDialogCreator(): DialogCreator
    }

    interface Presenter : Contract.Presenter {
        fun onActivityStarted()
        fun onDestroy()
        fun sendFullListUpdateRequest()
        fun onLogOutPressed()
        fun onItemClick()
        fun setAccessPermissions()
        fun makeOrder()
        fun addOrUpdateItem()
        fun removeItem()
        fun openDialog()
    }
}