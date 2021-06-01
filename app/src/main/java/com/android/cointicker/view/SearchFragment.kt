package com.android.cointicker.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cointicker.model.Coin
import com.android.cointicker.EventObserver
import com.android.cointicker.R
import com.android.cointicker.adapter.CoinAdapter
import com.android.cointicker.databinding.FragmentSearchBinding
import com.android.cointicker.utils.DetailClickedListener
import com.android.cointicker.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search.*


@AndroidEntryPoint
class SearchFragment : Fragment(), DetailClickedListener {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: CoinAdapter
    private var coinIdList = emptyList<Coin>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchAdapter = CoinAdapter(arrayListOf(), this)
        binding.coinRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.coinRv.adapter = searchAdapter
        binding.errorTv.visibility=View.VISIBLE
        binding.errorTv.text="Search a coin name with at least 3 characters"
        searchOperation()
        setObservers()

    }

    private fun setObservers() {
        viewModel._coinIdListState.observe(viewLifecycleOwner, EventObserver {
            coinIdList = it
        })
        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.VISIBLE
            binding.coinRv.visibility = View.GONE
            binding.errorTv.visibility = View.GONE

        })
        viewModel.loading.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.VISIBLE
            binding.coinRv.visibility = View.GONE
            binding.errorTv.visibility = View.GONE
        })
        viewModel._searchState.observe(viewLifecycleOwner,  {
            if (it.isEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.coinRv.visibility = View.GONE
                binding.errorTv.visibility = View.VISIBLE
                binding.errorTv.text = "Not Found!"
            } else {
                binding.progressBar.visibility = View.GONE
                binding.coinRv.visibility = View.VISIBLE
                binding.errorTv.visibility = View.GONE
                searchAdapter.clear()
                searchAdapter.addList(it)
            }
        })
    }
    private fun searchOperation() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty() && newText.length > 3) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.coinRv.visibility = View.GONE
                    binding.errorTv.visibility = View.GONE
                    viewModel.onSearchQuery(newText)
                } else {
                    searchAdapter.clear()
                    binding.coinRv.visibility = View.VISIBLE
                    binding.errorTv.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                return false
            }
        })
    }

    override fun onClicked(coinName: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(coinName)
        findNavController().navigate(action)
    }


}