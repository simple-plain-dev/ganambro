package com.example.ganambro.ui.menu

/**
 * Konten halaman Petunjuk — dipisahkan dari rendering.
 * Dibaca dari resource strings.xml, dapat diinject sebagai parameter ke Composable.
 */
data class PetunjukContent(
    val title: String,
    val steps: List<String>,
    val footer: String,
)
