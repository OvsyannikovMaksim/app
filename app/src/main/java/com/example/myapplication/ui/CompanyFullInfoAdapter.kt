package com.example.myapplication.ui

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.IListener
import com.example.myapplication.R
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.databinding.StockItemBinding

class CompanyFullInfoAdapter(private var mListener: IListener) :
        ListAdapter<CompanyInfoDst, CompanyFullInfoAdapter.CompanyVH>(DiffCallback){

    private lateinit var binding: StockItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyVH {

        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        binding = StockItemBinding.inflate(inflater,parent, false)
        return CompanyVH(binding, mListener)

    }

    override fun onBindViewHolder(holder: CompanyVH, position: Int) {
        holder.bind(currentList[position], position)
    }




    class CompanyVH (private var itemBinding: StockItemBinding, private var mListener: IListener):
        RecyclerView.ViewHolder(itemBinding.root){

        fun bind(companyInfo: CompanyInfoDst, position: Int){

            when(companyInfo.priceChange<0.0){
                true->itemBinding.companyPriceChange.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                false->itemBinding.companyPriceChange.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
            }
            when(position%2==1){
                true->itemBinding.root.setCardBackgroundColor(Color.WHITE)
                false->itemBinding.root.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.light_blue))
            }

            Glide.with(itemView.context).load(Uri.parse(companyInfo.logo)).error(R.drawable.ic_img_7400)
                    .into(itemBinding.companyPic)
            itemBinding.companyName.text=companyInfo.name
            itemBinding.companyPrice.text="$"+companyInfo.curPrice.toString()
            itemBinding.companyTicker.text=companyInfo.ticker
            itemBinding.companyPriceChange.text=createChangeString(companyInfo.priceChange,companyInfo.priceChangePercent)
            itemBinding.favButton.isChecked=companyInfo.isFavorite
            itemBinding.favButton.setOnCheckedChangeListener{ _,
            isChecked->mListener.pressButtonFavorite(isChecked, companyInfo.ticker)}

        }
        private fun createChangeString(priceChange: Double, priceChangePercent: Double):String{
            val sign: String = if(priceChange<0.0) "-" else "+"
            val priceChangeWithoutSign: Double = if(priceChange<0.0) priceChange*(-1.0) else priceChange
            val priceChangePercentWithoutSign: Double = if(priceChange<0.0) priceChangePercent*(-1.0) else priceChangePercent
            return "$sign$$priceChangeWithoutSign ($priceChangePercentWithoutSign%)"
        }
    }



    object DiffCallback : DiffUtil.ItemCallback<CompanyInfoDst>(){
        override fun areItemsTheSame(oldItem: CompanyInfoDst, newItem: CompanyInfoDst): Boolean {
            if(oldItem.name == newItem.name && oldItem.ticker == newItem.ticker){
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: CompanyInfoDst, newItem: CompanyInfoDst): Boolean {
            if(oldItem.curPrice==newItem.curPrice && oldItem.priceChange==newItem.priceChange &&
                oldItem.priceChangePercent==newItem.priceChangePercent ){
                return true
            }
            return false
        }

    }

}