package cn.surine.lazyandroid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;

import static android.graphics.drawable.GradientDrawable.Orientation.BL_TR;
import static android.graphics.drawable.GradientDrawable.Orientation.BOTTOM_TOP;
import static android.graphics.drawable.GradientDrawable.Orientation.BR_TL;
import static android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT;
import static android.graphics.drawable.GradientDrawable.Orientation.RIGHT_LEFT;
import static android.graphics.drawable.GradientDrawable.Orientation.TL_BR;
import static android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM;
import static android.graphics.drawable.GradientDrawable.Orientation.TR_BL;

/**
 * Intro：一个可以支持设置shape属性的button/textView
 *
 * @author sunliwei
 * @date 2019-08-08 14:34
 */
@SuppressLint("SupportAnnotationUsage")
public class ColorfulButton extends AppCompatButton {

    Context context;

    /**
     * Shape is a rectangle, possibly with rounded corners
     */
    public static final int RECTANGLE = 0;

    /**
     * Shape is an ellipse
     */
    public static final int OVAL = 1;

    /**
     * Shape is a line
     */
    public static final int LINE = 2;

    /**
     * Shape is a ring.
     */
    public static final int RING = 3;

    /**
     * Gradient is linear (default.)
     */
    public static final int LINEAR = 0;

    /**
     * Gradient is circular.
     */
    public static final int RADIAL = 1;

    /**
     * Gradient is a sweep.
     */
    public static final int SWEEP = 2;

    public static final int TOP_BOTTOM_INT = 0;
    public static final int TR_BL_INT = 1;
    public static final int RIGHT_LEFT_INT = 2;
    public static final int BR_TL_INT = 3;
    public static final int BOTTOM_TOP_INT = 4;
    public static final int BL_TR_INT = 5;
    public static final int LEFT_RIGHT_INT = 6;
    public static final int TL_BR_INT = 7;

    /**
     * 默认颜色
     */
    private final int NORMAL_COLOR = Color.parseColor("#DDDDDD");

    /**
     * shape形状
     */
    private int shape;

    /**
     * 默认，按压，聚焦 color
     */
    private int color;
    private int pressColor;
    private int focusColor;

    /**
     * 水波纹颜色，仅支持api>23
     */
    private int rippleColor;


    /**
     * corner radius
     * 优先级比cornerArray高，因此cornerRadius会覆盖cornerArray的值
     */
    @Px
    private float cornerRadius;

    /**
     * corner array
     */
    @Px
    private float[] cornerArray;

    /**
     * stroke width
     */
    @Px
    private float strokeWidth;

    /**
     * stroke color
     */
    @Px
    private int strokeColor;

    /**
     * dash gap
     */
    @Px
    private float dashGap;

    /**
     * dash width
     */
    @Px
    private float dashWidth;


    /**
     * 渐变类型
     */
    private int gradient;
    /**
     * 渐变颜色
     */
    private int startColor;
    private int centerColor;
    private int endColor;
    /**
     * 渐变角度
     */
    @Px
    private float gradientRadius;

    /**
     * 渐变方向
     */
    private int gradientOrientation;


    /**
     * 渐变数组
     * 高级渐变，可以设置多个颜色，仅支持Java
     */
    private int[] gradientColors;


    /**
     * 锁定按压背景，设置渐变色的时候会强制让普通状态和press状态的按钮颜色一样
     */
    private boolean isLockPressColor = true;


    public ColorfulButton(Context context) {
        this(context, null);
    }

    public ColorfulButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ColorfulButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfulButton);
        shape = typedArray.getInteger(R.styleable.ColorfulButton_shape, RECTANGLE);
        color = typedArray.getColor(R.styleable.ColorfulButton_color, NORMAL_COLOR);
        pressColor = typedArray.getColor(R.styleable.ColorfulButton_pressColor, NORMAL_COLOR);
        focusColor = typedArray.getColor(R.styleable.ColorfulButton_focusColor, NORMAL_COLOR);
        rippleColor = typedArray.getColor(R.styleable.ColorfulButton_rippleColor, Color.parseColor("#00000000"));
        cornerRadius = typedArray.getDimension(R.styleable.ColorfulButton_cornerRadius, 8);
        strokeColor = typedArray.getColor(R.styleable.ColorfulButton_strokeColor, NORMAL_COLOR);
        strokeWidth = typedArray.getDimension(R.styleable.ColorfulButton_strokeWidth, 0);

        gradient = typedArray.getInteger(R.styleable.ColorfulButton_gradient, LINEAR);
        gradientOrientation = typedArray.getInteger(R.styleable.ColorfulButton_gradientOrientation, 6);
        startColor = typedArray.getColor(R.styleable.ColorfulButton_startColor, NORMAL_COLOR);
        centerColor = typedArray.getColor(R.styleable.ColorfulButton_centerColor, startColor);
        endColor = typedArray.getColor(R.styleable.ColorfulButton_endColor, centerColor);
        gradientRadius = typedArray.getDimension(R.styleable.ColorfulButton_gradientRadius, 8);


        typedArray.recycle();
        update();
    }


    /**
     * set the shape
     * 设置当前形状
     *
     * @param shape
     */
    public ColorfulButton setShape(int shape) {
        if (shape < RECTANGLE || shape > RING) {
            throw new IllegalArgumentException("shape enum is not in the range");
        }
        this.shape = shape;
        return this;
    }


    /**
     * set the solid color
     * 设置背景颜色
     *
     * @param color
     */
    public ColorfulButton setColor(int color) {
        this.color = color;
        update();
        return this;
    }


    /**
     * 设置圆角半径
     *
     * @param cornerRadius 半径 px
     */
    public ColorfulButton setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        update();
        return this;
    }

    /**
     * get the corner array
     * 获取圆角数组
     */
    public float[] getCornerArray() {
        return cornerArray;
    }


    /**
     * set the corner array
     * 设置圆角数组,用于高级圆角
     * 接受8个值，分别为左上，右上，左下，右下4个端点圆心x,y坐标
     *
     * @param cornerArray
     * @see GradientDrawable#setCornerRadii(float[])
     */
    public ColorfulButton setCornerArray(float[] cornerArray) {
        this.cornerArray = cornerArray;
        update();
        return this;
    }


    /**
     * 设置边框宽度
     *
     * @param strokeWidth px
     */
    public ColorfulButton setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        update();
        return this;
    }


    /**
     * 设置边框颜色
     *
     * @param strokeColor
     */
    public ColorfulButton setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        update();
        return this;
    }


    /**
     * 设置虚线间隔
     *
     * @param dashGap px
     */
    public ColorfulButton setDashGap(float dashGap) {
        this.dashGap = dashGap;
        update();
        return this;
    }


    /**
     * 设置虚线主体长度
     *
     * @param dashWidth px
     */
    public ColorfulButton setDashWidth(float dashWidth) {
        this.dashWidth = dashWidth;
        update();
        return this;
    }


    /**
     * 设置按压时候的颜色
     *
     * @param pressColor
     */
    public ColorfulButton setPressColor(int pressColor) {
        this.pressColor = pressColor;
        update();
        return this;
    }


    /**
     * 设置聚焦颜色
     *
     * @param focusColor
     */
    public ColorfulButton setFocusColor(int focusColor) {
        this.focusColor = focusColor;
        update();
        return this;
    }


    /**
     * 设置水波纹颜色 (required api23)
     *
     * @param rippleColor
     */
    public ColorfulButton setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        update();
        return this;
    }


    /**
     * 设置渐变类型
     *
     * @param gradient
     */
    public ColorfulButton setGradient(int gradient) {
        this.gradient = gradient;
        update();
        return this;
    }


    /**
     * 设置渐变方向
     *
     * @param gradientOrientation
     */
    public ColorfulButton setGradientOrientation(int gradientOrientation) {
        this.gradientOrientation = gradientOrientation;
        update();
        return this;
    }


    /**
     * 设置渐变半径
     *
     * @param gradientRadius
     */
    public ColorfulButton setGradientRadius(float gradientRadius) {
        this.gradientRadius = gradientRadius;
        update();
        return this;
    }


    /**
     * 设置endColor
     *
     * @param endColor
     */
    public ColorfulButton setEndColor(int endColor) {
        this.endColor = endColor;
        update();
        return this;
    }

    /**
     * 设置centerColor
     *
     * @param centerColor
     */
    public ColorfulButton setCenterColor(int centerColor) {
        this.centerColor = centerColor;
        update();
        return this;
    }


    /**
     * 设置startColor
     *
     * @param startColor
     */
    public ColorfulButton setStartColor(int startColor) {
        this.startColor = startColor;
        update();
        return this;
    }


    /**
     * 设置渐变色数组
     * <p>
     * 优先级大于xml设置，仅支持java
     * 仅支持十六进制值（不支持R.color）
     * <p>
     * 如：0xFFFF9326, 0xFF48D1CC
     * Color.parseColor("#48D1CC")
     *
     * @param gradientColors
     */
    public ColorfulButton setGradientColors(int[] gradientColors) {
        this.gradientColors = gradientColors;
        update();
        return this;
    }


    /**
     * 强制锁定按压背景（只能在设置渐变色的时候用）
     * 主要原因是因为设置按钮按压背景的话跟会显得跟渐变色不和谐，而一般
     * 的设计也不会给按压再加渐变，所以暂时不支持按压渐变，如果需要使
     * 按压颜色和普通颜色不一致，调用此方法即可修改，默认锁定
     *
     * @param lockPressColor true 锁定（背景和按压一致）
     */
    public ColorfulButton setLockPressColor(boolean lockPressColor) {
        boolean condition = (startColor == NORMAL_COLOR && centerColor == NORMAL_COLOR && endColor == NORMAL_COLOR)
                && gradientColors == null;
        if (condition) {
            throw new IllegalArgumentException("shape solid without gradient isn't support lock press color");
        }
        isLockPressColor = lockPressColor;
        update();
        return this;
    }


    /**
     * 刷新当前UI
     */
    private void update() {
        //版本控制
        if (Build.VERSION.SDK_INT >= 21) {
            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{addStateDrawable(), initRipple()});
            setBackground(layerDrawable);
        } else if (Build.VERSION.SDK_INT >= 16) {
            setBackground(addStateDrawable());
        } else {
            setBackgroundDrawable(addStateDrawable());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private RippleDrawable initRipple() {

        //添加默认状态颜色为水波纹颜色，避免点击闪烁
        int[][] stateList = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{}
        };

        int[] stateColorList = new int[]{
                rippleColor,
                rippleColor
        };
        ColorStateList colorStateList = new ColorStateList(stateList, stateColorList);

        //控制水波纹边界
        float[] outRadius = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
        RoundRectShape roundRectShape = new RoundRectShape(outRadius, null, null);
        ShapeDrawable maskDrawable = new ShapeDrawable();
        maskDrawable.setShape(roundRectShape);

        RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, null, maskDrawable);
        return rippleDrawable;
    }


    //构建selector
    private StateListDrawable addStateDrawable() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable normal = copy(color, true);
        Drawable pressed = copy(pressColor, isLockPressColor);
        Drawable focus = copy(focusColor, isLockPressColor);

        stateListDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normal);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }


    /**
     * 解决对象引用copy引起的问题
     *
     * @param copyColor 涉及到的color
     * @param isLock    是否锁定按压颜色
     */
    private Drawable copy(int copyColor, boolean isLock) {

        GradientDrawable drawable;
        if (isLock) {
            drawable = new GradientDrawable(map(), gradientColors == null ? new int[]{startColor, centerColor, endColor} : gradientColors);
            boolean condition = (startColor == NORMAL_COLOR && centerColor == NORMAL_COLOR && endColor == NORMAL_COLOR)
                    && gradientColors == null;
            if (condition) {
                drawable.setColor(copyColor);
            }
        } else {
            drawable = new GradientDrawable();
            drawable.setColor(copyColor);
        }

        drawable.setShape(shape);
        drawable.setCornerRadii(cornerArray);
        drawable.setCornerRadius(cornerRadius);
        drawable.setStroke((int) strokeWidth, strokeColor, dashWidth, dashGap);
        drawable.setGradientType(gradient);
        drawable.setGradientRadius(gradientRadius);

        return drawable;
    }


    /**
     * 测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        final int normalWidth = 200;
        final int normalHeight = 100;
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(normalWidth, normalHeight);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(normalWidth, height);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, normalHeight);
        } else {
            setMeasuredDimension(width, height);
        }
    }


    /**
     * 渐变方位映射表
     */
    private GradientDrawable.Orientation map() {
        GradientDrawable.Orientation orientation = null;
        switch (gradientOrientation) {
            case 0:
                orientation = TOP_BOTTOM;
                break;
            case 1:
                orientation = TR_BL;
                break;
            case 2:
                orientation = RIGHT_LEFT;
                break;
            case 3:
                orientation = BR_TL;
                break;
            case 4:
                orientation = BOTTOM_TOP;
                break;
            case 5:
                orientation = BL_TR;
                break;
            case 6:
                orientation = LEFT_RIGHT;
                break;
            case 7:
                orientation = TL_BR;
                break;
            default:
                orientation = LEFT_RIGHT;
                break;
        }
        return orientation;
    }


    public int getFocusColor() {
        return focusColor;
    }

    public int getRippleColor() {
        return rippleColor;
    }

    public int getPressColor() {
        return pressColor;
    }

    public float getDashGap() {
        return dashGap;
    }

    public int getShape() {
        return shape;
    }

    public int getColor() {
        return color;
    }

    public float getDashWidth() {
        return dashWidth;
    }

    public float getStrokeColor() {
        return strokeColor;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public int getGradient() {
        return gradient;
    }


    public int getStartColor() {
        return startColor;
    }


    public int getCenterColor() {
        return centerColor;
    }


    public int getEndColor() {
        return endColor;
    }


    public float getGradientRadius() {
        return gradientRadius;
    }


    public int getGradientOrientation() {
        return gradientOrientation;
    }


    public int[] getGradientColors() {
        return gradientColors;
    }


    public float getStrokeWidth() {
        return strokeWidth;
    }


    public boolean isLockPressColor() {
        return isLockPressColor;
    }

}

