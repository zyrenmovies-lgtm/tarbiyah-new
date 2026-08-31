package com.tarbiyah.ailearn

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tarbiyah.ailearn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigation()
        setupFab()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        // Zoom animation on navbar item tap
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            animateNavIcon(binding.bottomNavigation, item.itemId)
            navController.navigate(item.itemId)
            true
        }

        // Show/hide FAB and BottomNav based on current destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.fabCheckIn.visibility = View.VISIBLE
                    binding.bottomNavCard.visibility = View.VISIBLE
                }
                R.id.aiTutorFragment,
                R.id.feedFragment,
                R.id.chatFragment,
                R.id.profileFragment -> {
                    binding.fabCheckIn.visibility = View.GONE
                    binding.bottomNavCard.visibility = View.VISIBLE
                }
                else -> {
                    binding.fabCheckIn.visibility = View.GONE
                    binding.bottomNavCard.visibility = View.GONE
                }
            }
        }
    }

    private fun animateNavIcon(navView: BottomNavigationView, itemId: Int) {
        val itemView = navView.findViewById<View>(itemId) ?: return

        val scaleX = ObjectAnimator.ofFloat(itemView, "scaleX", 1f, 1.35f, 1f)
        val scaleY = ObjectAnimator.ofFloat(itemView, "scaleY", 1f, 1.35f, 1f)

        scaleX.duration = 300
        scaleY.duration = 300

        val interpolator = OvershootInterpolator(2.5f)
        scaleX.interpolator = interpolator
        scaleY.interpolator = interpolator

        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    private fun setupFab() {
        binding.fabCheckIn.setOnClickListener {
            android.widget.Toast.makeText(
                this,
                "Check-In berhasil! Jazakallah khairan.",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}
