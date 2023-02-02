package com.udacity.uicomponents

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import com.udacity.R
import kotlin.math.min
import kotlin.properties.Delegates


class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr)  {
    init {
        isClickable = true

    }

    var loadingBtnTextSize = 0f
    var loadingBtnBgColor = 0
    var loadingBtnProgressColor = 0
    var loadingBtnCircleColor = 0
    var loadingBtnDoneColor = 0
    var loadingBtnTextColor = 0
    var loadingBtnText = ""
    var loadingButtonTextDone = ""
    var loadingButtonTextProgress = ""
    var loadingButtonCornerRadius = 0f
    var loadingButtonDisableProgress = false
    var loadingButtonLoadDuration = 5000L

    private var radius: Float = 0f
    private var widthSize = 0
    private var heightSize = 0

    @Volatile
    private var progress: Double = 0.0
    private lateinit var valueAnimator: ValueAnimator
      var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Initial) { p, old, new ->
    }


    private val updateListener = ValueAnimator.AnimatorUpdateListener {
        progress = (it.animatedValue as Float).toDouble()

        invalidate()
        requestLayout()
    }


    private fun initValueAnimator() {
        valueAnimator = AnimatorInflater.loadAnimator(
            context,
            R.animator.loading_animation
        ) as ValueAnimator
        valueAnimator.duration = loadingButtonLoadDuration
        valueAnimator.repeatCount = Animation.INFINITE
        valueAnimator.addUpdateListener(updateListener)
    }

    private fun initStyleable(attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            loadingBtnTextSize = getDimension(R.styleable.LoadingButton_loading_btn_text_size, 32f)
            loadingBtnBgColor = getColor(
                R.styleable.LoadingButton_loading_btn_bg_color,
                ContextCompat.getColor(context, R.color.colorPrimary)
            )
            loadingBtnProgressColor = getColor(
                R.styleable.LoadingButton_loading_btn_progress_color,
                ContextCompat.getColor(context, R.color.colorAccent)
            )
            loadingBtnCircleColor =
                getColor(R.styleable.LoadingButton_loading_btn_circle_color, Color.YELLOW)
            loadingBtnDoneColor =
                getColor(R.styleable.LoadingButton_loading_btn_done_color,
                    ContextCompat.getColor(context, R.color.download_success))
            loadingBtnTextColor = getColor(
                R.styleable.LoadingButton_loading_btn_text_color,
                ContextCompat.getColor(context, R.color.white)
            )
            loadingBtnText = getString(R.styleable.LoadingButton_loading_btn_text).toString()
            loadingButtonTextDone =
                getString(R.styleable.LoadingButton_loading_btn_text_done).toString()
            loadingButtonTextProgress =
                getString(R.styleable.LoadingButton_loading_btn_text_progress).toString()
            loadingButtonCornerRadius =
                getDimension(R.styleable.LoadingButton_loading_btn_corner_radius, 50f)
            loadingButtonDisableProgress =
                getBoolean(R.styleable.LoadingButton_loading_btn_disable_progress, false)

        }

    }

    init {
        initValueAnimator()
        initStyleable(attrs)


    }


    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER // button text alignment
        textSize = loadingBtnTextSize //button text size
        typeface = Typeface.create("", Typeface.BOLD)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }


    override fun performClick(): Boolean {
        super.performClick()
        if(buttonState== ButtonState.Loading) {
            animation()
        }
        return true
    }

    private fun animation() {
        valueAnimator.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (min(width, height) / 2.0 * 0.8).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawRectangle(canvas)
        drawText(canvas)
        drawCircle(canvas)

    }

    private fun drawRectangle(canvas: Canvas?) {
        if(buttonState == ButtonState.Completed){
            paint.color = loadingBtnDoneColor
        }else {
            paint.color = loadingBtnBgColor
        }
        canvas?.drawRoundRect(
            0f,
            0f,
            widthSize.toFloat(),
            heightSize.toFloat(),
            loadingButtonCornerRadius,
            radius,
            paint
        )
        if (buttonState == ButtonState.Loading && loadingButtonDisableProgress == false) {
            isEnabled = false
            paint.color = loadingBtnProgressColor
            canvas?.drawRoundRect(
                0f,
                0f,
                (widthSize * (progress / 100)).toFloat(),
                heightSize.toFloat(),
                loadingButtonCornerRadius,
                loadingButtonCornerRadius,
                paint
            )

        }

        if (isEnabled == false) {
            paint.color = Color.GRAY
            paint.alpha = 95
        }

    }


    private fun drawText(canvas: Canvas?) {
        val buttonText =
            if (buttonState == ButtonState.Loading && loadingButtonDisableProgress == false) {
                loadingButtonTextProgress
            } else if (buttonState == ButtonState.Completed && loadingButtonDisableProgress == false) {
                loadingButtonTextDone

            } else {
                loadingBtnText
            }

        // write the text on custom button
        paint.color = loadingBtnTextColor
        canvas?.drawText(buttonText, (width / 2).toFloat(), ((height + 30) / 2).toFloat(), paint)
    }

    private fun drawCircle(canvas: Canvas?) {
        if (buttonState == ButtonState.Loading && loadingButtonDisableProgress == false) {
            canvas?.let {
                paint.color = loadingBtnCircleColor
                val rect = RectF()
                rect.set(
                    (widthSize / 1.1 - radius).toFloat(), heightSize / 2 - radius,
                    (widthSize / 1.1 + radius).toFloat(), heightSize / 2 + radius
                )
                canvas.drawArc(rect, 0f, (progress * 3.6).toFloat(), true, paint);
            }
        }
    }

    fun hasCompletedDownload() {
        // cancel the animation when file is downloaded
        valueAnimator.cancel()
        buttonState = ButtonState.Completed
        isEnabled = true
        invalidate()
        requestLayout()
    }


}