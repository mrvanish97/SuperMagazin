package com.uonagent.supermagazin.user

import android.util.Log
import com.uonagent.supermagazin.utils.FirebaseListListener

private const val TAG = "UserPresenter"

class UserPresenter(private val mView: UserContract.View) : UserContract.Presenter {
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

    override fun onDestroy() {
    }

    override fun onActivityStarted() {
        if (!UserRepository.currentUserExists()) {
            mView.closeAndBackToLogin()
        }
    }



}