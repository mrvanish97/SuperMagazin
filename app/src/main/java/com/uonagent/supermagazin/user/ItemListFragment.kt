package com.uonagent.supermagazin.user

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uonagent.supermagazin.R
import kotlinx.android.synthetic.main.fragment_list.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private const val LIST_STATE_KEY = "list_state_key"
private const val LIST_POSITION = "pos"

private const val TAG = "ItemListFragment"

class ItemListFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null


    private var mCallBack: OnFragmentInteractionListener? = null

    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: ListAdapter

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var list: MutableList<ListItemModel>

    // Following field is being saved but not restored idk why
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLayoutManager = LinearLayoutManager(context)
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt(LIST_POSITION)
            mLayoutManager.scrollToPosition(position)
        }
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        list = ArrayList()

        mAdapter = ListAdapter(list, context)
    }

    fun notifyDatasetChanged() {
        Log.d(TAG, "HELLLLLLLOOOOO0OOO")
        mRecyclerView.adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        mRecyclerView = view.user_list
        mRecyclerView.layoutManager = mLayoutManager
        mRecyclerView.adapter = mAdapter

        mCallBack?.onFragmentViewCreated(list)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mCallBack = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mCallBack = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentViewCreated(list: MutableList<ListItemModel>)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val currentPosition = (mRecyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()

        outState.putInt(LIST_POSITION, currentPosition)

        //outState.putParcelable(LIST_STATE_KEY, mRecyclerView.layoutManager.onSaveInstanceState())
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ItemListFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }

        private var mBundleRecyclerViewState: Bundle? = null
    }
}
