package com.brainup.readbyapp.com.brainup.readbyapp.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brainup.readbyapp.R
import com.brainup.readbyapp.base.BaseActivity
import com.brainup.readbyapp.com.brainup.readbyapp.auth.login.LoginActivity
import com.brainup.readbyapp.utils.Constants
import com.brainup.readbyapp.utils.PrefrenceData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_login.*

class DashBoardActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var viewModel: DashBoardViewModel

    //private var userData: UserData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board_acitivty)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        initProgressbar(this)
        viewModel = ViewModelProvider(this).get(DashBoardViewModel::class.java)
        // getUserDetails()
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_profile,
                R.id.navigation_help
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //getUserDetails()
        // val headerView = navView.getHeaderView(0)
        // val tvHeader = headerView.findViewById(R.id.tvHeaderTitle) as TextView
        //  val logOut = headerView.findViewById<TextView>(R.id.logOut)


    }

    /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
         // Inflate the menu; this adds items to the action bar if it is present.
         menuInflater.inflate(R.menu.dash_board_acitivty, menu)
         return true
     }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun getUserDetails() {
        progressDialog.show()
        viewModel.getUserDetails(PrefrenceData.getUserId(this))
            ?.observe(this, Observer {
                progressDialog.dismiss()
                if (it.isSuccessful) {
                    val body = it.body
                    //  userData = body?.data
                    viewModel.userData?.value = body?.data
                }
            })
    }

}
