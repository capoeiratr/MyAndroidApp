package com.example.myandroidapp

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.service.AionLiveWallpaperService
import com.example.myandroidapp.ui.components.AionLiveWallpaperCanvas
import com.example.myandroidapp.ui.components.AionMMOIcon
import com.example.myandroidapp.ui.components.AionStatGauge
import com.example.myandroidapp.ui.theme.AbyssCardBg
import com.example.myandroidapp.ui.theme.AbyssDarkNavy
import com.example.myandroidapp.ui.theme.AetherCyan
import com.example.myandroidapp.ui.theme.AetherCyanBright
import com.example.myandroidapp.ui.theme.AsmodianCrimson
import com.example.myandroidapp.ui.theme.AsmodianViolet
import com.example.myandroidapp.ui.theme.CelestialGold
import com.example.myandroidapp.ui.theme.CelestialGoldBright
import com.example.myandroidapp.ui.theme.ElyosPearl
import com.example.myandroidapp.ui.theme.HpRedGreen
import com.example.myandroidapp.ui.theme.MpAetherBlue
import com.example.myandroidapp.ui.theme.MyAndroidAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAndroidAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AionThemeDashboard(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AionThemeDashboard(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var isElyos by remember { mutableStateOf(true) }

    // Read real battery level if possible
    val bm = context.getSystemService(android.content.Context.BATTERY_SERVICE) as? BatteryManager
    val batteryPct = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 84

    val currentTime = remember {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date())
    }
    val currentDate = remember {
        val sdf = SimpleDateFormat("EEEE, d MMMM", Locale("tr"))
        sdf.format(Date())
    }

    val primaryAether = if (isElyos) AetherCyan else AsmodianCrimson
    val secondaryGold = if (isElyos) CelestialGold else AsmodianViolet

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Interactive Live Wallpaper Background Engine
        AionLiveWallpaperCanvas(
            modifier = Modifier.fillMaxSize(),
            isElyos = isElyos
        )

        // 2. Foreground Glassmorphism MMORPG Dashboard UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Faction Header & Switcher
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(AbyssCardBg)
                    .border(1.dp, primaryAether.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Elyos Option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isElyos) primaryAether.copy(alpha = 0.25f) else Color.Transparent)
                        .clickable { isElyos = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚔️ ELYOS (IŞIK)",
                        color = if (isElyos) CelestialGoldBright else ElyosPearl.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Asmodian Option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (!isElyos) AsmodianCrimson.copy(alpha = 0.25f) else Color.Transparent)
                        .clickable { isElyos = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🌑 ASMODIAN (GÖLGE)",
                        color = if (!isElyos) AsmodianCrimson else ElyosPearl.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Runic Celestial Clock Widget
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(AbyssCardBg, AbyssDarkNavy.copy(alpha = 0.8f))
                        )
                    )
                    .border(1.dp, secondaryGold.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "AION II CHRONOS",
                    color = primaryAether,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
                Text(
                    text = currentTime,
                    color = ElyosPearl,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp
                )
                Text(
                    text = currentDate.uppercase(),
                    color = secondaryGold.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // HP / MP MMORPG Stat Bars
                AionStatGauge(
                    label = "AETHER HP (BATARYA)",
                    value = "%$batteryPct",
                    progress = batteryPct / 100f,
                    barColor = HpRedGreen,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                AionStatGauge(
                    label = "ASTRAL MANA (SİSTEM)",
                    value = "%92",
                    progress = 0.92f,
                    barColor = MpAetherBlue,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Aion 2 Themed Icon Grid Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(secondaryGold, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AION II KANATLI RÜN SİMGELERİ",
                    color = ElyosPearl,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3x3 Fantasy App Icons Grid
            val iconItems = listOf(
                Triple("Çanta", "Envanter", Icons.Default.Favorite),
                Triple("Yetenek", "Büyüler", Icons.Default.Star),
                Triple("Görevler", "Kayıt", Icons.Default.DateRange),
                Triple("Harita", "Atreia", Icons.Default.Place),
                Triple("Karakter", "Profil", Icons.Default.Person),
                Triple("Fısıltı", "Mesajlar", Icons.Default.Email),
                Triple("Gözcü", "Kamera", Icons.Default.PlayArrow),
                Triple("Demirci", "Araçlar", Icons.Default.Build),
                Triple("Ayarlar", "Rünler", Icons.Default.Settings)
            )

            for (row in iconItems.chunked(3)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (item in row) {
                        AionMMOIcon(
                            title = item.first,
                            category = item.second,
                            icon = item.third,
                            isElyos = isElyos,
                            onClick = {
                                Toast.makeText(
                                    context,
                                    "${item.first} (${item.second}) Aion II arayüzünde seçildi!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // "CANLI DUVAR KAĞIDI YAP" Main Action Button
            Button(
                onClick = {
                    try {
                        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                            putExtra(
                                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                ComponentName(context, AionLiveWallpaperService::class.java)
                            )
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "Canlı duvar kağıdı açılırken: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(16.dp, RoundedCornerShape(20.dp), spotColor = primaryAether)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(secondaryGold, primaryAether)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Text(
                    text = "✨ CANLI DUVAR KAĞIDINI UYGULA",
                    color = Color.Black,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "💡 Ekrana dokunarak veya sürükleyerek Aether ışık dalgalarını test edebilirsiniz.",
                color = ElyosPearl.copy(alpha = 0.6f),
                fontSize = 11.sp
            )
        }
    }
}
