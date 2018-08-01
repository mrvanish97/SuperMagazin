package com.uonagent.supermagazin.user.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.View
import android.widget.EditText
import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.utils.enums.ItemEditFields

private const val TYPE_TAG = "type"
private const val DATA_TAG = "data"

class ItemFieldEditDialog : DialogFragment(), DialogInterface.OnShowListener {

    companion object {

        fun newInstance(type: ItemEditFields, data: String): ItemFieldEditDialog {
            val dialog = ItemFieldEditDialog()

            val args = Bundle()
            args.putString(TYPE_TAG, type.string)
            args.putString(DATA_TAG, data)

            dialog.arguments = args

            return dialog
        }

    }

    private lateinit var v: View

    private var mCallback: ItemFieldEditDialog.OnFragmentInteractionListener? = null

    private lateinit var mTextInputLayout: TextInputLayout
    private lateinit var mEditText: EditText

    private lateinit var mType: String
    private lateinit var mData: String

    /*override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.dialog_item_edit, container, false)

        mTextInputLayout = v.findViewById(R.id.edit_dialog_textlayout)
        mEditText = v.findViewById(R.id.edit_dialog_edittext)

        mEditText.setText(mData)

        return v
    }*/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mType = arguments!!.getString(TYPE_TAG)
        mData = arguments!!.getString(DATA_TAG)

        mCallback = context as ItemFieldEditDialog.OnFragmentInteractionListener

        v = activity!!.layoutInflater.inflate(R.layout.dialog_item_edit, ConstraintLayout(context), false)

        mTextInputLayout = v.findViewById(R.id.edit_dialog_textlayout)
        mEditText = v.findViewById(R.id.edit_dialog_edittext)

        mEditText.setText(mData)

        return AlertDialog.Builder(activity)
                .setTitle(mType)
                .setView(v)
                .setNeutralButton(R.string.dialog_cancel, null)
                .setPositiveButton(R.string.dialog_apply, null)
                .create()
    }

    override fun onShow(p0: DialogInterface?) {
        val button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
        button.setOnClickListener {
            mData = mEditText.text.toString()
            if (!mData.isBlank()) {
                Log.d("DDDDDDDDDDD", "DDDDDDDDDD")
                mCallback?.onApplyClick(mData)
                dialog.dismiss()
            } else {
                setEmptyFieldError()
            }
        }
    }

    private fun setEmptyFieldError() {
        val message = activity!!.resources.getString(R.string.dialog_empty_error)
        mTextInputLayout.isErrorEnabled
        mTextInputLayout.error = message
    }

    interface OnFragmentInteractionListener {
        fun onApplyClick(data: String)
    }
}