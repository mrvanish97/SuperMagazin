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
    override fun signOut() {
        mAuth.signOut()
    }


    override fun loadItemPhotoFromStorage(url: String?, dest: ImageView?, context: Context?) {
        if (url != null && url != "") {
            val storageRef = url.let { storage.getReferenceFromUrl(it) }
            val size = getItemPhotoDp(context).toDp()
            Glide.with(context)
                    .using(FirebaseImageLoader())
                    .load(storageRef)
                    .centerCrop()
                    .override(size, size)
                    .into(dest)
        }
    }

    private fun getItemPhotoDp(context: Context?): Int {
        if (context != null) {
            return Math.round(context.resources.getDimension(R.dimen.item_photo_size))
        } else {
            throw Exception()
        }
    }


    private fun Int.toDp(): Int {
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

    override fun currentUserIsAdmin() = isAdminQuery { return@isAdminQuery it }

    private fun isAdminQuery(f: (Boolean) -> Boolean): Boolean {
        db.collection("users").whereEqualTo("uid", mAuth.uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    val type = doc["type"] as String
                    f(type.equals("admin", false))
                }
            }
        }

        return false
    }

    override fun getItemById(uid: String?, f: (ListItemModel?) -> Unit) {
        if (uid != null) {
            db.collection("items").document(uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val item = it.result.toObject(ListItemModel::class.java)
                    f(item)
                }
            }
        }
    }
}