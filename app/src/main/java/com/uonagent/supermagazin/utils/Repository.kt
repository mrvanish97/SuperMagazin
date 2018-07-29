package com.uonagent.supermagazin.utils

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.uonagent.supermagazin.user.ListItemModel
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Repository"

open class Repository : Contract.Repository {

    private val mAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

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

    override fun initialize(listener: FirebaseListListener) {
        listener.onStart()
        db.collection("items").get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    val item = document.toObject(ListItemModel::class.java)
                    listener.onAdd(item)
                }
            } else {
                Log.d(TAG, "Error getting documents: ", it.exception);
            }
        }
    }

    override fun addItemListListener(listener: FirebaseListListener) {
        db.collection("items").get()
    }


}