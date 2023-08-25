package tech.ritzvincentculanag.intelliquest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import tech.ritzvincentculanag.intelliquest.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = binding.navigationDashboard
        val navHost = supportFragmentManager.findFragmentById(binding.navHostFragment.id)
        val navController = navHost?.findNavController()

        if (navController != null) {
            NavigationUI.setupWithNavController(navigation, navController)
        }
    }
}