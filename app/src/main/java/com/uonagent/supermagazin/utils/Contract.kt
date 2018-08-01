package com.uonagent.supermagazin.utils

import android.content.Context
import android.widget.ImageView
import com.uonagent.supermagazin.utils.listeners.FirebaseEditListener
import com.uonagent.supermagazin.utils.listeners.FirebaseAuthListener
import com.uonagent.supermagazin.utils.listeners.FirebaseListListener
import com.uonagent.supermagazin.utils.models.ItemModel
import com.uonagent.supermagazin.utils.models.OrderModel

interface Contract {
    
    interface View {
        fun showErrorMessage(message: String)
        fun showInfoMessage(message: String)
    }

    interface Presenter {
    }

    interface Repository {
        fun currentUserExists(): Boolean
        fun signInEmail(email: String, password: String, listener: FirebaseAuthListener)
        fun signInAnonymously(listener: FirebaseAuthListener)
        fun signOut()
        fun addItemListListener(listener: FirebaseListListener)
        fun reloadItemList(listener: FirebaseListListener)
        fun loadItemPhotoFromStorage(url: String?, dest: ImageView?, context: Context?)
        fun getAdminPermissions(listener: FirebaseAuthListener)
        fun getItemById(uid: String?, f: (ItemModel?) -> Unit)
        fun isUserAnon(): Boolean
        fun addOrder(order: OrderModel, listener: FirebaseEditListener)
        fun addItem(item: ItemModel, listener: FirebaseEditListener)
        fun updateItem(item: ItemModel, listener: FirebaseEditListener)
        fun removeItem(uid: String, listener: FirebaseEditListener)
    }
}