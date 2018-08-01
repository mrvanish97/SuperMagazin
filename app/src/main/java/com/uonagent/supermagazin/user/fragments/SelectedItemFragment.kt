package com.uonagent.supermagazin.user.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.user.UserActivity
import com.uonagent.supermagazin.user.UserRepository
import com.uonagent.supermagazin.utils.CurrencyFormatter
import com.uonagent.supermagazin.utils.models.ItemModel
import kotlinx.android.synthetic.main.fragment_selected_item.view.*
import kotlinx.android.synthetic.main.list_item.view.*

private const val MODEL = "model"

private const val TAG = "SelectedItemFragment"

class SelectedItemFragment : Fragment() {

    private var mCallback: OnFragmentInteractionListener? = null

    private lateinit var mItem: ItemModel

    private lateinit var itemPhoto: ImageView
    private lateinit var itemTitle: TextView
    private lateinit var itemCost: TextView
    private lateinit var itemDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mItem = defaultItem

        if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(MODEL)
        } else {
            arguments?.let {
                mItem = it.getParcelable(MODEL)
            }
        }

        mCallback = context as UserActivity
        (mCallback as UserActivity).onFragmentIsEmpty(mItem.uid.isBlank())
    }

    fun isEmpty() = mItem.uid.isBlank()

    fun getItem() = mItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_selected_item, container, false)

        /*if (savedInstanceState != null && mItem == null) {
            mItem = savedInstanceState.getParcelable(MODEL_INTENT_TAG)
        }
*/
        itemPhoto = view.item_photo
        itemTitle = view.item_title
        itemCost = view.item_cost
        itemDesc = view.item_desc

        setOnClickListeners()

        return view
    }

    private fun setOnClickListeners() {
        itemPhoto.setOnClickListener {
            mCallback?.onPhotoClick()
        }
        itemTitle.setOnClickListener {
            mCallback?.onTitleClick(itemTitle.text.toString())
        }
        itemCost.setOnClickListener {
            mCallback?.onCostClick(itemCost.text.toString())
        }
        itemDesc.setOnClickListener {
            mCallback?.onDescriptionClick(itemDesc.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        draw(mItem)
    }

    private fun draw(model: ItemModel) {
        UserRepository.loadItemPhotoFromStorage(model.photo, itemPhoto, context)
        itemCost.text = CurrencyFormatter.doubleToString(model.cost)
        itemDesc.text = model.description
        itemTitle.text = model.title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(MODEL, mItem)
    }

    fun changeClickability(f: (View?) -> Unit) {
        for (view in arrayOf(itemCost, itemTitle, itemDesc, itemPhoto)) {
            f(view)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mCallback = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallback = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentIsEmpty(state: Boolean)

        fun onPhotoClick()
        fun onTitleClick(data: String)
        fun onCostClick(data: String)
        fun onDescriptionClick(data: String)
    }

    fun reloadItem(item: ItemModel?) {
        if (item != null) {
            mItem = item
            draw(mItem)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelectedItemFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(model: ItemModel?) =
                SelectedItemFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(MODEL, model)
                    }
                }

        private val defaultItem = ItemModel("Title", 0.0, "Description", "", "")

        fun getDefaultItem() = defaultItem
    }

}
