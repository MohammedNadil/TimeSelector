package com.timeselect.timeselector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import android.graphics.PorterDuffXfermode


class TimerPickView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var changer: TimeChanger? = null
    private var initialTime = 0
    private var endTime = 60

    private var start_color: Int
    private var end_color: Int
    private var lineColor: Int
    private var selectColor: Int

    var seltime = 0

    val margin = 30f
    val marginbottm = -60f
    val borderwidth = 8f


    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val bgColor = Paint(Paint.ANTI_ALIAS_FLAG)

    private val selectedColor = Paint(Paint.ANTI_ALIAS_FLAG)

    private val textsetColor = Paint(Paint.ANTI_ALIAS_FLAG)


    var selectprogress = 0f + margin

    init {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerPickView)
        val borderColor = typedArray.getColor(
            R.styleable.TimerPickView_addBorderColor,
            ContextCompat.getColor(context, R.color.yellow_border)
        )

        endTime = typedArray.getInt(R.styleable.TimerPickView_addEndTime, 60)
        if (endTime < 15) {
            endTime = 15
        }

        start_color = typedArray.getColor(
            R.styleable.TimerPickView_addStartColor,
            ContextCompat.getColor(context, R.color.start_color2)
        )
        end_color = typedArray.getColor(
            R.styleable.TimerPickView_addEndColor,
            ContextCompat.getColor(context, R.color.end_color2)
        )

        lineColor = typedArray.getColor(
            R.styleable.TimerPickView_addlineColor,
            ContextCompat.getColor(context, R.color.white)
        )

        selectColor = typedArray.getColor(
            R.styleable.TimerPickView_addselectColor,
            ContextCompat.getColor(context, R.color.selected_white)
        )

        val textColor = typedArray.getColor(
            R.styleable.TimerPickView_addtextColor,
            ContextCompat.getColor(context, R.color.white)
        )

        val bodyColor = typedArray.getColor(
            R.styleable.TimerPickView_addbodyColor,
            ContextCompat.getColor(context, R.color.black)
        )

        borderPaint.apply {
            strokeWidth = borderwidth
            color = borderColor
            style = Paint.Style.STROKE
        }

        bgColor.apply {
            color = bodyColor
            strokeWidth = 3f
        }

        selectedColor.apply {
            color = selectColor
            strokeWidth = 3f
        }

        textsetColor.apply {
            color = textColor
            textSize = 40F
        }


        typedArray.recycle()


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(parentWidth, parentHeight / 7)
    }

    @SuppressLint("DrawAllocation", "ClickableViewAccessibility")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val clear = Paint(Paint.ANTI_ALIAS_FLAG)
        clear.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        val maxslide = (width / 1f) - margin
        val minslide = 0f + margin

        val borderRect = RectF(minslide, minslide, maxslide, height + marginbottm)
        val selectedRect = RectF(minslide, minslide, selectprogress, height + marginbottm)
        val cornerRadius = height / 2f
        val maxline = height / 4f

        canvas?.apply {

            // black box
            drawRect(0f, height / 1f, width / 1f, 0f + marginbottm, bgColor)


            val equalwidth = (width - (2 * margin)) / endTime


            // used for adjustment in lowerscreen sizes
            val timecheck = (maxslide / equalwidth).toInt()
            val appendingval = endTime - timecheck

            seltime = (selectprogress / equalwidth).toInt() + appendingval

            val dif: Int = endTime / 10
            val maxdif = endTime - dif
            val mindif = initialTime + dif

            Log.d("canvastimecheck", "$timecheck")
            if (seltime < maxdif) {
                drawText("${endTime}s", maxslide - 70f, height / 1f, textsetColor)
            }

            if (seltime > mindif) {
                drawText("0s", minslide + 15f, height / 1f, textsetColor)
            }
            changer?.timeChanged(seltime)

            when {
                selectprogress <= minslide + 15f -> {
                    drawText("${seltime}s", minslide + 15f, height / 1f, textsetColor)
                }
                selectprogress >= maxslide - 70f -> {
                    drawText("${seltime}s", maxslide - 70f, height / 1f, textsetColor)
                }
                else -> {
                    drawText("${seltime}s", selectprogress, height / 1f, textsetColor)
                }
            }


            //cut center
            val path = Path()
            path.addRoundRect(borderRect, cornerRadius, cornerRadius, Path.Direction.CW)
            clipPath(path)

            val roundfigure = 71
            val roundequalwidth = (width - (2 * margin)) / roundfigure
//           Draw lines
            for (i in initialTime..roundfigure) {

                val startx = if (i == initialTime) {
                    0f + margin
                } else {
                    0f + margin + (roundequalwidth * i)
                }

                val small = maxline / 4
                val medium = (maxline / 4) * 2
                val large = (maxline / 4) * 3

                val lineheight = when (i % 7) {
                    1, 3, 5 -> {
                        medium
                    }
                    0, 4, 6 -> {
                        small
                    }
                    else -> {
                        large
                    }
                }
                Log.d("canvas", "lineheight = $lineheight")

                val mid_of_the_slider_height = height + margin + marginbottm
                val midofslider = mid_of_the_slider_height / 2

                Log.d("canvas", "midofslider = ${startx + margin}")
                Log.d("canvas", "midofslider half = $maxslide")


                if ((startx + margin) < (maxslide - 5)) {

                    drawLine(
                        startx + 15,
                        midofslider + (lineheight / 2),
                        startx + 15,
                        midofslider,
                        Paint().apply {
                            strokeWidth = roundequalwidth / 2
                            color = lineColor
                        }
                    )

                    drawLine(
                        startx + 15,
                        midofslider,
                        startx + 15,
                        midofslider - (lineheight / 2),
                        Paint().apply {
                            strokeWidth = roundequalwidth / 2
                            color = lineColor
                        }
                    )
                }


            }
//white shade
            drawRoundRect(selectedRect, cornerRadius, cornerRadius, selectedColor)

//black border
            drawRoundRect(borderRect, cornerRadius, cornerRadius, borderPaint)


            val shader1 =
                LinearGradient(
                    0f,
                    0f,
                    selectprogress + 100f,
                    0f,
                    start_color,
                    end_color,
                    Shader.TileMode.CLAMP
                )


            val gcolor = Paint().apply {
                shader = shader1
                strokeWidth = borderwidth
                style = Paint.Style.STROKE
            }


            //white shade border
            drawRoundRect(selectedRect, cornerRadius, cornerRadius, gcolor)


            setOnTouchListener { _, event ->
                val x = (event?.x ?: 0).toInt()
                when (event?.action) {
                    MotionEvent.ACTION_MOVE -> {
                        var xval = x.toFloat()
                        if (xval > maxslide) {
                            xval = maxslide
                        } else if (xval < minslide) {
                            xval = minslide
                        }
                        selectprogress = xval
                        invalidate()
                    }
                }
                true
            }
        }

    }

    fun setTimeChangeListener(changer: TimeChanger) {
        this.changer = changer
    }

    fun getSelectedTime(): Int {
        return seltime
    }
}
