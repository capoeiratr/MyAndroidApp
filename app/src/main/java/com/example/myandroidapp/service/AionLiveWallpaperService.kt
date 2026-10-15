package com.example.myandroidapp.service

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Aion 2 Ethereal Live Wallpaper Service
 * Renders glowing aether particles, rotating celestial runic circles, and touch-reactive ripples.
 */
class AionLiveWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return AionWallpaperEngine()
    }

    inner class AionWallpaperEngine : Engine() {
        private val handler = Handler(Looper.getMainLooper())
        private var visible = false

        // Drawing tools
        private val bgPaint = Paint()
        private val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val runePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val ripplePaint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Dimensions
        private var screenWidth = 1080f
        private var screenHeight = 2400f

        // Animation states
        private var rotationAngle = 0f
        private var pulsePhase = 0f

        // Particles & Touch Ripples
        private val particles = ArrayList<AetherParticle>()
        private val ripples = ArrayList<TouchRipple>()
        private val maxParticles = 65

        // Faction Colors (Default: Elyos Aether Gold & Cyan)
        private val cAetherCyan = Color.argb(230, 0, 229, 255)
        private val cCelestialGold = Color.argb(240, 255, 215, 0)
        private val cAetherWhite = Color.argb(255, 255, 255, 255)
        private val cSpaceDarkTop = Color.rgb(8, 12, 28)
        private val cSpaceDarkBottom = Color.rgb(4, 6, 14)

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
                if (visible) {
                    handler.postDelayed(this, 16) // ~60 FPS
                }
            }
        }

        override fun onCreate(surfaceHolder: SurfaceHolder) {
            super.onCreate(surfaceHolder)
            setTouchEventsEnabled(true)
            initParticles()
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                handler.post(drawRunnable)
            } else {
                handler.removeCallbacks(drawRunnable)
            }
        }

        override fun onSurfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            super.onSurfaceChanged(holder, format, width, height)
            screenWidth = width.toFloat()
            screenHeight = height.toFloat()
            initParticles()
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            visible = false
            handler.removeCallbacks(drawRunnable)
        }

        override fun onTouchEvent(event: MotionEvent) {
            super.onTouchEvent(event)
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE) {
                ripples.add(TouchRipple(event.x, event.y))
                // Burst extra sparkling particles at touch point
                for (i in 0 until 5) {
                    particles.add(
                        AetherParticle(
                            x = event.x + Random.nextFloat() * 40 - 20,
                            y = event.y + Random.nextFloat() * 40 - 20,
                            vx = (Random.nextFloat() - 0.5f) * 4f,
                            vy = (Random.nextFloat() - 0.8f) * 5f,
                            size = Random.nextFloat() * 6f + 3f,
                            alpha = 1f,
                            color = if (Random.nextBoolean()) cCelestialGold else cAetherCyan
                        )
                    )
                }
            }
        }

        private fun initParticles() {
            particles.clear()
            for (i in 0 until maxParticles) {
                particles.add(
                    AetherParticle(
                        x = Random.nextFloat() * screenWidth,
                        y = Random.nextFloat() * screenHeight,
                        vx = (Random.nextFloat() - 0.5f) * 0.8f,
                        vy = -(Random.nextFloat() * 1.5f + 0.5f),
                        size = Random.nextFloat() * 7f + 2f,
                        alpha = Random.nextFloat() * 0.8f + 0.2f,
                        color = if (Random.nextFloat() > 0.4f) cAetherCyan else cCelestialGold
                    )
                )
            }
        }

        private fun drawFrame() {
            val holder = surfaceHolder
            var canvas: Canvas? = null
            try {
                canvas = holder.lockCanvas()
                if (canvas != null) {
                    renderScene(canvas)
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas)
                }
            }
        }

        private fun renderScene(canvas: Canvas) {
            val cx = screenWidth / 2f
            val cy = screenHeight * 0.45f

            // 1. Celestial Cosmic Gradient Background
            bgPaint.shader = LinearGradient(
                0f, 0f, 0f, screenHeight,
                intArrayOf(cSpaceDarkTop, Color.rgb(12, 19, 44), cSpaceDarkBottom),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawRect(0f, 0f, screenWidth, screenHeight, bgPaint)

            // 2. Ambient Aether Nebula Core Glow
            pulsePhase += 0.03f
            val corePulse = (sin(pulsePhase.toDouble()).toFloat() * 0.15f + 0.85f)
            val glowRadius = (screenWidth * 0.55f) * corePulse

            glowPaint.shader = RadialGradient(
                cx, cy, glowRadius,
                intArrayOf(
                    Color.argb(90, 0, 229, 255),
                    Color.argb(35, 255, 215, 0),
                    Color.TRANSPARENT
                ),
                floatArrayOf(0f, 0.45f, 1f),
                Shader.TileMode.CLAMP
            )
            canvas.drawCircle(cx, cy, glowRadius, glowPaint)

            // 3. Rotating Aion 2 Runic Magic Circle
            rotationAngle = (rotationAngle + 0.35f) % 360f
            drawAionRuneCircle(canvas, cx, cy, rotationAngle)

            // 4. Update and Draw Touch Ripples
            val rippleIterator = ripples.iterator()
            ripplePaint.style = Paint.Style.STROKE
            while (rippleIterator.hasNext()) {
                val r = rippleIterator.next()
                r.radius += 7f
                r.alpha -= 0.025f
                if (r.alpha <= 0f) {
                    rippleIterator.remove()
                } else {
                    ripplePaint.color = cAetherCyan
                    ripplePaint.alpha = (r.alpha * 255).toInt().coerceIn(0, 255)
                    ripplePaint.strokeWidth = 3f * r.alpha
                    canvas.drawCircle(r.x, r.y, r.radius, ripplePaint)

                    ripplePaint.color = cCelestialGold
                    ripplePaint.alpha = (r.alpha * 180).toInt().coerceIn(0, 255)
                    canvas.drawCircle(r.x, r.y, r.radius * 0.6f, ripplePaint)
                }
            }

            // 5. Update and Draw Aether Feathers / Floating Light Particles
            val pIterator = particles.iterator()
            while (pIterator.hasNext()) {
                val p = pIterator.next()
                p.x += p.vx + sin(pulsePhase + p.phase) * 0.5f
                p.y += p.vy

                // Respawn at bottom if off screen
                if (p.y < -20f) {
                    p.y = screenHeight + 20f
                    p.x = Random.nextFloat() * screenWidth
                }
                if (p.x < -20f) p.x = screenWidth + 20f
                if (p.x > screenWidth + 20f) p.x = -20f

                particlePaint.color = p.color
                particlePaint.alpha = ((sin(pulsePhase * 2 + p.phase) * 0.3f + 0.7f) * p.alpha * 255).toInt().coerceIn(0, 255)
                canvas.drawCircle(p.x, p.y, p.size, particlePaint)

                // Particle soft glow halo
                particlePaint.alpha = (particlePaint.alpha * 0.3f).toInt()
                canvas.drawCircle(p.x, p.y, p.size * 2.2f, particlePaint)
            }

            // Clean up temporary burst particles
            if (particles.size > maxParticles) {
                particles.removeAt(particles.size - 1)
            }
        }

        private fun drawAionRuneCircle(canvas: Canvas, cx: Float, cy: Float, angle: Float) {
            runePaint.style = Paint.Style.STROKE
            runePaint.color = cCelestialGold
            runePaint.strokeWidth = 2f
            runePaint.alpha = 140

            val baseR = screenWidth * 0.34f

            // Outer ring
            canvas.drawCircle(cx, cy, baseR, runePaint)
            canvas.drawCircle(cx, cy, baseR * 0.94f, runePaint)

            // Inner counter-rotating ring
            runePaint.color = cAetherCyan
            runePaint.alpha = 160
            canvas.drawCircle(cx, cy, baseR * 0.72f, runePaint)
            canvas.drawCircle(cx, cy, baseR * 0.45f, runePaint)

            // Outer spokes / celestial runes (8 points)
            val rad1 = Math.toRadians(angle.toDouble())
            for (i in 0 until 8) {
                val spokeAngle = rad1 + i * (Math.PI / 4)
                val x1 = cx + (cos(spokeAngle) * baseR * 0.94f).toFloat()
                val y1 = cy + (sin(spokeAngle) * baseR * 0.94f).toFloat()
                val x2 = cx + (cos(spokeAngle) * baseR).toFloat()
                val y2 = cy + (sin(spokeAngle) * baseR).toFloat()
                canvas.drawLine(x1, y1, x2, y2, runePaint)

                // Runic dots
                val dotX = cx + (cos(spokeAngle + 0.1) * baseR * 0.83f).toFloat()
                val dotY = cy + (sin(spokeAngle + 0.1) * baseR * 0.83f).toFloat()
                canvas.drawCircle(dotX, dotY, 3.5f, runePaint)
            }

            // Inner Aion Star / 6-Pointed Runic Hexagram
            val rad2 = Math.toRadians(-angle * 1.5)
            val path = Path()
            val innerR = baseR * 0.45f
            for (i in 0 until 6) {
                val a = rad2 + i * (Math.PI / 3)
                val px = cx + (cos(a) * innerR).toFloat()
                val py = cy + (sin(a) * innerR).toFloat()
                if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
            }
            path.close()
            runePaint.color = cAetherWhite
            runePaint.alpha = 110
            canvas.drawPath(path, runePaint)
        }
    }

    private data class AetherParticle(
        var x: Float,
        var y: Float,
        var vx: Float,
        var vy: Float,
        var size: Float,
        var alpha: Float,
        var color: Int,
        val phase: Float = Random.nextFloat() * 6.28f
    )

    private data class TouchRipple(
        val x: Float,
        val y: Float,
        var radius: Float = 10f,
        var alpha: Float = 1.0f
    )
}
