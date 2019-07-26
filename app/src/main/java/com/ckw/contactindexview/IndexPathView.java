package com.ckw.contactindexview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ckw
 * on 2019/7/25.
 */
public class IndexPathView extends View {

    private int mLayerNum = 0;//item的数量,即横线的数量
    private float mLayerHeight = 48;//item的高度
    private int mWidth = 100;//控件宽度
    private int mHeight = 240;//控件高度
    private float mHorizontalWidth = 30;//横线宽度
    private float mSquareWidth = 4;
    private float mOutHeight = 10;//上方超出部分
    private Paint mPaint;//线条
    private Paint mSquarePaint;//小方块
    private int mSquareColor;
    private int mLineColor;
    private List<Integer> mFlagList;//当前展示的数据flag；是否是根节点
    private int mOffset;


    public IndexPathView(Context context) {
        this(context,null);
    }

    public IndexPathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndexPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexPathView);

        mLayerNum = typedArray.getInteger(R.styleable.IndexPathView_layerNum,0);
        mLayerHeight = typedArray.getDimension(R.styleable.IndexPathView_layerHeight,0f);
        mHorizontalWidth = typedArray.getDimension(R.styleable.IndexPathView_layerHorizontalWidth,0f);
        mSquareWidth = typedArray.getDimension(R.styleable.IndexPathView_squareWidth,0f);
        mOutHeight = typedArray.getDimension(R.styleable.IndexPathView_outHeight,20f);
        mSquareColor = typedArray.getColor(R.styleable.IndexPathView_squareColor, Color.BLUE);
        mLineColor = typedArray.getColor(R.styleable.IndexPathView_layerLineColor, Color.BLACK);
        float lineStroke = typedArray.getDimension(R.styleable.IndexPathView_layerLineStroke, 0f);
        float squareStroke = typedArray.getDimension(R.styleable.IndexPathView_squareStroke,0f);
        mHeight = (int) (mLayerHeight * (mLayerNum - 1) + mOutHeight + 8);
        mWidth = (int) ((mSquareWidth + mHorizontalWidth + 2) * 2);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(lineStroke);

        mSquarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSquarePaint.setStyle(Paint.Style.STROKE);
        mSquarePaint.setColor(mSquareColor);
        mSquarePaint.setStrokeWidth(squareStroke);

        mFlagList = new ArrayList<>();

        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int suggestWidth = MeasureSpec.getSize(widthMeasureSpec);
        int suggestHeight = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth,mHeight);
        }else{
            setMeasuredDimension(suggestWidth,suggestHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path verticalPath = new Path();
        verticalPath.moveTo(mWidth/2,0-mOffset);
        verticalPath.lineTo(mWidth/2, (float) (mHeight - mOffset + mFlagList.size()*0.5));
        canvas.drawPath(verticalPath,mPaint);
        drawHorizontalLine(mLayerNum,canvas);
    }

    /*
    * 画横线
    * */
    private void drawHorizontalLine(int lineNum,Canvas canvas){
        float originHeight = mOutHeight - mOffset;
        float centerX = mWidth / 2 - 2;
        for (int i = 0; i < lineNum; i++) {
            if(mFlagList.get(i) == 0){
                Path path = new Path();
                float x = centerX + mHorizontalWidth;
                float y = originHeight;
                path.moveTo(centerX,originHeight);
                path.lineTo(x,y);
                RectF rectF = new RectF(x,y-mSquareWidth/2,x+mSquareWidth,y+mSquareWidth/2);
                canvas.drawRect(rectF,mSquarePaint);
                canvas.drawPath(path,mPaint);
            }

            originHeight += (mLayerHeight + 0.5);

        }
    }

    public void setLayerColor(int layerColor){
        mLineColor = layerColor;
        invalidate();
    }

    public void setSquareColor(int squareColor){
        this.mSquareColor = squareColor;
    }


    public void updateView(List<Integer> currentList) {
        this.mLayerNum = getTrueLayer(currentList);
        mHeight = (int) (mLayerHeight * (mLayerNum - 1) + mOutHeight );
        mFlagList.clear();
        mFlagList.addAll(currentList);
        invalidate();
    }

    private int getTrueLayer(List<Integer> currentList){
        for (int i = currentList.size()-1; i >= 0; i--) {
            Integer flag = currentList.get(i);
            if(flag == 0){
                return i + 1;
            }
        }
        return currentList.size();
    }

    public void updateOffset(int offset) {
        mOffset = offset;
        invalidate();
    }
}
