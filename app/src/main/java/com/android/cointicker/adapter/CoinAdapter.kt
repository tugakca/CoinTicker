package com.android.cointicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.cointicker.model.CoinDetail
import com.android.cointicker.R
import com.android.cointicker.databinding.ItemCoinBinding
import com.android.cointicker.model.CoinPrice
import com.android.cointicker.utils.DetailClickedListener

class CoinAdapter (var coinList:ArrayList<CoinPrice>,val detailClickedListener: DetailClickedListener) : RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    private val tempCoinList= arrayListOf<CoinDetail>()
    class CoinViewHolder(var binding: ItemCoinBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinAdapter.CoinViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding =DataBindingUtil.inflate<ItemCoinBinding>(inflater, R.layout.item_coin,parent,false)
        return  CoinViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CoinAdapter.CoinViewHolder, position: Int) {
            holder.binding.model=coinList[position]


        holder.binding.rootLay.setOnClickListener {
            detailClickedListener.onClicked(coinList[position].name)
        }
    }

    override fun getItemCount(): Int {
        return coinList.size
    }


    fun addList(list:List<CoinPrice>){
        coinList.addAll(list)
        notifyDataSetChanged()

    }

//    fun addItem(item:CoinDetail){
//        coinList.add(item)
//        tempCoinList.add(item)
//        notifyDataSetChanged()
//    }
//
    fun clear(){
        coinList.clear()
        notifyDataSetChanged()
    }
//
//    fun clearTempList(){
//        tempCoinList.clear()
//        notifyDataSetChanged()
//    }
//
//    fun addSearchList(searchList:ArrayList<CoinDetail>){
//        coinList.addAll(searchList)
//        notifyDataSetChanged()
//    }
//
//    fun loadOldList(){
//        coinList.addAll(tempCoinList)
//        notifyDataSetChanged()
//    }
}