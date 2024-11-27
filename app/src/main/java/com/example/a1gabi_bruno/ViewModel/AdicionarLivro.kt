package com.example.a1gabi_bruno.ViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a1gabi_bruno.model.Livro
import com.example.a1gabi_bruno.dao.LivroDao
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerenciarLivro(
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
                title = { Text("Gerenciador de Livros") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Campo de texto para adicionar o nome do livro
            item {
                OutlinedTextField(
                    value = livroNome,
                    onValueChange = { livroNome = it },
                    label = { Text("Nome do Livro") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Botão para adicionar livro
            item {
                Button(
                    onClick = { addLivro(livroNome.text) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Adicionar Livro")
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate("CadernoNotas") }) {
                        Text("Tela Azul")
                    }
                    Button(onClick = { navController.navigate("ListaLivros") }) {
                        Text("Listar Livros")
                    }
                }
            }
        }
    }
}
