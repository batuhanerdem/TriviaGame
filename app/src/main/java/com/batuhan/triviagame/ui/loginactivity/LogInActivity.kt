package com.batuhan.triviagame.ui.loginactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.batuhan.triviagame.databinding.ActivityLogInBinding
import com.batuhan.triviagame.db.UserDatabase
import com.batuhan.triviagame.db.UserRepository

class LogInActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}