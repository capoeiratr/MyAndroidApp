package com.example.myandroidapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myandroidapp.ui.theme.AbyssCardBg
import com.example.myandroidapp.ui.theme.AbyssDarkNavy
import com.example.myandroidapp.ui.theme.AbyssDarkVoid
import com.example.myandroidapp.ui.theme.AetherCyan
import com.example.myandroidapp.ui.theme.AetherCyanBright
import com.example.myandroidapp.ui.theme.AsmodianAbyssVoid
import com.example.myandroidapp.ui.theme.AsmodianCrimson
import com.example.myandroidapp.ui.theme.AsmodianViolet
import com.example.myandroidapp.ui.theme.CelestialGold
import com.example.myandroidapp.ui.theme.CelestialGoldBright
import com.example.myandroidapp.ui.theme.ElyosPearl
import com.example.myandroidapp.ui.theme.HpRedGreen
import com.example.myandroidapp.ui.theme.MpAetherBlue
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class LiveParticle(
    var x: Float,
    var y: Float,
    val speed: Float,
    val size: Float,
    val color: Color,
    val phase: Float = Random.nextFloat() * 6.28f
)

data class TouchRippleEffect(
    val x: Float,
    val y: Float,
    var radius: Float = 0f,
    var alpha: Float = 1f
)

/**
 * Interactive Aion 2 Live Wallpaper Canvas preview with rotating runes,
 * floating aether motes, and touch-reactive ripples.
 */
@Composable
fun AionLiveWallpaperCanvas(
    modifier: Modifier = Modifier,
    isElyos: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "AionRuneRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(28000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "RuneCircleSpin"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AetherPulse"
    )

    val primaryColor = if (isElyos) AetherCyan else AsmodianCrimson
    val secondaryColor = if (isElyos) CelestialGold else AsmodianViolet
    val bgColor1 = if (isElyos) AbyssDarkVoid else AsmodianAbyssVoid
    val bgColor2 = if (isElyos) AbyssDarkNavy else Color(0xFF190623)

    val particles = remember(isElyos) {
        val list = mutableListOf<LiveParticle>()
        for (i in 0 until 45) {
            list.add(
                LiveParticle(
                    x = Random.nextFloat() * 1000f,
                    y = Random.nextFloat() * 2000f,
                    speed = Random.nextFloat() * 2f + 1f,
                    size = Random.nextFloat() * 6f + 2f,
                    color = if (Random.nextBoolean()) primaryColor else secondaryColor
                )
            )
        }
        list
    }

    val ripples = remember { mutableStateListOf<TouchRippleEffect>() }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    ripples.add(TouchRippleEffect(offset.x, offset.y))
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    if (ripples.size < 12) {
                        ripples.add(TouchRippleEffect(change.position.x, change.position.y))
                    }
                }
            }
    ) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h * 0.40f

        // 1. Celestial Cosmic Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(bgColor1, bgColor2, AbyssDarkVoid),
                startY = 0f,
                endY = h
            )
        )

        // 2. Aether Core Radial Glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    primaryColor.copy(alpha = 0.35f),
                    secondaryColor.copy(alpha = 0.15f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = (w * 0.55f) * pulse
            ),
            radius = (w * 0.55f) * pulse,
            center = Offset(cx, cy)
        )

        // 3. Rotating Aion 2 Celestial Runic Circle
        drawAionRuneRing(
            cx = cx,
            cy = cy,
            baseRadius = w * 0.32f,
            angle = rotation,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor
        )

        // 4. Update and Draw Floating Aether Feathers / Particles
        particles.forEach { p ->
            p.y -= p.speed
            if (p.y < -10f) {
                p.y = h + 10f
                p.x = Random.nextFloat() * w
            }
            // Draw core particle
            drawCircle(
                color = p.color.copy(alpha = 0.85f),
                radius = p.size,
                center = Offset(p.x, p.y)
            )
            // Draw soft halo
            drawCircle(
                color = p.color.copy(alpha = 0.25f),
                radius = p.size * 2.2f,
                center = Offset(p.x, p.y)
            )
        }

        // 5. Update and Draw Touch Ripples
        val iterator = ripples.iterator()
        while (iterator.hasNext()) {
            val r = iterator.next()
            r.radius += 5f
            r.alpha -= 0.03f
            if (r.alpha <= 0f) {
                iterator.remove()
            } else {
                drawCircle(
                    color = primaryColor.copy(alpha = r.alpha),
                    radius = r.radius,
                    center = Offset(r.x, r.y),
                    style = Stroke(width = 2.5f * r.alpha)
                )
                drawCircle(
                    color = secondaryColor.copy(alpha = r.alpha * 0.7f),
                    radius = r.radius * 0.65f,
                    center = Offset(r.x, r.y),
                    style = Stroke(width = 1.5f * r.alpha)
                )
            }
        }
    }
}

private fun DrawScope.drawAionRuneRing(
    cx: Float,
    cy: Float,
    baseRadius: Float,
    angle: Float,
    primaryColor: Color,
    secondaryColor: Color
) {
    // Outer concentric circles
    drawCircle(
        color = secondaryColor.copy(alpha = 0.6f),
        radius = baseRadius,
        center = Offset(cx, cy),
        style = Stroke(width = 2f)
    )
    drawCircle(
        color = secondaryColor.copy(alpha = 0.4f),
        radius = baseRadius * 0.94f,
        center = Offset(cx, cy),
        style = Stroke(width = 1.5f)
    )
    drawCircle(
        color = primaryColor.copy(alpha = 0.7f),
        radius = baseRadius * 0.72f,
        center = Offset(cx, cy),
        style = Stroke(width = 2f)
    )

    // Outer spokes / Runic Cardinal Points (8 spokes)
    val rad = Math.toRadians(angle.toDouble())
    for (i in 0 until 8) {
        val spokeAngle = rad + i * (Math.PI / 4)
        val x1 = cx + (cos(spokeAngle) * baseRadius * 0.94f).toFloat()
        val y1 = cy + (sin(spokeAngle) * baseRadius * 0.94f).toFloat()
        val x2 = cx + (cos(spokeAngle) * baseRadius).toFloat()
        val y2 = cy + (sin(spokeAngle) * baseRadius).toFloat()
        drawLine(
            color = secondaryColor.copy(alpha = 0.8f),
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = 2.5f
        )
        // Runic astral orbs
        val dotX = cx + (cos(spokeAngle + 0.1) * baseRadius * 0.83f).toFloat()
        val dotY = cy + (sin(spokeAngle + 0.1) * baseRadius * 0.83f).toFloat()
        drawCircle(
            color = primaryColor.copy(alpha = 0.9f),
            radius = 3.5f,
            center = Offset(dotX, dotY)
        )
    }

    // Inner Aion Celestial Hexagram
    val innerR = baseRadius * 0.45f
    val radInner = Math.toRadians(-angle * 1.5)
    val path = Path()
    for (i in 0 until 6) {
        val a = radInner + i * (Math.PI / 3)
        val px = cx + (cos(a) * innerR).toFloat()
        val py = cy + (sin(a) * innerR).toFloat()
        if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
    }
    path.close()
    drawPath(
        path = path,
        color = ElyosPearl.copy(alpha = 0.45f),
        style = Stroke(width = 1.5f)
    )
}

/**
 * Aion 2 MMORPG Themed App Icon with Runic Gold/Cyan Frame and Aether Glow
 */
@Composable
fun AionMMOIcon(
    title: String,
    category: String,
    icon: ImageVector,
    isElyos: Boolean = true,
    onClick: () -> Unit = {}
) {
    val borderColor = if (isElyos) CelestialGold else AsmodianCrimson
    val aetherGlow = if (isElyos) AetherCyan else AsmodianViolet

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(6.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(18.dp), spotColor = aetherGlow)
                .clip(RoundedCornerShape(18.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            AbyssCardBg,
                            AbyssDarkNavy
                        )
                    )
                )
                .border(
                    width = 1.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(borderColor, aetherGlow, borderColor)
                    ),
                    shape = RoundedCornerShape(18.dp)
                )
        ) {
            // Wing aura decorative corner dot
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .align(Alignment.TopEnd)
                    .padding(end = 4.dp, top = 4.dp)
                    .background(aetherGlow, CircleShape)
            )

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isElyos) CelestialGoldBright else ElyosPearl,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            color = ElyosPearl,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = category,
            color = aetherGlow.copy(alpha = 0.8f),
            fontSize = 9.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

/**
 * MMORPG HP / MP Style Gauge Widget (Battery & RAM / Storage)
 */
@Composable
fun AionStatGauge(
    label: String,
    value: String,
    progress: Float,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = CelestialGoldBright,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                color = ElyosPearl,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF060914))
                .border(0.8.dp, CelestialGold.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(barColor.copy(alpha = 0.6f), barColor)
                        )
                    )
            )
        }
    }
}
