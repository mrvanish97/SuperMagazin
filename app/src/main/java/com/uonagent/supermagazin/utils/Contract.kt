package com.uonagent.supermagazin.utils

import android.content.Context
import android.widget.ImageView
import com.uonagent.supermagazin.user.ListItemModel

interface Contract {
    
    interface View {
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
        fun currentUserIsAdmin(): Boolean
        fun getItemById(uid: String?, f: (ListItemModel?) -> Unit)
    }
}