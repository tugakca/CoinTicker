package com.android.cointicker.view

import android.os.Build
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.cointicker.EventObserver
import com.android.cointicker.R
import com.android.cointicker.adapter.CoinAdapter
import com.android.cointicker.databinding.FragmentHomepageBinding
import com.android.cointicker.model.Coin
import com.android.cointicker.utils.DetailClickedListener
import com.android.cointicker.viewmodel.HomepageViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomepageFragment : Fragment(), DetailClickedListener {
    private lateinit var binding: FragmentHomepageBinding
    private lateinit var coinAdapter: CoinAdapter
    private val viewModel: HomepageViewModel by viewModels()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var begin = 0
    private var coinIdList = arrayListOf<Coin>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_homepage, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        begin=0
        viewModel.getAllCoinList()
        coinAdapter = CoinAdapter(arrayListOf(), this)
        linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.coinRv.layoutManager = linearLayoutManager
        binding.coinRv.adapter = coinAdapter
        setObservers()
        pullToRefresh()
        pagination()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun setObservers() {
        viewModel.idListLiveData.observe(viewLifecycleOwner, EventObserver {
            coinIdList.clear()
            coinIdList.addAll(it)
            viewModel.getCoinListWithPrice(it, begin = begin)
            begin += 20

        })
        viewModel.priceListLiveData.observe(viewLifecycleOwner, EventObserver {
            successState()
            coinAdapter.addList(it)
        })

        viewModel.loading.observe(viewLifecycleOwner, EventObserver {
            loadingState()

        })

        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            errorState(it)
        })

    }

    private fun pullToRefresh() {
        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing = false
            binding.coinRv.visibility = View.GONE
            binding.errorTv.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            coinAdapter.clear()
            begin = 0
            viewModel.getAllCoinList()
        }
    }


    private fun loadingState() {

        binding.coinRv.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.errorTv.visibility = View.GONE

    }

    private fun errorState(e: Exception) {

        binding.coinRv.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.errorTv.visibility = View.VISIBLE
        binding.errorTv.text = e.message


    }

    private fun successState() {
        binding.coinRv.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.errorTv.visibility = View.GONE
    }


    private fun pagination() {
        binding.coinRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val currentItemsCount = linearLayoutManager.childCount
                val lastVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                if (currentItemsCount + lastVisibleItem >= coinAdapter.itemCount && dy > 0) {
                    if (binding.progressBar.visibility == View.GONE) {
                        binding.progressBar.visibility = View.VISIBLE
                        viewModel.getCoinListWithPrice(coinIdList, begin)
                        binding.progressBar.visibility = View.VISIBLE
                        binding.errorTv.visibility = View.GONE
                        begin += 20
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun onClicked(coinName: String) {
        val action = HomepageFragmentDirections.actionHomepageFragmentToDetailFragment(coinName)
        findNavController().navigate(action)
    }


}