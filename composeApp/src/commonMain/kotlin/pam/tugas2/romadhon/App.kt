package pam.tugas2.romadhon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class News(
    val id: Int,
    val title: String,
    val category: String
)

class NewsFeedSimulator {
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    fun markAsRead() {
        _readCount.value++
    }

    fun getNewsStream(): Flow<News> = flow {
        val categories = listOf("Teknologi", "Olahraga", "Politik", "Hiburan")
        var newsId = 1

        while (true) {
            delay(2000L)
            try {
                if (newsId % 5 == 0) throw Exception("Koneksi server terputus!")

                val randomCategory = categories.random()
                emit(News(newsId, "Berita Terkini #$newsId", randomCategory))
            } catch (e: Exception) {
                emit(News(newsId, "ERROR: ${e.message}", "Sistem"))
            }
            newsId++
        }
    }

    suspend fun fetchNewsDetail(id: Int): String {
        delay(1000L)
        if (id % 3 == 0) throw Exception("Gagal memuat dari database!")
        return "Detail lengkap dari Berita #$id berhasil diambil secara Async!"
    }
}

@Composable
fun App() {
    MaterialTheme {
        val simulator = remember { NewsFeedSimulator() }
        val totalRead by simulator.readCount.collectAsState()
        val coroutineScope = rememberCoroutineScope()
        var currentNews by remember { mutableStateOf("Menunggu berita pertama...") }
        var detailText by remember { mutableStateOf("Belum ada berita yang dibaca.") }

        LaunchedEffect(Unit) {
            simulator.getNewsStream()
                .filter { it.category == "Teknologi" || it.category == "Olahraga" || it.category == "Sistem" }
                .map { news -> "[Kategori: ${news.category}] ${news.title}" }
                .collect { formattedNews ->
                    currentNews = formattedNews
                }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Berita Live (Filter: Tech & Olahraga):")
            Text(currentNews)

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                simulator.markAsRead()
                coroutineScope.launch {
                    detailText = "Loading detail berita..."


                    val detailDeferred = async {
                        try {
                            simulator.fetchNewsDetail(totalRead)
                        } catch (e: Exception) {
                            "Waduh, terjadi masalah: ${e.message}"
                        }
                    }
                    detailText = detailDeferred.await()
                }
            }) {
                Text("Baca Berita Ini")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Total Berita Dibaca: $totalRead")
            Text("Isi Berita: $detailText")
        }
    }
}