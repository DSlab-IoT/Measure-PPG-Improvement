package com.example.luolab.measureppg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

// AppCompatActivity 用於現代 Android 開發


public class BreathGuidanceView extends View {
    private Paint circlePaint;
    private ValueAnimator breathAnimator;
    private float animationProgress = 0f;
    private boolean isInhaling = true;
    private TextView breathStateTextView;

    public BreathGuidanceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 初始化圓圈畫筆
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setAlpha(76); // 30% 透明度
        circlePaint.setStyle(Paint.Style.FILL);

        // 設置呼吸動畫
        breathAnimator = ValueAnimator.ofFloat(0f, 1f);
        breathAnimator.setDuration(4000); // 每個階段 4 秒
        breathAnimator.setInterpolator(new DecelerateInterpolator());
        breathAnimator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        breathAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // 切換呼吸階段
                isInhaling = !isInhaling;
                startBreathAnimation();
            }
        });
    }

    private void startBreathAnimation() {
        breathAnimator.start();
        // 更新文字狀態
        if (breathStateTextView != null) {
            breathStateTextView.setText(isInhaling ? "吸氣" : "吐氣");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 計算圓圈大小
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // 動態調整圓圈大小
        float baseRadius = 100f;
        float radiusVariation = 100f; // 最大變化範圍
        float currentRadius = baseRadius + (isInhaling ?
                animationProgress * radiusVariation :
                (1 - animationProgress) * radiusVariation);

        // 繪製呼吸圓圈
        canvas.drawCircle(centerX, centerY, currentRadius, circlePaint);
    }

    // 連結狀態文字
    public void setBreathStateTextView(TextView textView) {
        this.breathStateTextView = textView;
    }

    // 開始呼吸訓練
    public void startBreathTraining() {
        isInhaling = true;
        startBreathAnimation();
    }

    // 停止呼吸訓練
    public void stopBreathTraining() {
        breathAnimator.cancel();
    }
}

// 在 Activity 中使用範例
//public class MainActivity extends AppCompatActivity {
//    private BreathGuidanceView breathGuidanceView;
//    private TextView breathStateTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        breathGuidanceView = findViewById(R.id.breathGuidanceView);
//        breathStateTextView = findViewById(R.id.breathStateTextView);
//
//        // 將文字視圖連結到呼吸引導視圖
//        breathGuidanceView.setBreathStateTextView(breathStateTextView);
//
//        Button startButton = findViewById(R.id.startTrainingButton);
//        startButton.setOnClickListener(v -> breathGuidanceView.startBreathTraining());
//    }
//}