package com.example.a1gabi_bruno.ViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CadernoNotas(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.Blue) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween // Distribui os elementos verticalmente
        ) {
            Text(
                text = "Caderno Notas",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // Para centralizar a distribuição

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate("ListaLivros") }) {
                    Text("Listar Livros")
                }
                Button(onClick = { navController.navigate("AdicionarLivro") }) {
                    Text("Novo Livro")
                }
            }
        }
    }
}
