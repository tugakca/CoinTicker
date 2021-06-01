package com.android.cointicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private var selectedItem=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        selectedItem=R.id.homepageFragment

        navController= Navigation.findNavController(this,R.id.fragment)

        bottomNavigation.setOnNavigationItemSelectedListener {

            when(it.itemId){

                R.id.homepageFragment->{
                    navController.navigate(R.id.homepageFragment)
                    selectedItem=R.id.homepageFragment
                }
                R.id.favoritesFragment->{
                    navController.navigate(R.id.favoritesFragment)
                    selectedItem=R.id.favoritesFragment

                }
                R.id.searchFragment->{
                    navController.navigate(R.id.searchFragment)
                    selectedItem=R.id.searchFragment
                }

                R.id.profileFragment->{
                    navController.navigate(R.id.profileFragment)
                    selectedItem=R.id.profileFragment
                }

            }
            return@setOnNavigationItemSelectedListener true
        }

    }

    override fun onResume() {
        super.onResume()
        if(selectedItem==R.id.profileFragment)
        bottomNavigation.selectedItemId=selectedItem
    }

}