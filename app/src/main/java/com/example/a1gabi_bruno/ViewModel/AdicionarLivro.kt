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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import androidx.compose.ui.graphics.Color

/**** Interface que define as operações da API Open Library ****/
interface OpenLibraryApi {
    @GET("search.json")
    suspend fun searchBooks(@Query("title") title: String): BookSearchResponse
}

/**** Modelos para os dados retornados pela API Open Library ****/
data class BookSearchResponse(val docs: List<BookDoc>)

data class BookDoc(val title: String)

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
    var mensagemResultado by remember { mutableStateOf("") }
    var resultadoPositivo by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    /**** Configuração do cliente HTTP com timeout para a API ****/
    val timeout = 30L
    val client = OkHttpClient.Builder()
        .connectTimeout(timeout, TimeUnit.SECONDS)
        .readTimeout(timeout, TimeUnit.SECONDS)
        .writeTimeout(timeout, TimeUnit.SECONDS)
        .build()

    /**** Configuração do Retrofit para a API Open Library ****/
    val retrofit = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    /**** Instância da interface da API ****/
    val openLibraryApi = retrofit.create(OpenLibraryApi::class.java)

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
                println("API_DEBUG" + "URL sendo buscada: https://openlibrary.org/search.json?q=$nome")
                /**** Chamada à API para buscar livros com o título fornecido ****/
                try {
                    val response = openLibraryApi.searchBooks(nome)

                    val livrosEncontrados = response.docs.filter { it.title.contains(nome, ignoreCase = true) }
                    println(livrosEncontrados)
                    if (response.docs.isNotEmpty()) {
                        if (livrosEncontrados.isNotEmpty()) {
                            val num_livros = livrosEncontrados.size
                            mensagemResultado = "$num_livros livro(s) encontrados com o nome: \"$nome\" no repositório Open Library."
                            resultadoPositivo = true
                        } else {
                            mensagemResultado = "Nenhum livro encontrado com o nome: \"$nome\" no repositório Open Library."
                            resultadoPositivo = false
                        }
                    } else {
                        mensagemResultado = "Nenhum livro encontrado com o nome: \"$nome\" no repositório Open Library."
                        resultadoPositivo = false
                    }
                } catch (e: Exception) {
                    mensagemResultado = "Erro ao buscar livros: ${e.message}"
                    resultadoPositivo = false
                }

                val novoLivro = Livro(
                    titulo = nome,
                    autor = autor,
                    genero = genero,
                    statusLeitura = statusLeitura,
                    anotacoes = anotacoes
                )
                livroDao.insert(novoLivro)
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

            if (mensagemResultado.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (resultadoPositivo) Color(0xFFCCFF90) else Color(0xFFFFCDD2)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = mensagemResultado,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (resultadoPositivo) Color(0xFF388E3C) else Color(0xFFD32F2F)
                            )
                        }
                    }
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
