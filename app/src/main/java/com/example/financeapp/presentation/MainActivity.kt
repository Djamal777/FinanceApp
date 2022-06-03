package com.example.financeapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.financeapp.R
import com.example.financeapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_,destination,_->
            when(destination.id){
                R.id.addEditAccountFragment-> binding.bottomNavigationView.visibility= View.GONE
                R.id.addEditCategoryFragment-> binding.bottomNavigationView.visibility= View.GONE
                R.id.addEditOperationFragment-> binding.bottomNavigationView.visibility= View.GONE
                R.id.categoryOperationsFragment-> binding.bottomNavigationView.visibility= View.GONE
                R.id.financesFragment-> binding.bottomNavigationView.visibility= View.VISIBLE
                R.id.expensesFragment->binding.bottomNavigationView.visibility= View.VISIBLE
                R.id.incomeFragment->binding.bottomNavigationView.visibility= View.VISIBLE
            }
        }
    }
}