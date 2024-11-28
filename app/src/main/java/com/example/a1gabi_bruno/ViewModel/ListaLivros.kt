package com.example.a1gabi_bruno.ViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a1gabi_bruno.dao.LivroDao
import com.example.a1gabi_bruno.model.Livro
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLivros(
    livroDao: LivroDao,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var livros by remember { mutableStateOf(emptyList<Livro>()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Carregar os livros na inicialização
    LaunchedEffect(Unit) {
        livros = livroDao.getAll()
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
            items(livros) { livro ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Título: ${livro.titulo}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Autor: ${livro.autor}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Gênero: ${livro.genero}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Status de Leitura: ${livro.statusLeitura}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Anotações: ${livro.anotacoes}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        livroDao.delete(livro)
                                        livros = livroDao.getAll() // Atualiza a lista após deletar
                                    }
                                }
                            ) {
                                Text("Deletar")
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

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
                    Button(onClick = { navController.navigate("AdicionarLivro") }) {
                        Text("Novo Livro")
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = {
                        val shareText = livros.joinToString("\n") {
                            "Título: ${it.titulo}\nAutor: ${it.autor}\nGênero: ${it.genero}\nStatus: ${it.statusLeitura}\nAnotações: ${it.anotacoes}\n\n"
                        }
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Text("Compartilhar")
                    }
                }
            }
        }
    }
}
