package com.uonagent.supermagazin.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.user.ListItemModel

private const val TAG = "Repository"

open class Repository : Contract.Repository {

    override fun loadItemPhotoFromStorage(url: String?, dest: ImageView?, context: Context?) {
        val storageRef = url?.let { storage.getReferenceFromUrl(it) }
        val size = getItemPhotoDp(context).toDp()
        Glide.with(context)
                .using(FirebaseImageLoader())
                .load(storageRef)
                .centerCrop()
                .override(size, size)
                .into(dest)
    }

    private fun getItemPhotoDp(context: Context?) : Int {
        if (context != null) {
            return Math.round(context.resources.getDimension(R.dimen.item_photo_size))
        } else {
            throw Exception()
        }
    }

    private fun Int.toDp() : Int {
        return (this / Resources.getSystem().displayMetrics.density).toInt()
    }

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    override fun currentUserExists() = mAuth.currentUser != null

    override fun signInEmail(email: String, password: String, listener: FirebaseAuthListener) {
        listener.onStart()
        email.trim()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                listener.onSuccess()
            } else {
                listener.onFailure()
            }
        }
    }

    override fun signInAnonymously(listener: FirebaseAuthListener) {
        listener.onStart()
        mAuth.signInAnonymously().addOnCompleteListener {
            if (it.isSuccessful) {
                listener.onSuccess()
            } else {
                listener.onFailure()
            }
        }
    }

    override fun reloadItemList(listener: FirebaseListListener) {
        listener.onStart()
        db.collection("items").get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    val item = document.toObject(ListItemModel::class.java)
                    listener.onAdd(item)
                }
                listener.onFinish()
            } else {
                Log.d(TAG, "Error getting documents: ", it.exception);
            }
        }
    }

    override fun addItemListListener(listener: FirebaseListListener) {
        db.collection("items").get()
    }


}