package com.kreggscode.wearosbmi

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.kreggscode.wearosbmi.ui.screens.AnalysisScreen
import com.kreggscode.wearosbmi.ui.screens.CalculatorScreen
import com.kreggscode.wearosbmi.ui.screens.ChatWithAIScreen
import com.kreggscode.wearosbmi.ui.screens.DietPlansScreen
import com.kreggscode.wearosbmi.ui.screens.MealPlansScreen
import com.kreggscode.wearosbmi.ui.screens.ResultsScreen
import com.kreggscode.wearosbmi.ui.screens.TrackerScreen
import com.kreggscode.wearosbmi.ui.theme.BMITheme
import com.kreggscode.wearosbmi.viewmodel.BMIViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            BMIApp()
        }
    }
    
    override fun onResume() {
        super.onResume()
        hideSystemUI()
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
    
    private fun hideSystemUI() {
        window.decorView.post {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.let { controller ->
                    controller.hide(android.view.WindowInsets.Type.statusBars())
                    controller.hide(android.view.WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
            }
            
            @Suppress("DEPRECATION")
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}

@Composable
fun BMIApp() {
    val viewModel: BMIViewModel = viewModel()
    val navController = rememberSwipeDismissableNavController()

    BMITheme(isDarkTheme = viewModel.isDarkTheme.value) {
        BMINavHost(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun BMINavHost(
    navController: NavHostController,
    viewModel: BMIViewModel
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "calculator"
    ) {
        composable("calculator") {
            CalculatorScreen(
                viewModel = viewModel,
                onCalculate = {
                    viewModel.calculateBMI()
                    navController.navigate("results")
                }
            )
        }

        composable("results") {
            ResultsScreen(
                viewModel = viewModel,
                onAIAnalysis = {
                    navController.navigate("analysis")
                },
                onTracker = {
                    navController.navigate("tracker")
                },
                onDietPlans = {
                    navController.navigate("dietPlans")
                },
                onMealPlans = {
                    navController.navigate("mealPlans")
                },
                onChatWithAI = {
                    navController.navigate("chatWithAI")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("analysis") {
            AnalysisScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("tracker") {
            TrackerScreen(
                records = viewModel.bmiRecords.value,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("dietPlans") {
            DietPlansScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("mealPlans") {
            MealPlansScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("chatWithAI") {
            ChatWithAIScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
