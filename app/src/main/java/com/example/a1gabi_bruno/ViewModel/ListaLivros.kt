
package com.example.a1gabi_bruno.ViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a1gabi_bruno.dao.LivroDao
import com.example.a1gabi_bruno.model.Livro
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivros(
    livroDao: LivroDao,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var livros by remember { mutableStateOf(emptyList<Livro>()) }
    var livroNome by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        livros = livroDao.getAll()
    }

    fun addLivro(nome: String) {
        if (nome.isNotBlank()) {
            coroutineScope.launch {
                val novoLivro = Livro(titulo = nome)
                livroDao.insert(novoLivro)
                livroNome = TextFieldValue("")
                livros = livroDao.getAll()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Lista de Livros") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
//
            // Lista de livros
            items(livros) { livro ->
                Text(
                    text = livro.titulo,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Espaçamento entre a lista de livros e os botões de navegação
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Botões de navegação para outras telas
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate("CadernoNotas") }) {
                        Text("Caderno de anotações")
                    }
                    Button(onClick = { navController.navigate("AdicionarLivro") }) {
                        Text("Novo Livro")
                    }
                }
            }
        }
    }
}
