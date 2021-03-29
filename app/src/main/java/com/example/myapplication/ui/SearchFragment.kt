package com.example.myapplication.ui

import  android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.IListener
import com.example.myapplication.api.RetrofitIEXCloud
import com.example.myapplication.api.RetrofitFinHub
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.db.DataBase
import com.example.myapplication.repository.CompanyRepoImpl
import com.example.myapplication.repository.LocalRepoImpl
import com.example.myapplication.viewmodel.SearchViewModel
import com.example.myapplication.viewmodel.SearchViewModelFactory

class SearchFragment: Fragment() {

    private var binding: FragmentSearchBinding?=null
    private var mListener: IListener? = null
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchViewModelFactory: SearchViewModelFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(requireActivity() is IListener){
            mListener = requireActivity() as IListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentSearchBinding.inflate(inflater)
        val companyRepo = CompanyRepoImpl(RetrofitIEXCloud.iexCloudApi, RetrofitFinHub.finHubApi)
        val dataBase = DataBase.getDataBase(this.context!!)!!
        val localDao=dataBase.localDao()
        val localRepo = LocalRepoImpl(localDao)
        searchViewModelFactory= SearchViewModelFactory(companyRepo, localRepo)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel =  ViewModelProvider(this, searchViewModelFactory).get(SearchViewModel::class.java)
        val mLayoutOnlyNameAdapterGainers= StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL)
        val mLayoutOnlyNameAdapterLastSearch= StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL)
        val mLayoutFullInfoAdapter= GridLayoutManager(activity, 1,
                LinearLayoutManager.VERTICAL, false)
        val gainers : LiveData<List<CompanyInfoDst>> = searchViewModel.gainersList
        val lastSearch : LiveData<List<CompanyInfoDst>> = searchViewModel.searchHistoryList
        val resultOfSearch : LiveData<List<CompanyInfoDst>> = mListener!!.getSearch()
        val onlyNameAdapterGainers=CompanyOnlyNameAdapter(ClickChecker())
        val onlyNameAdapterLastSearch=CompanyOnlyNameAdapter(ClickChecker())
        val fullInfoAdapter=CompanyFullInfoAdapter(ClickChecker())
        gainers.observe(viewLifecycleOwner, {
            Log.d("SearchFragment", "Result of downloading gainers $it")
            onlyNameAdapterGainers.submitList(it)
            binding?.gainersRecycler?.adapter = onlyNameAdapterGainers
            binding?.gainersRecycler?.layoutManager=mLayoutOnlyNameAdapterGainers
        })
        lastSearch.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                onlyNameAdapterLastSearch.submitList(it)
                binding?.lastRecycler?.visibility=VISIBLE
                binding?.lastStock?.visibility=VISIBLE
                binding?.lastRecycler?.adapter = onlyNameAdapterLastSearch
                binding?.lastRecycler?.layoutManager=mLayoutOnlyNameAdapterLastSearch
            }
        })
        resultOfSearch.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                fullInfoAdapter.submitList(it)
                binding?.lastStock?.visibility=GONE
                binding?.gainersStock?.visibility=GONE
                binding?.lastRecycler?.visibility=GONE
                binding?.gainersRecycler?.visibility=GONE
                binding?.searchRecycler?.visibility= VISIBLE
                binding?.searchRecycler?.adapter=fullInfoAdapter
                binding?.searchRecycler?.layoutManager=mLayoutFullInfoAdapter
            }
            else{
                binding?.gainersStock?.visibility= VISIBLE
                binding?.gainersRecycler?.visibility= VISIBLE
                if(binding?.lastRecycler?.adapter?.itemCount !=0){
                    binding?.lastStock?.visibility= VISIBLE
                    binding?.lastRecycler?.visibility= VISIBLE
                }
                binding?.searchRecycler?.visibility= GONE
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
        searchViewModel.clear()
    }

    inner class ClickChecker : IListener{
        override fun pressButtonFavorite(bool: Boolean, ticker: String) {
            mListener?.pressButtonFavorite(bool, ticker)
        }

        override fun getSearch(): MutableLiveData<List<CompanyInfoDst>> {
            TODO("Not yet implemented")
        }

        override fun find(name: String) {
            mListener?.find(name)
        }
    }
}