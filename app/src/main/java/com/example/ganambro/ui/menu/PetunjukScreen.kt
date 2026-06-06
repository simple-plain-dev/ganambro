package com.example.ganambro.ui.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Halaman panduan langkah-demi-langkah untuk Peserta.
 * Menerima [PetunjukContent] sebagai parameter — konten dipisahkan dari rendering.
 */
@Composable
fun PetunjukScreen(
    content: PetunjukContent,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        // Judul
        Text(
            text = content.title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00FF88),
            fontFamily = FontFamily.Monospace,
        )

        Spacer(Modifier.height(24.dp))

        // Langkah-langkah
        content.steps.forEach { step ->
            Text(
                text = step,
                fontSize = 15.sp,
                color = Color.White,
                fontFamily = FontFamily.Monospace,
                lineHeight = 22.sp,
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(24.dp))

        // Footer
        Text(
            text = content.footer,
            fontSize = 13.sp,
            color = Color.Gray,
            fontFamily = FontFamily.Monospace,
        )

        Spacer(Modifier.height(32.dp))

        // Tombol kembali ke Menu
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Kembali ke Menu")
        }
    }
}
