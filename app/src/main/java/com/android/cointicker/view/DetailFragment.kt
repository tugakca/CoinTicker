package com.android.cointicker.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.cointicker.App
import com.android.cointicker.EventObserver
import com.android.cointicker.R
import com.android.cointicker.databinding.FragmentDetailBinding
import com.android.cointicker.model.CoinDetail
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.viewmodel.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {


    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var coinName = ""
    private lateinit var coinDetail: CoinDetail
    private var isFavorite = false
    var RESULT = 1

    var resultLauncher =
        this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                binding.addToFav.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.favorite
                    )
                )
                Toast.makeText(requireContext(), "Coin added to favorites", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            coinName = DetailFragmentArgs.fromBundle(it).coinId
        }
        coinName?.let {
            viewModel.getCoinDetail(coinName)
        }
        setObservers()
        editTextOperations()
        onBackPressed()

        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing=false
            binding.progressBar.visibility = View.VISIBLE
            binding.errorTv.visibility = View.GONE
            binding.mainLay.visibility = View.GONE
            binding.relativeLayout.visibility = View.GONE
            viewModel.getCoinDetail(coinName)
        }
        binding.addToFav.setOnClickListener {
            if (!isFavorite) {
                if (App.userId.isNullOrEmpty()) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    coinDetail.name = coinName
                    intent.putExtra("coinDetail", coinDetail)
                    resultLauncher.launch(intent)
                } else {
                    val userV = CoinPrice(coinName, coinDetail.marketData.currentPrice.usd)
                    viewModel.addToFavorite(userV)

                }
            }
        }
    }

    private fun onBackPressed() {
        binding.backIv.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setObservers() {

        viewModel.addToFavoriteLiveData.observe(viewLifecycleOwner, {
            binding.addToFav.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.favorite
                )
            )
        })
        viewModel.coinDetailLiveData.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.GONE
            binding.errorTv.visibility = View.GONE
            binding.relativeLayout.visibility = View.VISIBLE
            binding.mainLay.visibility = View.VISIBLE
            binding.price = it.marketData
            coinDetail = it
            binding.exchangeEt2.text = it.marketData.currentPrice.usd.toString().toEditable()
            viewModel.isFavorite(coinName)

        })
        viewModel.error.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.GONE
            binding.errorTv.visibility = View.VISIBLE
            binding.mainLay.visibility = View.GONE
            binding.relativeLayout.visibility = View.GONE
            binding.errorTv.text = it.message
        })
        viewModel.loading.observe(viewLifecycleOwner, EventObserver {
            binding.progressBar.visibility = View.VISIBLE
            binding.errorTv.visibility = View.GONE
            binding.mainLay.visibility = View.GONE
            binding.relativeLayout.visibility = View.GONE
        })
        viewModel.isFavoriteLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = View.GONE
            binding.errorTv.visibility = View.GONE
            binding.relativeLayout.visibility = View.VISIBLE
            binding.mainLay.visibility = View.VISIBLE
            coinDetail.isFavorite = it
            isFavorite = it
            binding.model = coinDetail
        })
    }

    fun Editable.toDouble() = toString().toDouble()
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
    private fun editTextOperations() {

        binding.exchangeIv.setOnClickListener {
            var tempText = ""
            var tempValue = ""
            tempValue = binding.exhangeEt1.text.toString()
            tempText = binding.exchangeTv1.text.toString()
            0
            binding.exchangeTv1.text = binding.exchangeTv2.text
            binding.exhangeEt1.text = binding.exchangeEt2.text
            binding.exchangeEt2.text = tempValue.toEditable()
            binding.exchangeTv2.text = tempText


        }
        binding.exhangeEt1.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                if (binding.exchangeTv1.text == "usd") {
                    val doubleValue: Double = it.toDouble()
                    if (doubleValue > 1) {
                        val value = doubleValue / (coinDetail.marketData.currentPrice.usd)
                        binding.exchangeEt2.text = value.toString().toEditable()
                    } else if (doubleValue.toInt() == 0) {
                        binding.exchangeEt2.text = "0".toEditable()
                    }
                } else {
                    val doubleValue: Double = it.toDouble()
                    if (doubleValue >= 1) {
                        val value = doubleValue * (coinDetail.marketData.currentPrice.usd)
                        binding.exchangeEt2.text = value.toString().toEditable()
                    } else if (doubleValue.toInt() == 0) {
                        binding.exchangeEt2.text = "0".toEditable()
                    }
                }
            }
        }
    }


}