package com.example.myandroidapp.ui.components

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

// --- AION 2 METALLIC & AETHER COLOR SYSTEM ---
val AionSteelDark = Color(0xFF090D15)
val AionSteelSurface = Color(0xFF141C2B)
val AionSteelBevelLight = Color(0xFF88A0C4)
val AionSteelBevelDark = Color(0xFF05070D)
val AionCyanNeon = Color(0xFF00E5FF)
val AionCyanSoft = Color(0xFF80D8FF)
val AionAetherBlue = Color(0xFF2979FF)
val AionSilver = Color(0xFFE2E8F0)
val AionGoldAccent = Color(0xFFFFD700)

data class ThemedApp(
    val id: String,
    val name: String,
    val iconType: String,
    val accentColor: Color = AionCyanNeon,
    val packageName: String? = null
)

// --- AION 2 CUSTOM WALLPAPER CANVAS RENDERER ---
@Composable
fun AionWallpaperCanvas(
    wallpaperIndex: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AionWallpaperAnim")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "NebulaPulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        when (wallpaperIndex) {
            0 -> {
                // WALLPAPER 0: Floating Sky Citadel & Waterfall Fortress
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D1B2A),
                            Color(0xFF1B263B),
                            Color(0xFF0F2037),
                            Color(0xFF070B14)
                        )
                    )
                )
                // Sky Castle Rings
                drawCircle(
                    color = AionCyanNeon.copy(alpha = 0.25f * pulse),
                    radius = w * 0.45f,
                    center = Offset(w * 0.72f, h * 0.28f),
                    style = Stroke(width = 2.5f)
                )
                drawCircle(
                    color = AionCyanSoft.copy(alpha = 0.15f),
                    radius = w * 0.32f,
                    center = Offset(w * 0.72f, h * 0.28f),
                    style = Stroke(width = 1.5f)
                )
                // Mystic Citadel Waterfall Streams
                for (i in 0 until 6) {
                    val streamX = w * (0.45f + i * 0.08f)
                    drawLine(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, AionCyanNeon.copy(alpha = 0.45f), Color.Transparent),
                            startY = h * 0.25f,
                            endY = h * 0.58f
                        ),
                        start = Offset(streamX, h * 0.25f),
                        end = Offset(streamX - 10f, h * 0.58f),
                        strokeWidth = 3f
                    )
                }
                // Winged Seraph Silhouette on Cliff (Left)
                val cliff = Path().apply {
                    moveTo(0f, h)
                    lineTo(0f, h * 0.62f)
                    lineTo(w * 0.38f, h * 0.72f)
                    lineTo(w * 0.48f, h * 0.88f)
                    lineTo(w * 0.30f, h)
                    close()
                }
                drawPath(cliff, Color(0xFF05080E))
                // Seraph Wings Light
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AionSilver.copy(alpha = 0.5f), Color.Transparent),
                        center = Offset(w * 0.22f, h * 0.60f),
                        radius = 90f
                    ),
                    radius = 90f,
                    center = Offset(w * 0.22f, h * 0.60f)
                )
            }
            1 -> {
                // WALLPAPER 1: Ethereal Silver-Haired Empress & Cosmic Nebula
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF080612),
                            Color(0xFF141226),
                            Color(0xFF1A213B),
                            Color(0xFF05070E)
                        )
                    )
                )
                // Astral Nebula Aura
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AionAetherBlue.copy(alpha = 0.35f * pulse), Color(0xFF9C27B0).copy(alpha = 0.2f), Color.Transparent),
                        center = Offset(w * 0.65f, h * 0.35f),
                        radius = w * 0.6f
                    ),
                    radius = w * 0.6f,
                    center = Offset(w * 0.65f, h * 0.35f)
                )
                // Tiara & Runic Arch
                drawCircle(
                    color = AionCyanNeon.copy(alpha = 0.3f),
                    radius = w * 0.25f,
                    center = Offset(w * 0.5f, h * 0.32f),
                    style = Stroke(width = 2f)
                )
            }
            2 -> {
                // WALLPAPER 2: Giant Celestial Moon & Stone Spire
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF060D1A), Color(0xFF112338), Color(0xFF060B12))
                    )
                )
                // Giant Moon
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE0F7FA), AionCyanNeon.copy(alpha = 0.4f), Color.Transparent),
                        center = Offset(w * 0.75f, h * 0.22f),
                        radius = w * 0.35f
                    ),
                    radius = w * 0.35f,
                    center = Offset(w * 0.75f, h * 0.22f)
                )
            }
            3 -> {
                // WALLPAPER 3: Pure Void & Glowing Metallic Seraph Wing Crest
                drawRect(Color(0xFF04060A))
                // Wing Crest Glow
                val cx = w * 0.5f
                val cy = h * 0.42f
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(AionCyanNeon.copy(alpha = 0.4f * pulse), Color.Transparent),
                        center = Offset(cx, cy),
                        radius = w * 0.4f
                    ),
                    radius = w * 0.4f,
                    center = Offset(cx, cy)
                )
                // Central Aion Wing Crest Path
                for (dir in listOf(-1f, 1f)) {
                    val wing = Path().apply {
                        moveTo(cx, cy + 40f)
                        cubicTo(cx + dir * 60f, cy - 20f, cx + dir * 130f, cy - 100f, cx + dir * 140f, cy - 150f)
                        cubicTo(cx + dir * 110f, cy - 90f, cx + dir * 80f, cy - 50f, cx, cy - 20f)
                        close()
                    }
                    drawPath(wing, AionCyanNeon.copy(alpha = 0.75f))
                    drawPath(wing, AionSilver, style = Stroke(width = 1.8f))
                }
            }
        }
    }
}

// --- PIXEL-PERFECT AION 2 DARK METALLIC BEVELED ICON ---
@Composable
fun AionMetallicIcon(
    name: String,
    iconType: String,
    accentColor: Color = AionCyanNeon,
    sizeDp: Int = 62,
    showLabel: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    ) {
        // Metallic Squircle Frame
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(sizeDp.dp)
                .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp), spotColor = accentColor)
                .clip(RoundedCornerShape(20.dp))
                // Outer Bevel Metallic Border
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AionSteelBevelLight.copy(alpha = 0.8f),
                            AionSteelBevelDark,
                            AionSteelBevelLight.copy(alpha = 0.4f)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(100f, 100f)
                    )
                )
                .padding(1.8.dp) // Bevel thickness
                // Inner Dark Slate Radial Texture
                .clip(RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF1E2838),
                            Color(0xFF101622),
                            Color(0xFF080B12)
                        )
                    )
                )
                .border(
                    width = 0.8.dp,
                    color = accentColor.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            // Corner Metallic Screw/Runic Studs
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .align(Alignment.TopStart)
                    .padding(start = 3.dp, top = 3.dp)
                    .background(AionSteelBevelLight, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .align(Alignment.TopEnd)
                    .padding(end = 3.dp, top = 3.dp)
                    .background(AionSteelBevelLight, CircleShape)
            )

            // Inner Icon Glyph with Aion Glow
            RenderIconGlyph(
                iconType = iconType,
                accentColor = accentColor,
                size = (sizeDp * 0.46).dp
            )
        }

        if (showLabel) {
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = name,
                color = AionSilver,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun RenderIconGlyph(
    iconType: String,
    accentColor: Color,
    size: androidx.compose.ui.unit.Dp
) {
    when (iconType) {
        "phone" -> Icon(Icons.Default.Call, null, tint = accentColor, modifier = Modifier.size(size))
        "messages" -> Icon(Icons.Default.Send, null, tint = accentColor, modifier = Modifier.size(size))
        "camera" -> Icon(Icons.Default.PlayArrow, null, tint = AionSilver, modifier = Modifier.size(size))
        "gallery" -> Icon(Icons.Default.Star, null, tint = AionCyanNeon, modifier = Modifier.size(size))
        "clock" -> Icon(Icons.Default.DateRange, null, tint = AionSilver, modifier = Modifier.size(size))
        "contacts" -> Icon(Icons.Default.Person, null, tint = accentColor, modifier = Modifier.size(size))
        "settings" -> Icon(Icons.Default.Settings, null, tint = AionSilver, modifier = Modifier.size(size))
        "calendar" -> Icon(Icons.Default.DateRange, null, tint = AionSilver, modifier = Modifier.size(size))
        "calculator" -> Icon(Icons.Default.Build, null, tint = AionSilver, modifier = Modifier.size(size))
        "notes" -> Icon(Icons.Default.Email, null, tint = AionSilver, modifier = Modifier.size(size))
        "playstore" -> Icon(Icons.Default.PlayArrow, null, tint = AionCyanNeon, modifier = Modifier.size(size))
        "health" -> Icon(Icons.Default.Favorite, null, tint = AionCyanSoft, modifier = Modifier.size(size))
        "whatsapp" -> Icon(Icons.Default.Call, null, tint = Color(0xFF25D366), modifier = Modifier.size(size))
        "discord" -> Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF7289DA), modifier = Modifier.size(size))
        "youtube" -> Icon(Icons.Default.PlayArrow, null, tint = Color(0xFFFF0000), modifier = Modifier.size(size))
        "chrome" -> Icon(Icons.Default.Star, null, tint = AionCyanNeon, modifier = Modifier.size(size))
        "gmail" -> Icon(Icons.Default.Email, null, tint = Color(0xFFEA4335), modifier = Modifier.size(size))
        "files" -> Icon(Icons.Default.Build, null, tint = AionSilver, modifier = Modifier.size(size))
        "maps" -> Icon(Icons.Default.LocationOn, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(size))
        "drive" -> Icon(Icons.Default.Share, null, tint = Color(0xFF00E5FF), modifier = Modifier.size(size))
        "spotify" -> Icon(Icons.Default.PlayArrow, null, tint = Color(0xFF1DB954), modifier = Modifier.size(size))
        "steam" -> Icon(Icons.Default.Build, null, tint = AionCyanNeon, modifier = Modifier.size(size))
        "aion" -> Icon(Icons.Default.Star, null, tint = AionCyanNeon, modifier = Modifier.size(size * 1.2f))
        else -> Icon(Icons.Default.Star, null, tint = accentColor, modifier = Modifier.size(size))
    }
}

// --- 1. AION 2 LOCK SCREEN (Kilit Ekranı) ---
@Composable
fun AionLockScreen(
    currentTime: String = "17:56",
    currentDate: String = "28 Eylül Pazar",
    onUnlock: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onUnlock)
    ) {
        AionWallpaperCanvas(wallpaperIndex = 0, modifier = Modifier.fillMaxSize())

        // Top Status Bar (Turkcell & Battery)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Turkcell", color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("%100", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Lock Screen Clock & Weather
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp)
        ) {
            Icon(Icons.Default.Lock, null, tint = Color.White.copy(alpha = 0.7f), modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 62.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp
            )
            Text(
                text = currentDate,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⛅ 26°", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Mersin", color = Color.White.copy(alpha = 0.75f), fontSize = 13.sp)
            }
        }

        // Bottom Aion 2 Glowing Title Logo & Shortcuts
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
        ) {
            Text(
                text = "AION 2",
                color = AionCyanNeon,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 5.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Bottom Shortcuts (Phone & Camera)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(Icons.Default.Phone, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(46.dp)
                        .background(Color.White.copy(alpha = 0.15f), CircleShape)
                ) {
                    Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

// --- 2. AION 2 HOME SCREEN (Ana Ekran) ---
@Composable
fun AionHomeScreen(
    wallpaperIndex: Int,
    currentTime: String = "17:56",
    onOpenDrawer: () -> Unit = {},
    onOpenQuickPanel: () -> Unit = {}
) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        AionWallpaperCanvas(wallpaperIndex = wallpaperIndex, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Status Bar & Quick Panel Pull Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOpenQuickPanel)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(currentTime, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("%100", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Top AION 2 Crest & Weather
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "AION 2",
                        color = AionCyanNeon,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⛅ 26°", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Mersin", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp)
                        Text("Parçalı bulutlu", color = Color.White.copy(alpha = 0.6f), fontSize = 10.sp)
                    }
                }
            }

            // Google Pill Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xD0121722))
                    .border(1.dp, AionSteelBevelLight.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .clickable {
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
                            context.startActivity(intent)
                        } catch (e: Exception) {}
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("G", color = Color(0xFF4285F4), fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Google'da arayın", color = AionSilver.copy(alpha = 0.5f), fontSize = 13.sp)
                    }
                    Row {
                        Icon(Icons.Default.Search, null, tint = AionCyanNeon, modifier = Modifier.size(18.dp))
                    }
                }
            }

            // 4x2 Home Apps Grid (as shown in image)
            val homeApps = listOf(
                ThemedApp("wa", "WhatsApp", "whatsapp", Color(0xFF25D366), "com.whatsapp"),
                ThemedApp("dc", "Discord", "discord", Color(0xFF7289DA), "com.discord"),
                ThemedApp("yt", "YouTube", "youtube", Color(0xFFFF0000), "com.google.android.youtube"),
                ThemedApp("gal", "Galeri", "gallery", AionCyanNeon, "com.sec.android.gallery3d"),
                ThemedApp("cam", "Kamera", "camera", AionSilver, "com.sec.android.app.camera"),
                ThemedApp("set", "Ayarlar", "settings", AionSilver, "com.android.settings"),
                ThemedApp("play", "Play Store", "playstore", AionCyanNeon, "com.android.vending"),
                ThemedApp("far", "Farmakit", "aion", AionCyanNeon, null)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(homeApps) { app ->
                    AionMetallicIcon(
                        name = app.name,
                        iconType = app.iconType,
                        accentColor = app.accentColor,
                        onClick = { launchOrFallback(context, app.packageName, app.name) }
                    )
                }
            }

            // Page Dots & Dock
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Box(modifier = Modifier.size(6.dp).background(Color.White, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(6.dp).background(Color.White.copy(alpha = 0.3f), CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(modifier = Modifier.size(6.dp).background(Color.White.copy(alpha = 0.3f), CircleShape))
                }

                // 5-Icon Dock
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AionMetallicIcon("Telefon", "phone", AionCyanNeon, 56, false) {
                        launchIntent(context, Intent(Intent.ACTION_DIAL))
                    }
                    AionMetallicIcon("Mesajlar", "messages", AionCyanNeon, 56, false) {
                        launchIntent(context, Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING))
                    }
                    // AION Crest Drawer Opener (Middle)
                    AionMetallicIcon("Uygulamalar", "aion", AionCyanNeon, 60, false, onClick = onOpenDrawer)
                    AionMetallicIcon("Chrome", "chrome", AionCyanNeon, 56, false) {
                        launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")))
                    }
                    AionMetallicIcon("AION 2", "aion", AionCyanNeon, 56, false, onClick = onOpenDrawer)
                }
            }
        }
    }
}

// --- 3. AION 2 APP DRAWER (Uygulama Çekmecesi) ---
@Composable
fun AionAppDrawer(
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }

    val allApps = listOf(
        ThemedApp("tel", "Telefon", "phone", AionCyanNeon, "com.samsung.android.dialer"),
        ThemedApp("mes", "Mesajlar", "messages", AionCyanNeon, "com.samsung.android.messaging"),
        ThemedApp("kam", "Kamera", "camera", AionSilver, "com.sec.android.app.camera"),
        ThemedApp("gal", "Galeri", "gallery", AionCyanNeon, "com.sec.android.gallery3d"),
        ThemedApp("saat", "Saat", "clock", AionSilver, "com.sec.android.app.clockpackage"),
        ThemedApp("kis", "Kişiler", "contacts", AionCyanNeon, "com.samsung.android.app.contacts"),
        ThemedApp("ayar", "Ayarlar", "settings", AionSilver, "com.android.settings"),
        ThemedApp("tak", "Takvim", "calendar", AionSilver, "com.samsung.android.calendar"),
        ThemedApp("hes", "Hesap Makinesi", "calculator", AionSilver, null),
        ThemedApp("not", "Notlar", "notes", AionSilver, null),
        ThemedApp("play", "Play Store", "playstore", AionCyanNeon, "com.android.vending"),
        ThemedApp("hea", "Samsung Health", "health", AionCyanSoft, null),
        ThemedApp("dc", "Discord", "discord", Color(0xFF7289DA), "com.discord"),
        ThemedApp("yt", "YouTube", "youtube", Color(0xFFFF0000), "com.google.android.youtube"),
        ThemedApp("chr", "Chrome", "chrome", AionCyanNeon, "com.android.chrome"),
        ThemedApp("gm", "Gmail", "gmail", Color(0xFFEA4335), "com.google.android.gm"),
        ThemedApp("dos", "Dosyalarım", "files", AionSilver, "com.sec.android.app.myfiles"),
        ThemedApp("har", "Haritalar", "maps", AionCyanNeon, "com.google.android.apps.maps"),
        ThemedApp("drv", "Drive", "drive", AionCyanNeon, "com.google.android.apps.docs"),
        ThemedApp("far", "Farmakit", "aion", AionCyanNeon, null),
        ThemedApp("oyun", "Oyunlar", "aion", AionCyanNeon, null),
        ThemedApp("spot", "Spotify", "spotify", Color(0xFF1DB954), "com.spotify.music"),
        ThemedApp("stm", "Steam", "steam", AionCyanNeon, "com.valvesoftware.android.steam.community"),
        ThemedApp("aion", "AION 2", "aion", AionCyanNeon, null)
    )

    val filtered = if (query.isEmpty()) allApps else allApps.filter { it.name.contains(query, ignoreCase = true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF5080C14))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Bar & Close
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Color(0xFF161F2E))
                        .border(1.dp, AionSteelBevelLight.copy(alpha = 0.3f), RoundedCornerShape(22.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, null, tint = AionCyanNeon, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (query.isEmpty()) "Ara" else query,
                            color = if (query.isEmpty()) AionSilver.copy(alpha = 0.5f) else Color.White,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF161F2E))
                        .clickable(onClick = onClose)
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4-Column Metallic Icon Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filtered) { app ->
                    AionMetallicIcon(
                        name = app.name,
                        iconType = app.iconType,
                        accentColor = app.accentColor,
                        onClick = {
                            launchOrFallback(context, app.packageName, app.name)
                        }
                    )
                }
            }
        }
    }
}

// --- 4. AION 2 QUICK SETTINGS / NOTIFICATION PANEL ---
@Composable
fun AionQuickPanel(
    currentTime: String = "17:56",
    currentDate: String = "28 Eylül Pazar",
    onClose: () -> Unit = {}
) {
    val context = LocalContext.current
    var wifiOn by remember { mutableStateOf(true) }
    var btOn by remember { mutableStateOf(true) }
    var soundOn by remember { mutableStateOf(true) }
    var rotateOn by remember { mutableStateOf(true) }
    var torchOn by remember { mutableStateOf(false) }
    var flightOn by remember { mutableStateOf(false) }

    var brightness by remember { mutableFloatStateOf(0.7f) }
    var volume by remember { mutableFloatStateOf(0.6f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xF2070A12))
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header: Clock & Settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(currentTime, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(currentDate, color = AionCyanNeon, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
                Row {
                    Icon(
                        Icons.Default.Settings,
                        null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { launchIntent(context, Intent(Settings.ACTION_SETTINGS)) }
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Icon(
                        Icons.Default.Close,
                        null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable(onClick = onClose)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Big Pill Toggles (Wi-Fi & Bluetooth)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Wi-Fi Pill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (wifiOn) Color(0xFF1976D2) else Color(0xFF182232))
                        .clickable { wifiOn = !wifiOn }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Default.Share, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Wi-Fi", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(if (wifiOn) "Bağlı" else "Kapalı", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                        }
                    }
                }

                // Bluetooth Pill
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(58.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (btOn) Color(0xFF1976D2) else Color(0xFF182232))
                        .clickable { btOn = !btOn }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(Icons.Default.Star, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Bluetooth", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(if (btOn) "Açık" else "Kapalı", color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6 Circular Quick Action Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QuickCircleToggle(Icons.Default.Notifications, soundOn) { soundOn = !soundOn }
                QuickCircleToggle(Icons.Default.Share, rotateOn) { rotateOn = !rotateOn }
                QuickCircleToggle(Icons.Default.PlayArrow, torchOn) { torchOn = !torchOn }
                QuickCircleToggle(Icons.Default.Send, flightOn) { flightOn = !flightOn }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dual Sliders (Brightness & Sound)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(19.dp))
                    .background(Color(0xFF182232))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("☀️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Slider(
                        value = brightness,
                        onValueChange = { brightness = it },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AION 2 Music Player Card ("Beyond the Skies")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF121B2A), Color(0xFF1A273D))
                        )
                    )
                    .border(1.dp, AionCyanNeon.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(AionSteelSurface)
                            .border(1.dp, AionCyanNeon, RoundedCornerShape(14.dp))
                    ) {
                        Text("AION 2", color = AionCyanNeon, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text("AION 2", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Beyond the Skies", color = AionCyanNeon, fontSize = 12.sp)
                    }

                    Row {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(26.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications
            Text("BİLDİRİMLER", color = AionSilver.copy(alpha = 0.6f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            NotificationItem("WhatsApp", "2 bildirim", "17:40", Color(0xFF25D366))
            Spacer(modifier = Modifier.height(8.dp))
            NotificationItem("Gmail", "1 yeni e-posta", "16:22", Color(0xFFEA4335))
            Spacer(modifier = Modifier.height(8.dp))
            NotificationItem("Farmakit", "Sıcaklık normal aralıkta", "15:18", AionCyanNeon)

            Spacer(modifier = Modifier.weight(1f))

            // Clear all notifications
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFF182232))
                    .clickable { onClose() }
            ) {
                Text("Tüm bildirimleri temizle", color = AionSilver, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun QuickCircleToggle(
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(46.dp)
            .clip(CircleShape)
            .background(if (active) Color(0xFF1976D2) else Color(0xFF182232))
            .clickable(onClick = onClick)
    ) {
        Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun NotificationItem(
    title: String,
    content: String,
    time: String,
    badgeColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF131B29))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(badgeColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text(content, color = AionSilver.copy(alpha = 0.7f), fontSize = 11.sp)
                }
            }
            Text(time, color = AionSilver.copy(alpha = 0.5f), fontSize = 11.sp)
        }
    }
}

// --- HELPER LAUNCHERS FOR REAL PHONE APPS ---
private fun launchOrFallback(context: Context, packageName: String?, fallbackName: String) {
    if (packageName != null) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
            return
        }
    }
    // Generic fallback intents
    when (fallbackName.lowercase()) {
        "telefon" -> launchIntent(context, Intent(Intent.ACTION_DIAL))
        "kamera" -> launchIntent(context, Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        "ayarlar" -> launchIntent(context, Intent(Settings.ACTION_SETTINGS))
        "haritalar" -> launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0")))
        "chrome" -> launchIntent(context, Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")))
        else -> {
            // Feedback notification
            android.widget.Toast.makeText(context, "$fallbackName başlatıldı!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}

private fun launchIntent(context: Context, intent: Intent) {
    try {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {}
}
