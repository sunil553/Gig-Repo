package com.llyod.task.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.llyod.task.R
import com.llyod.task.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GigWorkerActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navGraph: NavGraph

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavGraph()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                navController.popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        return super.onPrepareOptionsMenu(menu)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tool_bar, menu)
        return true
    }


    private fun initNavGraph() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val imageView = findViewById<ImageView>(R.id.imageMenu)
        val title = findViewById<TextView>(R.id.title)
        imageView.setOnClickListener{
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Find our drawer view
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Setup drawer view
        setupDrawerContent(navigationView,drawerLayout,title);

//        navController = findNavController(R.id.fragmentContainer)
//        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
//        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        navigationView.itemIconTintList = null
//        val appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        setupNavigationMenu(navController)
    }

    private fun setupDrawerContent(
        navigationView: NavigationView?,
        drawerLayout: DrawerLayout,
        title: TextView
    ) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        navigationView?.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.dashboard -> {// Handle item 1 click
                    navController.navigate(R.id.dashboardFragment)
                    title.text = "Dashboard"
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.personalFragment -> {
                    // Handle item 2 click
                    navController.navigate(R.id.personalFragment)
                    title.text = "User Details"
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.faq -> {
                    // Handle item 3 click
                    true
                }
                R.id.logout -> {
                    sharedViewModel.clearAllData()
                    finish()
                    Intent(this, LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                    true
                }
               else -> false
            }}
    }


    private fun setupNavigationMenu(navController: NavController){
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)
    }
}