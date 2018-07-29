package com.uonagent.supermagazin.utils

import android.graphics.Bitmap
import java.net.URL

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
        fun initialize(listener: FirebaseListListener)
        fun loadImageFromStorage(url: URL)
        fun listItemPhotoProcess(bitmap: Bitmap)
    }
}