package com.uonagent.supermagazin.user

import android.util.Log
import com.uonagent.supermagazin.utils.FirebaseAuthListener
import com.uonagent.supermagazin.utils.FirebaseListListener
import com.uonagent.supermagazin.utils.ItemModel
import com.uonagent.supermagazin.utils.UserViewType

private const val TAG = "UserPresenter"

class UserPresenter(private val mView: UserContract.View) : UserContract.Presenter {

    override fun setAccessPermissions() {
        val type = mView.getViewType()
        Log.d(TAG, type.toString())

        if (!UserRepository.isUserAnon()) {
            UserRepository.isCurrentUserAdmin(object : FirebaseAuthListener {
                override fun onStart() {
                }

                override fun onSuccess() {
                    when (type) {
                        UserViewType.ITEM -> mView.setAdminItemPermissions()
                        UserViewType.LIST -> mView.setAdminListPermissions()
                    }
                }

                override fun onFailure() {
                    when (type) {
                        UserViewType.ITEM -> mView.setUserItemPermissions()
                        UserViewType.LIST -> mView.setUserListPermissions()
                    }
                }

            })
        } else {
            when (type) {
                UserViewType.ITEM -> mView.setUserItemPermissions()
                UserViewType.LIST -> mView.setUserListPermissions()
            }
        }
    }

    override fun onLogOutPressed() {
        UserRepository.signOut()
    }

    override fun sendFullListUpdateRequest() {
        val updatedList = arrayListOf<ItemModel>()
        UserRepository.reloadItemList(object : FirebaseListListener {
            override fun onStart() {
            }

            override fun onAdd(item: ItemModel) {
                updatedList.add(item)
            }

            override fun onFinish() {
                mView.setListItemArray(updatedList)
            }
        })
    }

    override fun onItemClick() {
        val uid = mView.getItemUid()
        UserRepository.getItemById(uid, {
            mView.getItemFromRepo(it)
        })
        UserRepository.isCurrentUserAdmin(object : FirebaseAuthListener {
            override fun onStart() {
            }

            override fun onSuccess() {
                mView.makeFieldsClickable()
            }

            override fun onFailure() {
                mView.makeFieldsUnclickable()
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

    override fun makeOrder() {
        val item = mView.getSelectedItemForOrder()
        mView.replaceWithOrderView(item)
    }

}