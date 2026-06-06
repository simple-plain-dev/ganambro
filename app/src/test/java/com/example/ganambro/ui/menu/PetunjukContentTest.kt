package com.example.ganambro.ui.menu

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PetunjukContentTest {

    @Test
    fun `PetunjukContent holds title steps and footer`() {
        val content = PetunjukContent(
            title = "Panduan Penggunaan",
            steps = listOf(
                "1. Buka aplikasi Ganambro",
                "2. Masukkan Token dari Pengawas",
                "3. Kerjakan ujian",
            ),
            footer = "Jika ada masalah, hubungi Pengawas.",
        )

        assertEquals("Panduan Penggunaan", content.title)
        assertEquals(3, content.steps.size)
        assertEquals("1. Buka aplikasi Ganambro", content.steps[0])
        assertEquals("Jika ada masalah, hubungi Pengawas.", content.footer)
    }

    @Test
    fun `PetunjukContent can be instantiated with empty steps`() {
        val content = PetunjukContent("Kosong", emptyList(), "Selesai")
        assertTrue(content.steps.isEmpty())
    }
}
