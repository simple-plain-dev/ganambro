package com.example.ganambro.ui.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ganambro.exit.ExitContext
import com.example.ganambro.exit.ExitCoordinator

/**
 * Halaman utama setelah Login.
 * 4 tombol: Ujian, Petunjuk, Exit, Logo.
 * Back button diblokir — tidak bisa mundur ke Login.
 */
@Composable
fun MenuScreen(
    exitCoordinator: ExitCoordinator,
    onNavigateToTokenInput: () -> Unit,
    onNavigateToPetunjuk: (PetunjukContent) -> Unit,
    onLogoTap: () -> Unit,
) {
    // Blokir back button dari Menu
    BackHandler(enabled = true) { /* no-op — tidak mundur ke Login */ }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Judul
        Text(
            text = "Ganambro",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00FF88),
            fontFamily = FontFamily.Monospace,
        )

        Spacer(Modifier.height(48.dp))

        // Tombol Ujian
        Button(
            onClick = onNavigateToTokenInput,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Ujian", fontSize = 18.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Tombol Petunjuk
        Button(
            onClick = {
                val content = PetunjukContent(
                    title = "Petunjuk Penggunaan Ganambro",
                    steps = listOf(
                        "1. Buka aplikasi Ganambro sebelum ujian dimulai.",
                        "2. Tunggu Precheck selesai — pastikan semua cek hijau.",
                        "3. Login dengan akun Google, atau pilih Skip.",
                        "4. Minta Token 6-digit dari Pengawas.",
                        "5. Masukkan Token di halaman input.",
                        "6. Kerjakan ujian — gunakan tombol toolbar untuk navigasi.",
                        "7. Jangan tinggalkan aplikasi atau colok headset selama ujian.",
                        "8. Setelah selesai, klik Exit dan konfirmasi.",
                    ),
                    footer = "Token berlaku 10 menit. Jika Token kadaluarsa, minta Token baru dari Pengawas.",
                )
                onNavigateToPetunjuk(content)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
        ) {
            Text("Petunjuk", fontSize = 18.sp)
        }

        Spacer(Modifier.height(16.dp))

        // Tombol Exit
        Button(
            onClick = { exitCoordinator.exit(ExitContext.Menu) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        ) {
            Text("Exit", fontSize = 18.sp)
        }

        Spacer(Modifier.height(48.dp))

        // Tombol Logo (trigger akses Pengawas)
        Button(
            onClick = onLogoTap,
            modifier = Modifier.size(64.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        ) {
            Text(
                text = "⚡",
                fontSize = 28.sp,
            )
        }
    }
}
