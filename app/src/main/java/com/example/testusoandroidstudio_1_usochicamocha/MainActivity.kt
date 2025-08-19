// Archivo: /src/main/java/com/example/testusoandroidstudio_1_usochicamocha/MainActivity.kt

package com.example.testusoandroidstudio_1_usochicamocha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.testusoandroidstudio_1_usochicamocha.ui.form.FormScreen
import com.example.testusoandroidstudio_1_usochicamocha.ui.log.LogScreen
import com.example.testusoandroidstudio_1_usochicamocha.ui.login.LoginScreen
import com.example.testusoandroidstudio_1_usochicamocha.ui.login.LoginViewModel
import com.example.testusoandroidstudio_1_usochicamocha.ui.main.MainScreen
import com.example.testusoandroidstudio_1_usochicamocha.ui.shared.ConnectionStatusTopBar
import com.example.testusoandroidstudio_1_usochicamocha.ui.splash.SplashScreen
import com.example.testusoandroidstudio_1_usochicamocha.ui.splash.SplashViewModel
import com.example.testusoandroidstudio_1_usochicamocha.ui.theme.AppUsoChicamochaTheme
import com.example.testusoandroidstudio_1_usochicamocha.util.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppUsoChicamochaTheme {
                val networkStatus by networkMonitor.networkStatus.collectAsState()

                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        val splashViewModel: SplashViewModel = hiltViewModel()
                        SplashScreen(navController = navController, viewModel = splashViewModel)
                    }
                    composable("login") {
                        val loginViewModel: LoginViewModel = hiltViewModel()
                        Scaffold(
                            topBar = { ConnectionStatusTopBar(isConnected = networkStatus) }
                        ) { paddingValues ->
                            Box(modifier = Modifier.padding(paddingValues)) {
                                LoginScreen(
                                    viewModel = loginViewModel,
                                    onLoginSuccess = {
                                        navController.navigate("main") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }
                        }
                    }
                    composable("main") {
                        MainScreen(
                            networkStatus = networkStatus,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            },
                            onNavigateToLogs = {
                                navController.navigate("logs")
                            },
                            onNavigateToForm = {
                                navController.navigate("form")
                            }
                        )
                    }
                    composable("form") {
                        FormScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("logs") {
                        LogScreen(
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
