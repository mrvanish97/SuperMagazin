package com.uonagent.supermagazin.utils

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.utils.listeners.FirebaseEditListener
import com.uonagent.supermagazin.utils.listeners.FirebaseAuthListener
import com.uonagent.supermagazin.utils.listeners.FirebaseListListener
import com.uonagent.supermagazin.utils.models.ItemModel
import com.uonagent.supermagazin.utils.models.OrderModel

private const val TAG = "Repository"

private const val ORDERS_COLLECTION = "orders"
private const val ITEMS_COLLECTION = "items"
private const val USERS_COLLECTION = "users"
private const val TYPE_KEY = "type"
private const val TYPE_ADMIN = "admin"
private const val UID_KEY = "uid"
private const val AUTH_UID_KEY = "authuid"
private const val COST_KEY = "cost"

private const val UNKNOWN_ERROR_MESSAGE = "Unknown Error"

open class Repository : Contract.Repository {
    override fun addOrder(order: OrderModel, listener: FirebaseEditListener) {
        db.collection(ORDERS_COLLECTION).add(order).addOnSuccessListener {
            addUidToDocument(it, listener)
        }.addOnFailureListener {
            listener.onFailure(UNKNOWN_ERROR_MESSAGE)
        }
    }

    private fun addUidToDocument(documentReference: DocumentReference, listener: FirebaseEditListener) {
        documentReference.update(UID_KEY, documentReference.id).addOnSuccessListener {
            listener.onSuccess()
        }.addOnFailureListener {
            listener.onFailure(UNKNOWN_ERROR_MESSAGE)
        }
    }

    override fun isUserAnon(): Boolean {
        return mAuth.currentUser!!.isAnonymous
    }

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
        db.collection(ITEMS_COLLECTION).orderBy(COST_KEY).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result) {
                    val item = document.toObject(ItemModel::class.java)
                    listener.onAdd(item)
                }
                listener.onFinish()
            } else {
                Log.d(TAG, "Error getting documents: ", it.exception);
            }
        }
    }

    override fun addItemListListener(listener: FirebaseListListener) {
        db.collection(ITEMS_COLLECTION).get()
    }

    override fun getAdminPermissions(listener: FirebaseAuthListener) {
        db.collection(USERS_COLLECTION).whereEqualTo(AUTH_UID_KEY, mAuth.uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    val type = doc[TYPE_KEY] as String
                    Log.d(TAG, type)
                    if (type == TYPE_ADMIN) {
                        Log.d(TAG, "THIS USER IS AN ADMIN")
                        listener.onSuccess()
                    } else {
                        listener.onFailure()
                        Log.d(TAG, "THIS USER IS A PLAIN USER")
                    }
                }
            }
        }
    }

    override fun getItemById(uid: String?, f: (ItemModel?) -> Unit) {
        if (uid != null) {
            db.collection(ITEMS_COLLECTION).document(uid).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val item = it.result.toObject(ItemModel::class.java)
                    f(item)
                }
            }
        }
    }

    override fun addItem(item: ItemModel, listener: FirebaseEditListener) {
        db.collection(ITEMS_COLLECTION).add(item).addOnSuccessListener {
            addUidToDocument(it, listener)
        }.addOnFailureListener {
            listener.onFailure(UNKNOWN_ERROR_MESSAGE)
        }
    }

    override fun updateItem(item: ItemModel, listener: FirebaseEditListener) {
        db.collection(ITEMS_COLLECTION).document(item.uid).update(item.asMap()).addOnSuccessListener {
            listener.onSuccess()
        }.addOnFailureListener {
            listener.onFailure(UNKNOWN_ERROR_MESSAGE)
        }
    }

    override fun removeItem(uid: String, listener: FirebaseEditListener) {
        db.collection(ITEMS_COLLECTION).document(uid).delete().addOnSuccessListener {
            listener.onSuccess()
        }.addOnFailureListener {
            listener.onFailure(UNKNOWN_ERROR_MESSAGE)
        }
    }
}