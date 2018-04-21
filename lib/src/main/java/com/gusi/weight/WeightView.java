package com.gusi.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Author ylw  2018-03-27 10:10
 */
public class WeightView extends ViewGroup {
  private static final int HORIZONTAL = 0;
  private static final int VERTICAL = 1;

  private int mWeightTotal;
  private Paint mPaint;
  private Path mBorderPath;
  private int mTopBorder;
  private int mBottomBorder;
  private int mLeftBorder;
  private int mRightBorder;
  private int mChildGap;

  private final int mBorderColor;
  private final int mGapColor;
  private final int mOrientation;

  //@formatter:off
  public WeightView(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WeightView);
    try {
      mWeightTotal = array.getInteger(R.styleable.WeightView_weight_total, 0);
      int border = array.getDimensionPixelSize(R.styleable.WeightView_border, SizeUtils.dp2px(context, 1));
      mOrientation = array.getInt(R.styleable.WeightView_orientation, HORIZONTAL);
      mBorderColor = array.getColor(R.styleable.WeightView_border_color, Color.BLACK);
      mGapColor = array.getColor(R.styleable.WeightView_gap_color, mBorderColor);
      mTopBorder = array.getDimensionPixelOffset(R.styleable.WeightView_top_border, border);
      mLeftBorder = array.getDimensionPixelOffset(R.styleable.WeightView_left_border, border);
      mRightBorder = array.getDimensionPixelOffset(R.styleable.WeightView_right_border, border);
      mBottomBorder = array.getDimensionPixelOffset(R.styleable.WeightView_bottom_border, border);
      mChildGap = array.getDimensionPixelOffset(R.styleable.WeightView_child_gap, border);
    } finally {
      array.recycle();
    }

    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setColor(Color.BLACK);
    mPaint.setStyle(Paint.Style.STROKE);

    mBorderPath = new Path();
    setWillNotDraw(false);
  }
  //@formatter:on
  public int getTopBorder() {
    return mTopBorder;
  }

  public void setTopBorder(int topBorder) {
    mTopBorder = topBorder;
    invalidate();
  }

  public int getBottomBorder() {
    return mBottomBorder;
  }

  public void setBottomBorder(int bottomBorder) {
    mBottomBorder = bottomBorder;
    invalidate();
  }

  public int getLeftBorder() {
    return mLeftBorder;
  }

  public void setLeftBorder(int leftBorder) {
    mLeftBorder = leftBorder;
    invalidate();
  }

  public int getRightBorder() {
    return mRightBorder;
  }

  public void setRightBorder(int rightBorder) {
    mRightBorder = rightBorder;
    invalidate();
  }

  public int getChildGap() {
    return mChildGap;
  }

  public void setChildGap(int childGap) {
    mChildGap = childGap;
    invalidate();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mWeightTotal <= 0) {
      setMeasuredDimension(0, 0);
    } else if (mOrientation == HORIZONTAL
        && MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
      measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    } else if (mOrientation == VERTICAL
        && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
      measureVertical(widthMeasureSpec, heightMeasureSpec);
    } else {
      setMeasuredDimension(0, 0);
    }
  }

  private void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
    int usableHeight = heightSize - mTopBorder - mBottomBorder - (count - 1) * mChildGap;
    int maxChildWidth = 0;
    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      LayoutParams params = (LayoutParams) child.getLayoutParams();
      int childHeight = (int) (usableHeight * (float) params.weight / mWeightTotal + 0.5f);
      int heightSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
      int widthSpec = MeasureSpec.makeMeasureSpec(
          MeasureSpec.getSize(widthMeasureSpec) - mLeftBorder - mRightBorder,
          MeasureSpec.getMode(widthMeasureSpec));
      if (params.width != LayoutParams.MATCH_PARENT && params.width != LayoutParams.WRAP_CONTENT) {
        widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
      }
      child.measure(widthSpec, heightSpec);
      maxChildWidth = Math.max(maxChildWidth, child.getMeasuredWidth());
    }
    setMeasuredDimension(maxChildWidth + mLeftBorder + mRightBorder, heightSize);
  }

  private void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
    int count = getChildCount();
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    int usableWidth = widthSize - mLeftBorder - mRightBorder - (count - 1) * mChildGap;
    int maxChildHeight = 0;
    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      LayoutParams params = (LayoutParams) child.getLayoutParams();
      int childWidth = (int) (usableWidth * (float) params.weight / mWeightTotal + 0.5f);
      int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
      int heightSpec = MeasureSpec.makeMeasureSpec(
          MeasureSpec.getSize(heightMeasureSpec) - mTopBorder - mBottomBorder,
          MeasureSpec.getMode(heightMeasureSpec));
      if (params.height != LayoutParams.MATCH_PARENT
          && params.height != LayoutParams.WRAP_CONTENT) {
        heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
      }
      child.measure(widthSpec, heightSpec);
      maxChildHeight = Math.max(maxChildHeight, child.getMeasuredHeight());
    }
    setMeasuredDimension(widthSize, maxChildHeight + mTopBorder + mBottomBorder);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (mOrientation == HORIZONTAL) {
      layoutHorizontal();
    } else {
      layoutVertical();
    }
  }

  private void layoutVertical() {
    int measuredWidth = getMeasuredWidth();
    int count = getChildCount();
    int startTop = mTopBorder;

    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == GONE) continue;
      LayoutParams params = (LayoutParams) child.getLayoutParams();
      int childMWidth = child.getMeasuredWidth();
      int childMHeight = child.getMeasuredHeight();

      int left;
      int right;
      switch (params.gravity) {
        case LayoutParams.LEFT:
          left = mLeftBorder;
          right = left + childMWidth;
          break;
        case LayoutParams.RIGHT:
          right = measuredWidth - mRightBorder;
          left = right - childMWidth;
          break;
        default:
          left = ((measuredWidth - childMWidth - mLeftBorder - mRightBorder) / 2) + mLeftBorder;
          right = left + childMWidth;
          break;
      }
      child.layout(left, startTop, right, startTop + childMHeight);
      startTop = startTop + childMHeight + mChildGap;
    }
  }

  private void layoutHorizontal() {
    int measuredHeight = getMeasuredHeight();
    int count = getChildCount();
    int startLeft = mLeftBorder;
    for (int i = 0; i < count; i++) {
      View child = getChildAt(i);
      if (child.getVisibility() == GONE) continue;
      LayoutParams params = (LayoutParams) child.getLayoutParams();
      int childMWidth = child.getMeasuredWidth();
      int childMHeight = child.getMeasuredHeight();

      int top;
      int bottom;
      switch (params.gravity) {
        case LayoutParams.TOP:
          top = mTopBorder;
          bottom = top + childMHeight;
          break;
        case LayoutParams.BOTTOM:
          bottom = measuredHeight - mBottomBorder;
          top = bottom - childMHeight;
          break;
        default:
          top = ((measuredHeight - childMHeight - mTopBorder - mBottomBorder) / 2) + mTopBorder;
          bottom = top + childMHeight;
          break;
      }
      child.layout(startLeft, top, startLeft + childMWidth, bottom);
      startLeft = startLeft + childMWidth + mChildGap;
    }
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //draw border
    drawBorder(canvas);
  }

  private void drawBorder(Canvas canvas) {
    int measuredWidth = getMeasuredWidth();
    int measuredHeight = getMeasuredHeight();

    mPaint.setColor(mBorderColor);

    if (mLeftBorder > 0) {
      mBorderPath.reset();
      mBorderPath.moveTo((float) mLeftBorder / 2, mTopBorder);
      mBorderPath.lineTo((float) mLeftBorder / 2, measuredHeight - mBottomBorder);
      mPaint.setStrokeWidth(mLeftBorder);
      canvas.drawPath(mBorderPath, mPaint);
    }
    if (mRightBorder > 0) {
      mBorderPath.reset();
      mBorderPath.moveTo(measuredWidth - (float) mRightBorder / 2, mTopBorder);
      mBorderPath.lineTo(measuredWidth - (float) mRightBorder / 2, measuredHeight - mBottomBorder);
      mPaint.setStrokeWidth(mRightBorder);
      canvas.drawPath(mBorderPath, mPaint);
    }
    if (mTopBorder > 0) {
      mBorderPath.reset();
      mBorderPath.moveTo(0, (float) mTopBorder / 2);
      mBorderPath.lineTo(measuredWidth, (float) mTopBorder / 2);
      mPaint.setStrokeWidth(mTopBorder);
      canvas.drawPath(mBorderPath, mPaint);
    }
    if (mBottomBorder > 0) {
      mBorderPath.reset();
      mBorderPath.moveTo(0, measuredHeight - (float) mBottomBorder / 2);
      mBorderPath.lineTo(measuredWidth, measuredHeight - (float) mBottomBorder / 2);
      mPaint.setStrokeWidth(mBottomBorder);
      canvas.drawPath(mBorderPath, mPaint);
    }

    mPaint.setStrokeWidth(mChildGap);
    mPaint.setColor(mGapColor);

    if (mOrientation == HORIZONTAL) {
      for (int len = getChildCount(), i = 0; i < len; i++) {
        View child = getChildAt(i);
        int childRight = child.getRight();
        boolean isRight = (childRight + mChildGap) == measuredWidth;
        if ((i == len - 1 && isRight) || child.getVisibility() == GONE) continue;
        mBorderPath.reset();
        mBorderPath.moveTo(childRight + (float) mChildGap / 2, mTopBorder);
        mBorderPath.lineTo(childRight + (float) mChildGap / 2, measuredHeight - mBottomBorder);
        canvas.drawPath(mBorderPath, mPaint);
      }
    } else {
      for (int len = getChildCount(), i = 0; i < len; i++) {
        View child = getChildAt(i);
        int childBottom = child.getBottom();
        boolean isBottom = (mChildGap + childBottom) == measuredHeight;
        if ((i == len - 1 && isBottom) || child.getVisibility() == GONE) continue;
        mBorderPath.reset();
        mBorderPath.moveTo(mLeftBorder, childBottom + (float) mChildGap / 2);
        mBorderPath.lineTo(measuredWidth - mRightBorder, childBottom + (float) mChildGap / 2);
        canvas.drawPath(mBorderPath, mPaint);
      }
    }
  }

  @Override protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
    return p instanceof LayoutParams;
  }

  @Override public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new LayoutParams(getContext(), attrs);
  }

  @Override protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
    return new LayoutParams(p);
  }

  //先忽略Margin
  public static class LayoutParams extends ViewGroup.LayoutParams {
    private static final int CENTER = 0;
    private static final int TOP = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;

    public int weight;
    public int gravity = CENTER;

    public LayoutParams(Context c, AttributeSet attrs) {
      super(c, attrs);
      TypedArray array = c.obtainStyledAttributes(attrs, R.styleable.WeightChildView);
      try {
        weight = array.getInteger(R.styleable.WeightChildView_weight, 0);
        int vGravity = array.getInt(R.styleable.WeightChildView_vertical_gravity, CENTER);
        int hGravity = array.getInt(R.styleable.WeightChildView_horizontal_gravity, CENTER);
        gravity = Math.max(vGravity, hGravity);
      } finally {
        array.recycle();
      }
    }

    public LayoutParams(ViewGroup.LayoutParams p) {
      super(p);
    }
  }
}
