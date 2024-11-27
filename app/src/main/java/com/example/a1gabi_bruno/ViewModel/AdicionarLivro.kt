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
    var livroAutor by remember { mutableStateOf(TextFieldValue("")) }
    var livroGenero by remember { mutableStateOf(TextFieldValue("")) }
    var livroStatusLeitura by remember { mutableStateOf(TextFieldValue("")) }
    var livroAnotacoes by remember { mutableStateOf(TextFieldValue("")) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        livros = livroDao.getAll()
    }

    fun addLivro(
        nome: String,
        autor: String,
        genero: String,
        statusLeitura: String,
        anotacoes: String
    ) {
        if (nome.isNotBlank() && autor.isNotBlank() && genero.isNotBlank()) {
            coroutineScope.launch {
                val novoLivro = Livro(
                    titulo = nome,
                    autor = autor,
                    genero = genero,
                    statusLeitura = statusLeitura,
                    anotacoes = anotacoes
                )
                livroDao.insert(novoLivro)
                // Limpar os campos após adicionar o livro
                livroNome = TextFieldValue("")
                livroAutor = TextFieldValue("")
                livroGenero = TextFieldValue("")
                livroStatusLeitura = TextFieldValue("")
                livroAnotacoes = TextFieldValue("")
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

            // Campo de texto para autor
            item {
                OutlinedTextField(
                    value = livroAutor,
                    onValueChange = { livroAutor = it },
                    label = { Text("Autor") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Campo de texto para gênero
            item {
                OutlinedTextField(
                    value = livroGenero,
                    onValueChange = { livroGenero = it },
                    label = { Text("Gênero") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Campo de texto para status de leitura
            item {
                OutlinedTextField(
                    value = livroStatusLeitura,
                    onValueChange = { livroStatusLeitura = it },
                    label = { Text("Status de Leitura") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Campo de texto para anotações
            item {
                OutlinedTextField(
                    value = livroAnotacoes,
                    onValueChange = { livroAnotacoes = it },
                    label = { Text("Anotações") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            // Botão para adicionar livro
            item {
                Button(
                    onClick = {
                        addLivro(
                            livroNome.text,
                            livroAutor.text,
                            livroGenero.text,
                            livroStatusLeitura.text,
                            livroAnotacoes.text
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Adicionar Livro")
                }
            }

            // Botões de navegação
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { navController.navigate("CadernoNotas") }) {
                        Text("Caderno de Anotações")
                    }
                    Button(onClick = { navController.navigate("ListarLivros") }) {
                        Text("Listar Livros")
                    }
                }
            }
        }
    }
}
