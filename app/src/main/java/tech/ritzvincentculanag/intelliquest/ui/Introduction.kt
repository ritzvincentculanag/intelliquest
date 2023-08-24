package tech.ritzvincentculanag.intelliquest.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.ritzvincentculanag.intelliquest.LoginActivity
import tech.ritzvincentculanag.intelliquest.databinding.ActivityIntroductionBinding
import tech.ritzvincentculanag.intelliquest.model.adapter.IntroductionAdapter
import tech.ritzvincentculanag.intelliquest.util.SessionManager

class Introduction : AppCompatActivity() {

    private lateinit var binding: ActivityIntroductionBinding
    private lateinit var adapter: IntroductionAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        adapter = IntroductionAdapter()
        sessionManager = SessionManager(applicationContext)

        if (sessionManager.userIsActive()) {
            val goToLogin = Intent(this, LoginActivity::class.java)
            startActivity(goToLogin)
            onDestroy()
        }

        binding.viewPager.adapter = adapter
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPagerIndicator.setViewPager(binding.viewPager)

        setContentView(binding.root)
    }
}