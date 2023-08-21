package tech.ritzvincentculanag.intelliquest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnvMain = findViewById<BottomNavigationView>(R.id.bnvMain)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nvfMain)
        val navController = navHostFragment?.findNavController()

        if (navController != null) {
            bnvMain.setupWithNavController(navController)
        }
    }
}