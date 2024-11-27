import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a1gabi_bruno.dao.CadernoDao
import com.example.a1gabi_bruno.model.Caderno
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CadernoNotas(
    cadernoDao: CadernoDao,
    navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()
    var texto by remember { mutableStateOf("") }

    // Carrega a anotação existente (se houver)
    LaunchedEffect(Unit) {
        val cadernoExistente = cadernoDao.getCadernoById(1)
        if (cadernoExistente != null) {
            texto = cadernoExistente.texto
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Caderno de Notas") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Suas Anotações",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                BasicTextField(
                    value = texto,
                    onValueChange = { texto = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black
                    ),
                    decorationBox = { innerTextField ->
                        if (texto.isEmpty()) {
                            Text(
                                text = "Escreva aqui suas anotações...",
                                style = TextStyle(color = Color.Gray, fontSize = 18.sp)
                            )
                        }
                        innerTextField()
                    }
                )
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val caderno = Caderno(id = 1, texto = texto)
                        cadernoDao.insertOrUpdate(caderno)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Salvar Anotações")
            }

            Button(
                onClick = { navController.navigate("AdicionarLivro") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Voltar para Livros")
            }
        }
    }
}
