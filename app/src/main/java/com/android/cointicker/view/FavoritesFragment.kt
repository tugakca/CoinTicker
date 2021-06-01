package com.android.cointicker.view

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.cointicker.App
import com.android.cointicker.EventObserver
import com.android.cointicker.R
import com.android.cointicker.adapter.CoinAdapter
import com.android.cointicker.databinding.FragmentFavoritesBinding
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.utils.DetailClickedListener
import com.android.cointicker.viewmodel.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment(),DetailClickedListener {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var coinPriceList :ArrayList<CoinPrice>
    private val viewModel: FavoritesViewModel by viewModels()
    private  var  coinAdapter= CoinAdapter(arrayListOf(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coinPriceList= arrayListOf()
        binding.coinRv.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.coinRv.adapter=coinAdapter

        if(!App.userId.isNullOrEmpty()){
            viewModel.getFavorites()
        }else{
            binding.coinRv.visibility=View.GONE
            binding.errorTv.visibility=View.VISIBLE
            binding.errorTv.text="Please login"
        }
        setObservers()
    }
    private  fun setObservers(){

        viewModel.getFavoriteLiveData.observe(viewLifecycleOwner,{
            binding.coinRv.visibility=View.VISIBLE
            binding.progressBar.visibility=View.GONE
            binding.errorTv.visibility=View.GONE
            coinAdapter.clear()
            coinAdapter.addList(it)

        })

        viewModel.error.observe(viewLifecycleOwner,EventObserver{
            binding.coinRv.visibility=View.GONE
            binding.progressBar.visibility=View.GONE
            binding.errorTv.visibility=View.VISIBLE
            binding.errorTv.text=it.message

        })
        viewModel.loading.observe(viewLifecycleOwner,EventObserver{
            binding.coinRv.visibility=View.GONE
            binding.progressBar.visibility=View.VISIBLE
            binding.errorTv.visibility=View.GONE

        })
    }

    override fun onClicked(coinId: String) {
        val action  =FavoritesFragmentDirections.actionFavoritesFragmentToDetailFragment(coinId)
        findNavController().navigate(action)
    }

}