package com.uonagent.supermagazin.user.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.uonagent.supermagazin.R

class ItemRemoveDialog : DialogFragment(), DialogInterface.OnClickListener {

    private var mCallback: OnFragmentInteractionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        mCallback = context as OnFragmentInteractionListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(activity)
                .setTitle(R.string.remove_item_dialog_title)
                .setNegativeButton(R.string.dialog_no, this)
                .setPositiveButton(R.string.dialog_yes, this)
                .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when (which) {
            Dialog.BUTTON_POSITIVE -> mCallback?.onYesClick()
        }
    }

    override fun onDetach() {
        super.onDetach()

        mCallback = null
    }

    interface OnFragmentInteractionListener {
        fun onYesClick()
    }
}