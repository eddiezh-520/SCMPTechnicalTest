package com.scmp.android.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.scmp.android.R
import com.scmp.android.adapter.StaffInfoAdapter
import com.scmp.android.databinding.FragmentLoginBinding
import com.scmp.android.databinding.FragmentStaffBinding
import com.scmp.android.model.LoadMore
import com.scmp.android.model.StaffInfo
import com.scmp.android.viewmodel.StaffLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StaffFragment : Fragment() {

    val mBinding by lazy {
        FragmentStaffBinding.inflate(layoutInflater)
    }

    val mAdapter by lazy {
        StaffInfoAdapter(requireContext())
    }

    private val viewModel: StaffLoginViewModel by activityViewModels()

    var page = 1
    var totalPage =1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        fetchData()
        initObserver()
        initListener()
    }

    private fun initView() {
        mBinding.token.text = viewModel.resultToken.value
        mBinding.recyclerview.adapter = mAdapter
    }

    private fun fetchData() {
        viewModel.getStaffs(page)
    }

    private fun initObserver() {
       viewModel.staffList.observe(viewLifecycleOwner) {
           totalPage = it.totalPages
           mAdapter.submitList(it.data)
       }
       viewModel.loadMoreStaffList.observe(viewLifecycleOwner) {
           lifecycleScope.launch {
               delay(5000)
               viewModel.appendListData(mAdapter,it)
           }
       }

    }

    private fun initListener() {
        mBinding.recyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //The number of items seen on the current screen
                    val childCount = recyclerView.childCount
                    //or
                    //val childCount = mRecyclerView.layoutManager.childCount
                    //number of all items of the RecyclerView
                    val itemCount = recyclerView.layoutManager?.itemCount
                    //The position of the first visible item in the screen
                    val firstVisibleItem =
                        (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    //load more list
                    if (firstVisibleItem + childCount == itemCount) {
                        //have slide to the bottom...
                        lifecycleScope.launch {
                            page++
                            if (page <= totalPage) {
                                viewModel.addLoading(mAdapter)
                                viewModel.getLoadMoreStaffs(page)
                            }
                        }
                    }
                }

            }
        })
    }

}