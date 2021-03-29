package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.RetrofitFinHub
import com.example.myapplication.api.RetrofitIEXCloud
import com.example.myapplication.common.CompanyInfoDst
import com.example.myapplication.common.SearchInfo
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.db.DataBase
import com.example.myapplication.db.FavoriteCompany
import com.example.myapplication.db.SearchHistory
import com.example.myapplication.repository.CompanyRepoImpl
import com.example.myapplication.repository.LocalRepo
import com.example.myapplication.repository.LocalRepoImpl
import com.example.myapplication.ui.FavoriteFragment
import com.example.myapplication.ui.SearchFragment
import com.example.myapplication.ui.StocksFragment
import com.example.myapplication.util.CompanyMapper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), IListener {


    private lateinit var binding: ActivityMainBinding
    private lateinit var dataBase: DataBase
    private lateinit var localRepo: LocalRepo
    private lateinit var textWatcher: TextWatcher
    var companyRepo = CompanyRepoImpl(RetrofitIEXCloud.iexCloudApi, RetrofitFinHub.finHubApi)
    private var subject = PublishSubject.create<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //выбор
                Log.d("TabLayout", "sel " + tab?.position)
                chooseFragment(tab?.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //повторное нажатие на выбранный уже
                Log.d("TabLayout", "sel " + tab?.position)
                chooseFragment(tab?.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //показывыает какой был выбран до переключения
            }
        })


        dataBase = DataBase.getDataBase(applicationContext)!!
        val localDao = dataBase.localDao()
        localRepo = LocalRepoImpl(localDao)


        binding.textInputEditText.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.container, SearchFragment())
                    .addToBackStack(null)
                    .commit()
            binding.textInputEditText.text = null
            binding.tabLayout.visibility = GONE
            binding.outlinedTextField.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            binding.outlinedTextField.setEndIconDrawable(R.drawable.ic_baseline_clear_24)
            binding.outlinedTextField.setStartIconDrawable(R.drawable.ic_baseline_keyboard_backspace_24)
            textWatcher = binding.textInputEditText.addTextChangedListener(
                    { q, w, e, r ->
                        Log.d("TextChangedListener", " before $q $w $e $r")
                    },
                    { q, w, e, r ->
                        Log.d("TextChangedListener", "on $q $w $e $r")
                        subject.onNext(q.toString())
                    },
                    { q ->
                        Log.d("TextChangedListener", "after $q ")
                    }
            )
            binding.outlinedTextField.setStartIconOnClickListener {
                binding.textInputEditText.removeTextChangedListener(textWatcher)
                supportFragmentManager.popBackStack()
                binding.outlinedTextField.endIconDrawable = null
                binding.outlinedTextField.setStartIconDrawable(R.drawable.ic_baseline_search_24)
                binding.tabLayout.visibility = VISIBLE
                binding.outlinedTextField.endIconMode = TextInputLayout.END_ICON_NONE
                binding.textInputEditText.setText(R.string.find_company_or_ticker)
            }
        }


        if (savedInstanceState == null) {
            fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .add(R.id.container, StocksFragment())
                    .commit()
        }
    }

    fun chooseFragment(tabPos: Int?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when (tabPos) {
            0 -> fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.container, StocksFragment())
                    .commit()

            1 -> fragmentTransaction
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.container, FavoriteFragment())
                    .commit()
        }
    }

    override fun pressButtonFavorite(bool: Boolean, ticker: String) {
        Log.d("MainActivity", "$bool, $ticker")
        if (bool) {
            localRepo.insertTicker(FavoriteCompany(ticker))
        } else {
            localRepo.deleteTicker(FavoriteCompany(ticker))
        }
    }

    @SuppressLint("CheckResult")
    override fun getSearch(): MutableLiveData<List<CompanyInfoDst>> {
        val resultOfSearch: MutableLiveData<List<CompanyInfoDst>> = MutableLiveData()
        val compMapper = CompanyMapper()
        subject
                .debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext { Log.d("Subject", it) }
                .subscribeOn(Schedulers.io())
                .distinctUntilChanged()
                .switchMap { query ->

                    companyRepo.doSearch(query)
                            .onErrorReturn { throwable -> SearchInfo(0, listOf()) }
                            .doOnNext { if(query!="") localRepo.insertSearch(SearchHistory(query)) }
                }
                .flatMapSingle {
                    Flowable.fromIterable(it.result)
                            .filter { s -> !s.symbol.contains(".") }
                            .flatMap { companyRepo.getCompanyInfo(it.symbol) }
                            .map { z -> compMapper.map(z) }
                            .toList()
                            .onErrorReturn { throwable -> listOf() }
                }
                .withLatestFrom(localRepo.getAllFavoriteCompany().toObservable()){ comps, favLst->
                    for(comp in comps){
                        if(favLst.contains(FavoriteCompany(comp.ticker))){
                            comp.isFavorite=true
                        }
                    }
                    return@withLatestFrom comps
                }
                .subscribe({ resultOfSearch.postValue(it) },
                        { error -> Log.d("MainActivity", "Error in downloading search list: " + error.message) })
        return resultOfSearch
    }

    override fun find(name: String) {
        binding.textInputEditText.setText(name)
    }
}
