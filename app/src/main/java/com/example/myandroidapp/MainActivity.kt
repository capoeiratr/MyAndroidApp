package com.example.myandroidapp

import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.myandroidapp.ui.components.AionAppDrawer
import com.example.myandroidapp.ui.components.AionCyanNeon
import com.example.myandroidapp.ui.components.AionHomeScreen
import com.example.myandroidapp.ui.components.AionLockScreen
import com.example.myandroidapp.ui.components.AionQuickPanel
import com.example.myandroidapp.ui.components.AionSilver
import com.example.myandroidapp.ui.components.AionSteelSurface
import com.example.myandroidapp.ui.components.AionWallpaperCanvas
import com.example.myandroidapp.ui.theme.MyAndroidAppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LauncherScreen {
    HOME,
    DRAWER,
    QUICK_PANEL,
    LOCK_PREVIEW
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAndroidAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AionLauncherRoot(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AionLauncherRoot(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf(LauncherScreen.HOME) }
    var selectedWallpaper by remember { mutableIntStateOf(0) }
    var showShowcaseBar by remember { mutableStateOf(false) }

    val currentTime = remember {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.format(Date())
    }
    val currentDate = remember {
        val sdf = SimpleDateFormat("d MMMM EEEE", Locale("tr"))
        sdf.format(Date())
    }

    // Hardware back button navigation
    BackHandler(enabled = currentScreen != LauncherScreen.HOME || showShowcaseBar) {
        if (showShowcaseBar) {
            showShowcaseBar = false
        } else {
            currentScreen = LauncherScreen.HOME
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Main Screen Switcher
        when (currentScreen) {
            LauncherScreen.HOME -> {
                AionHomeScreen(
                    wallpaperIndex = selectedWallpaper,
                    currentTime = currentTime,
                    onOpenDrawer = { currentScreen = LauncherScreen.DRAWER },
                    onOpenQuickPanel = { currentScreen = LauncherScreen.QUICK_PANEL }
                )
            }
            LauncherScreen.DRAWER -> {
                AionAppDrawer(
                    onClose = { currentScreen = LauncherScreen.HOME }
                )
            }
            LauncherScreen.QUICK_PANEL -> {
                AionQuickPanel(
                    currentTime = currentTime,
                    currentDate = currentDate,
                    onClose = { currentScreen = LauncherScreen.HOME }
                )
            }
            LauncherScreen.LOCK_PREVIEW -> {
                AionLockScreen(
                    currentTime = currentTime,
                    currentDate = currentDate,
                    onUnlock = { currentScreen = LauncherScreen.HOME }
                )
            }
        }

        // Floating Floating Bottom Switcher Button (Shows the theme customizer bar from screenshot)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            AnimatedVisibility(
                visible = showShowcaseBar,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                AionBottomThemeControlBar(
                    selectedWallpaper = selectedWallpaper,
                    onSelectWallpaper = { selectedWallpaper = it },
                    onPreviewLock = { currentScreen = LauncherScreen.LOCK_PREVIEW },
                    onPreviewQuick = { currentScreen = LauncherScreen.QUICK_PANEL },
                    onApplyWallpaper = {
                        try {
                            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER).apply {
                                putExtra(
                                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                    ComponentName(context, AionLiveWallpaperService::class.java)
                                )
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(context, "Duvar kağıdı açılırken: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onSetDefaultLauncher = {
                        try {
                            val intent = Intent(Settings.ACTION_MANAGE_DEFAULT_APPS_SETTINGS)
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            try {
                                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                                context.startActivity(intent)
                            } catch (e2: Exception) {
                                Toast.makeText(context, "Ayarlar > Varsayılan Uygulamalar > Giriş Ekranı yolunu izleyin", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                )
            }

            // Small Floating Toggle Tab
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xE0101624))
                    .border(1.dp, AionCyanNeon.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .clickable { showShowcaseBar = !showShowcaseBar }
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = if (showShowcaseBar) "▼ Temayı Kapat" else "▲ AION 2 TEMA & DUVAR KAĞITLARI",
                    color = AionCyanNeon,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- BOTTOM THEME SHOWCASE & CONTROLLER (Matches the bottom row from screenshot) ---
@Composable
fun AionBottomThemeControlBar(
    selectedWallpaper: Int,
    onSelectWallpaper: (Int) -> Unit,
    onPreviewLock: () -> Unit,
    onPreviewQuick: () -> Unit,
    onApplyWallpaper: () -> Unit,
    onSetDefaultLauncher: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color(0xF80A0E18))
            .border(1.dp, AionCyanNeon.copy(alpha = 0.4f), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "DUVAR KAĞIDI SEÇENEKLERİ (CANLI)",
            color = AionSilver,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 4 Wallpaper Thumbnails
        val wallpapers = listOf(
            "Uçan Şehir",
            "Gümüş Seraph",
            "Astral Ay",
            "Aion Kanatları"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            wallpapers.forEachIndexed { index, name ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { onSelectWallpaper(index) }
                ) {
                    Box(
                        modifier = Modifier
                            .width(72.dp)
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (selectedWallpaper == index) 2.5.dp else 1.dp,
                                color = if (selectedWallpaper == index) AionCyanNeon else Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        AionWallpaperCanvas(wallpaperIndex = index, modifier = Modifier.fillMaxSize())
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = name,
                        color = if (selectedWallpaper == index) AionCyanNeon else AionSilver.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = if (selectedWallpaper == index) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Screen Preview Switchers (Lock Screen & Quick Panel)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onPreviewLock,
                modifier = Modifier.weight(1f).height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF162032))
            ) {
                Text("🔒 Kilit Ekranı", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = onPreviewQuick,
                modifier = Modifier.weight(1f).height(40.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF162032))
            ) {
                Text("⚡ Hızlı Ayarlar", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Action Buttons: Apply Wallpaper & Set as Default Launcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onApplyWallpaper,
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1))
            ) {
                Text("✨ Canlı Duvar Kağıdı Yap", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onSetDefaultLauncher,
                modifier = Modifier.weight(1f).height(46.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E5FF))
            ) {
                Text("🏠 Varsayılan Başlatıcı Yap", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
