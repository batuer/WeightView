package com.gusi.weight;

import android.content.Context;

/**
 * @Author ylw  2018-04-08 11:41
 */
public class SizeUtils {
  /**
   * dp 转 px
   *
   * @param dpValue dp 值
   * @return px 值
   */
  public static int dp2px(Context context, final float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
