# WeightView
This is an exercise program to study View of the drawing process.
compile 'com.gusi:WeightView:1.0.0'

```xml
  <declare-styleable name="WeightView">
    <attr name="weight_total" format="integer" />
    //总的可分配比例
    <attr name="border" format="dimension" />//所有的Border宽度(默认1)
    <attr name="top_border" format="dimension" />//外边框左侧Border，不定义时默认是border的值
    <attr name="left_border" format="dimension" />//...
    <attr name="right_border" format="dimension" />//...
    <attr name="bottom_border" format="dimension" />//...
    <attr name="child_gap" format="dimension" />//各Child View之间的间隔...
    <attr name="orientation">
      <flag name="horizontal" value="0" />//水平分布,宽度必须是 MeasureSpec.EXACTLY(match_parent或者明确的dp值)
      <flag name="vertical" value="1" />//垂直，。。。。
    </attr>
    <attr name="border_color" format="color" />//边框颜色
    <attr name="gap_color" format="color" />//间隔颜色
  </declare-styleable>

  <declare-styleable name="WeightChildView">
    <attr name="weight" format="integer" />//Child所占比例
    <attr name="vertical_gravity">//垂直分布 Child 所在位置
      <flag name="center" value="0" />
      <flag name="top" value="1" />
      <flag name="bottom" value="2" />
    </attr>

    <attr name="horizontal_gravity">//。。。
      <flag name="center" value="0" />
      <flag name="left" value="3" />
      <flag name="right" value="4" />
    </attr>
  </declare-styleable>
```


![image](https://github.com/batuer/WeightView/blob/master/example/img/Screenshot.png)
