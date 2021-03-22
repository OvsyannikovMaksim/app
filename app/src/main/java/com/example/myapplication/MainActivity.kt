package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.FavoriteFragment
import com.example.myapplication.ui.StocksFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentTransaction = supportFragmentManager.beginTransaction()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //выбор
                Log.d("TAG", "sel "+tab?.position)
                chooseFragment(tab?.position)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                //повторное нажатие на выбранный уже
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                //показывыает какой был выбран до переключения
            }
        })

        //binding.outlinedTextField.setOnClickListener{supportFragmentManager.beginTransaction()}


        if (savedInstanceState == null) {
            fragmentTransaction
                .add(R.id.container, StocksFragment())
                .commit()
        }
    }

    fun chooseFragment(tabPos:Int?){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        when(tabPos){
            0->fragmentTransaction
                    .replace(R.id.container, StocksFragment())
                    .commit()

            1->fragmentTransaction
                    .replace(R.id.container, FavoriteFragment())
                    .commit()
        }
    }


}

