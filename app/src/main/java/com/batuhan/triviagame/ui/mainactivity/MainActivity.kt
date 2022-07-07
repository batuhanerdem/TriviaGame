package com.batuhan.triviagame.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val containerId = R.id.fragmentContainerView2
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()
        val fragmentManager = supportFragmentManager
        val navHostFragment = fragmentManager.findFragmentById(containerId) as NavHostFragment
        val username = intent.getStringExtra("username")

        username?.let {
            val bundle = bundleOf("name" to it)
            profileFragment.arguments = bundle
        }

        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView, navController)

        binding.apply {
            bottomNavigationView.menu.getItem(1).isEnabled = false
            bottomNavigationView.background = null


            fab.setOnClickListener() {
                val fragmentTransaction = fragmentManager.beginTransaction()
                val playFragment = PlayFragment()
                fragmentTransaction.replace(containerId, playFragment).commit()
            }
            /*bottomNavigationView.setOnItemSelectedListener {
                val fragmentTransaction = fragmentManager.beginTransaction()
                when (it.itemId) {
                    R.id.profileItem -> fragmentTransaction.replace(containerId, profileFragment)
                        .commit()
                    R.id.settingsItem -> fragmentTransaction.replace(containerId, settingsFragment)
                        .commit()
                }
                return@setOnItemSelectedListener true
            }*/
        }
    }
}