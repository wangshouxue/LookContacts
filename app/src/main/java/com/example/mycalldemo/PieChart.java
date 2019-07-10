package com.example.mycalldemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;

public class PieChart extends View {

    private int width, height;

    private List<PieChartData> values;

    private int[] colors = {0xff1eb1ed, 0xfffdbf2d, 0xfffc0d1b};

    private float startAngle = 0; // 开始角度

    private Paint paint = new Paint();

    private float R, r, middleR;  // 圆半径

    private int radius = 55;

    private float sumValue = 0;

    private Paint textPaint; // 画图例文字

    private Paint lablePaint; // 饼块上文字

    private int rightOffset; // 圆从中心向右的偏移量

    private float legendX; // 图例第一个小矩形左上点坐标

    private float legendY;

    // 饼块中文字相关
    private float textWidthHalf;

    private float textHeightHalf;

    private float x;

    private float y;



    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#000000"));
        textPaint.setTextSize(34);

        lablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lablePaint.setColor(Color.BLACK);
        lablePaint.setTextSize(28);

        R = dip2px(context, radius);
        r = dip2px(context, radius - 27);
        middleR = dip2px(context, radius - 20);

        rightOffset = dip2px(context, 60);

        legendX = dip2px(context, -110);
        legendY = dip2px(context, -27);

        int textWidth = getTextWidth(textPaint, "30");
        int textHeight = getTextHeight(textPaint, "30");

        textWidthHalf = textWidth / 2 - 3;
        textHeightHalf = textHeight / 2 - 3;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (values == null) return;
        float currentAngle = startAngle;     // 当前角度
        canvas.translate(width / 2, height / 2);  // 圆心移动到屏幕中心点
        RectF rect = new RectF(-R + rightOffset, -R, R + rightOffset, R);   // 圆的外切矩形
        if (sumValue == 0) {
            paint.setColor(Color.parseColor("#cccccc"));
            canvas.drawCircle(0, 0, R, paint);
        } else {
            float textAngle;
            for (int i = 0; i < values.size(); i++) {
                PieChartData pie = values.get(i);
                paint.setColor(pie.getColor());
                canvas.drawArc(rect, currentAngle, pie.getAngle(), true, paint);

                textAngle = currentAngle + pie.getAngle() / 2;
                drawPieLable(textAngle);

                canvas.drawText("" + pie.getValue(), x, y, lablePaint);
                currentAngle += pie.getAngle();
            }
        }

        // 中间圆和最小圆
//        paint.setColor(Color.parseColor("#aaffffff"));
//        canvas.drawCircle(rightOffset, 0, middleR, paint);
//        paint.setColor(Color.WHITE);
//        canvas.drawCircle(rightOffset, 0, r, paint);

        drawLegend(canvas);

    }


    // 画每个饼块的文字
    private void drawPieLable(float textAngle) {
        float percent = 0.85f;
        if (textAngle >= 0 && textAngle < 90) {
            x = (float) (percent * R * Math.cos(Math.toRadians(textAngle))) + rightOffset - textWidthHalf;
            y = (float) (percent * R * Math.sin(Math.toRadians(textAngle))) + textHeightHalf;
        } else if (textAngle >= 90 && textAngle <= 180) {
            x = (float) (-0.75 * R * Math.cos(Math.toRadians(180 - textAngle))) + rightOffset - textWidthHalf;
            y = (float) (percent * R * Math.sin(Math.toRadians(180 - textAngle))) + textHeightHalf;
        } else if (textAngle > 180 && textAngle <= 270) {
            x = (float) (-percent * R * Math.cos(Math.toRadians(textAngle - 180))) + rightOffset - textWidthHalf;
            y = (float) (-percent * R * Math.sin(Math.toRadians(textAngle - 180))) + textHeightHalf;
        } else {
            x = (float) (percent * R * Math.cos(Math.toRadians(360 - textAngle))) + rightOffset - textWidthHalf;
            y = (float) (-percent * R * Math.sin(Math.toRadians(360 - textAngle))) + textHeightHalf;
        }
    }


    // 画图例
    private void drawLegend(Canvas canvas) {
        RectF rectF1 = new RectF(legendX, legendY, legendX + 30, legendY + 30);
        paint.setColor(colors[0]);
        canvas.drawRect(rectF1, paint);

        RectF rectF2 = new RectF(legendX, legendY + 70, legendX + 30, legendY + 100);
        paint.setColor(colors[1]);
        canvas.drawRect(rectF2, paint);

        RectF rectF3 = new RectF(legendX, legendY + 140, legendX + 30, legendY + 170);
        paint.setColor(colors[2]);
        canvas.drawRect(rectF3, paint);
        https://github.com/wuseal/PieChartView.git

        canvas.drawText("有效完成次数", legendX + 50, legendY + 27, textPaint);
        canvas.drawText("无效完成次数", legendX + 50, legendY + 97, textPaint);
        canvas.drawText("逾期未办次数", legendX + 50, legendY + 167, textPaint);
    }


    // 设置起始角度
    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }


    // 设置数据源
    public void setValues(List<PieChartData> values) {
        this.values = values;
        initData(values);
        invalidate();
    }


    // 整合数据
    private void initData(List<PieChartData> values) {
        if (values == null || values.size() == 0) {
            return;
        }

        for (int i = 0; i < values.size(); i++) {
            PieChartData pieData = values.get(i);
            sumValue += pieData.getValue();  // 求和
            pieData.setColor(colors[i]);   // 设置颜色
        }

        for (int i = 0; i < values.size(); i++) {
            PieChartData pieData = values.get(i);

            float percentage = pieData.getValue() / sumValue;  // 百分比
            float angle = percentage * 360;  // 对应的角度

            pieData.setPersent(percentage);
            pieData.setAngle(angle);
        }
    }


    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getTextHeight(Paint paint, String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


    private int getTextWidth(Paint paint, String text) {
        return (int) paint.measureText(text);
    }

    public static class PieChartData{
        int color;
        float angle;
        float persent;
        float value;
        public PieChartData(int color,float angle,float persent,float value){
            this.color=color;
            this.angle=angle;
            this.persent=persent;
            this.value=value;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }

        public float getAngle() {
            return angle;
        }

        public void setPersent(float persent) {
            this.persent = persent;
        }

        public float getPersent() {
            return persent;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public float getValue() {
            return value;
        }
    }
}
