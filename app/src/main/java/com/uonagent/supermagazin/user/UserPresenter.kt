package com.uonagent.supermagazin.user

import android.util.Log
import com.uonagent.supermagazin.utils.listeners.FirebaseAuthListener
import com.uonagent.supermagazin.utils.listeners.FirebaseListListener
import com.uonagent.supermagazin.utils.models.ItemModel
import com.uonagent.supermagazin.utils.enums.UserViewType
import com.uonagent.supermagazin.utils.listeners.FirebaseEditListener

private const val TAG = "UserPresenter"

private const val SUCCESS_MESSAGE = "Successful"
private const val FAILURE_MESSAGE = "Nu takoje"
private const val EMPTY_FIELD_ERROR = "Some fields are blank"

class UserPresenter(private val mView: UserContract.View) : UserContract.Presenter {

    override fun setAccessPermissions() {
        val type = mView.getViewType()
        Log.d(TAG, type.toString())

        if (!UserRepository.isUserAnon()) {
            UserRepository.getAdminPermissions(object : FirebaseAuthListener {
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
        val uid = mView.getSelectedItem()?.uid
        UserRepository.getItemById(uid, {
            mView.getItemFromRepo(it)
        })
        UserRepository.getAdminPermissions(object : FirebaseAuthListener {
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

    override fun addItem() {
        updateItem()
    }

    override fun updateItem() {

        val item = mView.getSelectedItem()
        if (item != null) {
            if (mView.getPhotoWasChanged()) {
                UserRepository.loadImageToStorage(item.photo, item.uid, {
                    item.photo = it
                    Log.d(TAG, it)
                    addOrUpdateItem(item)
                })
            } else {
                addOrUpdateItem(item)
            }
        }
    }


    private fun addOrUpdateItem(item: ItemModel) {

        val listener = object : FirebaseEditListener {
            override fun onSuccess() {
                mView.showInfoMessage(SUCCESS_MESSAGE)
            }

            override fun onFailure(message: String) {
                mView.showErrorMessage(FAILURE_MESSAGE)
            }

        }

        if (item != null) {
            if (item.uid.isBlank()) {
                UserRepository.addItem(item, listener)
            } else {
                UserRepository.updateItem(item, listener)
            }
        }
    }

    override fun removeItem() {
        val uid = mView.getSelectedItemForRemoveUid()
        if (uid != null) {
            UserRepository.removeItem(uid, object : FirebaseEditListener {
                override fun onSuccess() {
                    mView.showInfoMessage(SUCCESS_MESSAGE)
                }

                override fun onFailure(message: String) {
                    mView.showErrorMessage(FAILURE_MESSAGE)
                }
            })
        } else {
            mView.showErrorMessage(FAILURE_MESSAGE)
        }
    }

    override fun openDialog() {
        val dialogCreator = mView.getDialogCreator()
        val dialog = dialogCreator.createDialog()
        mView.showDialog(dialog)
    }

}