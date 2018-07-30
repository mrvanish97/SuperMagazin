package com.uonagent.supermagazin.user

import android.util.Log
import com.uonagent.supermagazin.utils.FirebaseListListener
import com.uonagent.supermagazin.utils.UserViewType

private const val TAG = "UserPresenter"

class UserPresenter(private val mView: UserContract.View) : UserContract.Presenter {

    override fun setAccessPermissions() {
        val type = mView.getViewType()
        Log.d(TAG, type.toString())

        if (UserRepository.currentUserIsAdmin()) {
            when (type) {
                UserViewType.ITEM -> mView
                    UserViewType.LIST
                -> TODO()
                UserViewType.PROFILE -> TODO()
                null -> TODO()
            }
        }
    }

    override fun onLogOutPressed() {
        UserRepository.signOut()
    }

    override fun sendFullListUpdateRequest() {
        val updatedList = arrayListOf<ListItemModel>()
        UserRepository.reloadItemList(object : FirebaseListListener {
            override fun onStart() {
            }

            override fun onAdd(item: ListItemModel) {
                updatedList.add(item)
            }

            override fun onFinish() {
                mView.setListItemArray(updatedList)
            }
        })
    }

    override fun onItemClick() {
        val uid = mView.getItemUid()
        if (UserRepository.currentUserIsAdmin()) {
            mView.makeFieldsClickable()
        } else {
            mView.makeFieldsUnclickable()
        }
        UserRepository.getItemById(uid, {
            mView.getItemFromRepo(it)
        })
    }

    override fun onDestroy() {
    }

    override fun onActivityStarted() {
        if (!UserRepository.currentUserExists()) {
            mView.closeAndBackToLogin()
        }
    }


}