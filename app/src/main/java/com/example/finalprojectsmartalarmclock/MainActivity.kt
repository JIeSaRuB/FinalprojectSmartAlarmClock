package com.example.finalprojectsmartalarmclock

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.finalprojectsmartalarmclock.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView


class MainActivity() : AppCompatActivity(){

    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainer.id, MainFragment())
            .commit()
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_main -> {
                    openFragment(MainFragment())
                    true
                }
                R.id.nav_alarm -> {
                    openFragment(AlarmFragment())
                    true
                }
                R.id.nav_notify -> {
                    openFragment(NotifyFragment())
                    true
                }
                R.id.nav_info -> {
                    openFragment(InfoFragment())
                    true
                }
                R.id.nav_settings -> {
                    openFragment(SettingsFragment())
                    true
                }
                // Добавьте обработку других пунктов меню при необходимости
                else -> false
            }
        }
    }
    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
        drawerLayout.closeDrawer(GravityCompat.START)
    }


}
