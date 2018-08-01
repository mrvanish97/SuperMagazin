package com.uonagent.supermagazin.user.dialogs

import android.support.v4.app.DialogFragment
import com.uonagent.supermagazin.utils.enums.ItemEditFields

abstract class DialogCreator {
    abstract fun createDialog(): DialogFragment
}

class ItemRemoveDialogCreator : DialogCreator(){
    override fun createDialog(): DialogFragment {
        return ItemRemoveDialog()
    }
}

class ItemFieldEditDialogCreator(
        private val type: ItemEditFields = ItemEditFields.NULL,
        private val data: String = "") : DialogCreator() {

    override fun createDialog(): DialogFragment {
        return ItemFieldEditDialog.newInstance(type, data)
    }
}