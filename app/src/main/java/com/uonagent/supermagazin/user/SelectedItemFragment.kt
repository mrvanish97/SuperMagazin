package com.uonagent.supermagazin.user

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.uonagent.supermagazin.R
import com.uonagent.supermagazin.utils.CurrencyFormatter
import com.uonagent.supermagazin.utils.ItemModel
import kotlinx.android.synthetic.main.fragment_selected_item.view.*
import kotlinx.android.synthetic.main.list_item.view.*

private const val MODEL = "model"

class SelectedItemFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private var model: ItemModel? = null

    private lateinit var itemPhoto: ImageView
    private lateinit var itemTitle: TextView
    private lateinit var itemCost: TextView
    private lateinit var itemDesc: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            model = savedInstanceState.getParcelable(MODEL)
        } else {
            arguments?.let {
                model = it.getParcelable(MODEL)
            }
        }
    }

    fun getModel(): ItemModel? {
        return model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_selected_item, container, false)

        /*if (savedInstanceState != null && model == null) {
            model = savedInstanceState.getParcelable(MODEL)
        }
*/
        itemPhoto = view.item_photo
        itemTitle = view.item_title
        itemCost = view.item_cost
        itemDesc = view.item_desc

        return view
    }

    override fun onResume() {
        super.onResume()
        draw(model)
    }

    private fun draw(model: ItemModel?) {
        val item = model ?: defaultItem
        UserRepository.loadItemPhotoFromStorage(item.photo, itemPhoto, context)
        itemCost.text = CurrencyFormatter.doubleToString(item.cost)
        itemDesc.text = item.description
        itemTitle.text = item.title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(MODEL, model)
    }

    fun changeClickability(f: (View?) -> Unit) {
        for (view in arrayOf(itemCost, itemTitle, itemDesc, itemPhoto)) {
            f(view)
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
        fun onFragmentInteraction(uri: Uri)
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
    }


}
