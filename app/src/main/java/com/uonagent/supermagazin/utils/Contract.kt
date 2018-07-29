package com.uonagent.supermagazin.utils

import android.content.Context
import android.widget.ImageView

interface Contract {
    
    interface View {
    }

    interface Presenter {
    }

    interface Repository {
        fun currentUserExists(): Boolean
        fun signInEmail(email: String, password: String, listener: FirebaseAuthListener)
        fun signInAnonymously(listener: FirebaseAuthListener)
        fun addItemListListener(listener: FirebaseListListener)
        fun reloadItemList(listener: FirebaseListListener)
        fun loadItemPhotoFromStorage(url: String?, dest: ImageView?, context: Context?)
    }
}