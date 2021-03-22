package com.example.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.IListener
import com.example.myapplication.api.Retrofit
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.databinding.FragmentFavoriteBinding
import com.example.myapplication.db.DataBase
import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.db.FavoriteCompanyDao
import com.example.myapplication.repository.CompanyRepoImpl
import com.example.myapplication.repository.LocalRepoImpl
import com.example.myapplication.viewmodel.FavoriteViewModel
import com.example.myapplication.viewmodel.FavoriteViewModelFactory


class FavoriteFragment: Fragment() {

    private var binding: FragmentFavoriteBinding?=null
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var favoriteViewModelFactory: FavoriteViewModelFactory
    private lateinit var dataBase:DataBase
    lateinit var favCompDao: FavoriteCompanyDao


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentFavoriteBinding.inflate(inflater)
        dataBase = DataBase.getDataBase(this.context!!)!!
        favCompDao=dataBase.favoriteCompanyDao()
        val companyRepo = CompanyRepoImpl(Retrofit.finHubApi)
        val localRepo = LocalRepoImpl(DataBase.getDataBase(this.context!!)!!.favoriteCompanyDao())
        favoriteViewModelFactory= FavoriteViewModelFactory(companyRepo, localRepo)
        return binding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayout= GridLayoutManager(
            activity, 1,
            LinearLayoutManager.VERTICAL, false)
        favoriteViewModel =  ViewModelProvider(this, favoriteViewModelFactory).get(FavoriteViewModel::class.java)
        val companies : LiveData<List<CompanyInfoDst>> = favoriteViewModel.companyList
        val companyAdapter = CompanyAdapter(ClickChecker())
        companies.observe(viewLifecycleOwner, { res ->
            Log.d("TAG000", ""+res)
            companyAdapter.submitList(res)
            binding?.favoriteRecyclerView?.adapter = companyAdapter
        })
        binding?.favoriteRecyclerView?.layoutManager=mLayout
    }




    override fun onDestroyView() {
        super.onDestroyView()
        binding=null
        favoriteViewModel.clear()
    }

    inner class ClickChecker : IListener{
        override fun pressButtonFavorite(bool: Boolean, ticker: String) {
                if(bool){
                    favCompDao.insertTicker(FavoriteCompany(ticker))
                }
                else{
                    favCompDao.deleteTicker(FavoriteCompany(ticker))
                }
        }

    }
}