package com.example.foodapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class NavigationFragment: Fragment(R.layout.fragment_navigation) {

    companion object
    {
        fun newInstance() = NavigationFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bNav)
        val navController = (childFragmentManager.findFragmentById(R.id.container) as NavHostFragment)
                .navController
        bottomNavigationView.setupWithNavController(navController)
    }
}