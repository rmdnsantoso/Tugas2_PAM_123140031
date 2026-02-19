package pam.tugas2.romadhon

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class NewsFeedSimulatorTest {

    @Test
    fun testFlowMengeluarkanBerita() = runTest {
        val simulator = NewsFeedSimulator()
        val beritaPertama = simulator.getNewsStream().first()
        assertTrue(beritaPertama.id > 0, "Berita harus memiliki ID lebih dari 0")
    }

    @Test
    fun testSuspendFunctionBerhasil() = runTest {
        val simulator = NewsFeedSimulator()
        val hasil = simulator.fetchNewsDetail(1)
        assertTrue(hasil.contains("berhasil"), "Fungsi async gagal mengembalikan teks sukses")
    }
}