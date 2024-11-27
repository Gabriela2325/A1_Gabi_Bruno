package com.example.a1gabi_bruno

import CadernoNotas
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.a1gabi_bruno.ViewModel.GerenciarLivro
import com.example.a1gabi_bruno.ViewModel.ListaLivros
import com.example.a1gabi_bruno.database.AppDatabase
import com.example.a1gabi_bruno.ui.theme.A1gabi_brunoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o banco de dados
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app-database"
        ).build()

        val livroDao = database.livroDao()

        setContent {
            A1gabi_brunoTheme {
                val navController = rememberNavController()


                NavHost(
                    navController = navController,
                    startDestination = "AdicionarLivro"
                ) {
                    composable("CadernoNotas") { CadernoNotas(cadernoDao = database.cadernoDao(),navController = navController)}
                    composable("ListarLivros") { ListaLivros(livroDao = livroDao,navController = navController) }
                    composable("AdicionarLivro") { GerenciarLivro(livroDao = livroDao, navController = navController) }

                }
            }
        }


    }
}

