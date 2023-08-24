package tech.ritzvincentculanag.intelliquest.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tech.ritzvincentculanag.intelliquest.databinding.ActivityIntroductionBinding
import tech.ritzvincentculanag.intelliquest.model.adapter.IntroductionAdapter

class Introduction : AppCompatActivity() {

    private lateinit var binding: ActivityIntroductionBinding
    private lateinit var adapter: IntroductionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = IntroductionAdapter()
        binding.viewPager.adapter = adapter
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false
        binding.viewPagerIndicator.setViewPager(binding.viewPager)
    }
}