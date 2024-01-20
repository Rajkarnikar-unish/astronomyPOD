package com.example.astronomypod.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.navigateUp
import com.example.astronomypod.R
import com.example.astronomypod.database.AstronomyDatabase
import com.example.astronomypod.databinding.ActivityMainBinding
import com.example.astronomypod.models.AstronomyPOD
import com.example.astronomypod.repository.PODRepository

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var podViewModel: PODViewModel

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //App Drawer
        val navController = binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)

        NavigationUI.setupWithNavController(binding.drawerNavigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        //View Model
        val podRepository = PODRepository(AstronomyDatabase(this))

        val podViewModelFactory = PodViewModelProviderFactory(application, podRepository)
        podViewModel = ViewModelProvider(this, podViewModelFactory)[PODViewModel::class.java]
        

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}