package com.aifortune.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aifortune.app.ui.screens.apipanel.ApiPanelScreen
import com.aifortune.app.ui.screens.bazi.BaziScreen
import com.aifortune.app.ui.screens.features.FeaturesScreen
import com.aifortune.app.ui.screens.home.HomeScreen
import com.aifortune.app.ui.screens.name.NameScreen
import com.aifortune.app.ui.screens.namegen.NameGenScreen
import com.aifortune.app.ui.screens.profile.ProfileScreen
import com.aifortune.app.ui.screens.shangye.ShangyeScreen
import com.aifortune.app.ui.screens.tarot.TarotScreen
import com.aifortune.app.ui.screens.xingzuo.XingzuoScreen
import com.aifortune.app.ui.screens.xueye.XueyeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Features : Screen("features")
    object ApiPanel : Screen("api_panel")
    object Profile : Screen("profile")
    object Bazi : Screen("bazi")
    object Xueye : Screen("xueye")
    object Shangye : Screen("shangye")
    object Xingzuo : Screen("xingzuo")
    object Name : Screen("name")
    object Tarot : Screen("tarot")
    object NameGen : Screen("name_gen")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(300)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToFeatures = { navController.navigate(Screen.Features.route) },
                onNavigateToApi = { navController.navigate(Screen.ApiPanel.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }
        
        composable(Screen.Features.route) {
            FeaturesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToBazi = { navController.navigate(Screen.Bazi.route) },
                onNavigateToXueye = { navController.navigate(Screen.Xueye.route) },
                onNavigateToShangye = { navController.navigate(Screen.Shangye.route) },
                onNavigateToXingzuo = { navController.navigate(Screen.Xingzuo.route) },
                onNavigateToName = { navController.navigate(Screen.Name.route) },
                onNavigateToTarot = { navController.navigate(Screen.Tarot.route) },
                onNavigateToNameGen = { navController.navigate(Screen.NameGen.route) }
            )
        }
        
        composable(Screen.ApiPanel.route) {
            ApiPanelScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Bazi.route) {
            BaziScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Xueye.route) {
            XueyeScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Shangye.route) {
            ShangyeScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Xingzuo.route) {
            XingzuoScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Name.route) {
            NameScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Tarot.route) {
            TarotScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.NameGen.route) {
            NameGenScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
