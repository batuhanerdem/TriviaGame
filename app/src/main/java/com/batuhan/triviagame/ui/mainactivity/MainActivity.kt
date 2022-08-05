package com.batuhan.triviagame.ui.mainactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.batuhan.triviagame.R
import com.batuhan.triviagame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    var backPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        backPressed = false
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val fragmentManager = supportFragmentManager
        val navHostFragment =
            fragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment

        navController = navHostFragment.navController
        setupWithNavController(binding.bottomNavigationView, navController)

        binding.apply {
            bottomNavigationView.menu.getItem(1).isEnabled = false
            bottomNavigationView.background = null

            fab.setOnClickListener() {
                Intent(applicationContext, PlayActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (backPressed) {
            super.onBackPressed()
        } else {
            backPressed = true
            Toast.makeText(this, "Cikmak icin back tusuna basin", Toast.LENGTH_SHORT).show()
        }
    }
}
