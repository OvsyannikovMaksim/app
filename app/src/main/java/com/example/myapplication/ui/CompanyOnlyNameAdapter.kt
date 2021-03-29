package com.example.myapplication.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.IListener
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.databinding.NameItemBinding

class CompanyOnlyNameAdapter(private var mListener: IListener) :
        ListAdapter<CompanyInfoDst, CompanyOnlyNameAdapter.CompanyVH>(DiffCallback){

    private lateinit var binding: NameItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyVH {

        val inflater : LayoutInflater = LayoutInflater.from(parent.context)
        binding = NameItemBinding.inflate(inflater,parent, false)
        return CompanyVH(binding, mListener)

    }

    override fun onBindViewHolder(holder: CompanyVH, position: Int) {
        holder.bind(currentList[position])
    }




    class CompanyVH (private var itemBinding: NameItemBinding, private var mListener: IListener):
            RecyclerView.ViewHolder(itemBinding.root){
        private lateinit var text:String
        init{
            itemBinding.companyNameNameItem.setOnClickListener { mListener.find(text) }
        }
        fun bind(companyInfo: CompanyInfoDst){
            text=companyInfo.ticker
            itemBinding.companyNameNameItem.text=companyInfo.name
        }

    }



    object DiffCallback : DiffUtil.ItemCallback<CompanyInfoDst>(){
        override fun areItemsTheSame(oldItem: CompanyInfoDst, newItem: CompanyInfoDst): Boolean {
            if(oldItem.name == newItem.name){
                return true
            }
            return false
        }

        override fun areContentsTheSame(oldItem: CompanyInfoDst, newItem: CompanyInfoDst): Boolean {
            if(oldItem.name == newItem.name){
                return true
            }
            return false
        }

    }

}