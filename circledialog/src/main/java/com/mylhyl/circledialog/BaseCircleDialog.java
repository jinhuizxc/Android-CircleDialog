package com.mylhyl.circledialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.mylhyl.circledialog.scale.ScaleUtils;
import com.mylhyl.circledialog.res.drawable.CircleDrawable;
import com.mylhyl.circledialog.res.values.CircleColor;
import com.mylhyl.circledialog.res.values.CircleDimen;

/**
 * Created by hupei on 2017/3/29.
 */

public abstract class BaseCircleDialog extends DialogFragment {

    public abstract View createView(Context context, LayoutInflater inflater, ViewGroup container);

    private int mGravity = Gravity.CENTER;//对话框的位置
    private boolean mCanceledOnTouchOutside = true;//是否触摸外部关闭
    private float mWidth = 0.9f;//对话框宽度，范围：0-1；1整屏宽
    private int[] mPadding;//对话框与屏幕边缘距离
    private int mAnimStyle;//显示动画
    private boolean isDimEnabled = true;
    private int mBackgroundColor = CircleColor.bgDialog;//对话框的背景色
    private int mRadius = CircleDimen.radius;//对话框的圆角半径
    private float mAlpha = 1f;//对话框透明度，范围：0-1；1不透明
    private int mX, mY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置 无标题 无边框
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(getContext(), inflater, container);
        view.setBackground(new CircleDrawable(mBackgroundColor, mRadius));
        view.setAlpha(mAlpha);
        return view;
    }


    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            setDialogGravity(dialog);//设置对话框布局
        }
        super.onStart();
    }

    /**
     * 设置对话框底部显示
     *
     * @param dialog
     */
    private void setDialogGravity(Dialog dialog) {
        // 设置宽度为屏宽、靠近屏幕底部。
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wlp = window.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);//获取屏幕宽
        wlp.width = (int) (dm.widthPixels * mWidth);//宽度按屏幕大小的百分比设置
        wlp.gravity = mGravity;
        wlp.x = mX;
        wlp.y = mY;
        //边距
        if (mPadding != null) {
            int[] padding = mPadding;
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.getDecorView().setPadding(ScaleUtils.scaleValue(padding[0]), ScaleUtils
                    .scaleValue(padding[1]), ScaleUtils.scaleValue(padding[2]), ScaleUtils
                    .scaleValue(padding[3]));
        }
        //动画
        if (mAnimStyle != 0) {
            window.setWindowAnimations(mAnimStyle);
        }
        if (isDimEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        window.setAttributes(wlp);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.add(this, tag);
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * 设置对话框位置
     * {@link Gravity#CENTER 默认}
     *
     * @param gravity
     */
    protected void setGravity(int gravity) {
        mGravity = gravity;
    }

    /**
     * 设置对话框点击外部关闭
     *
     * @param cancel
     * @return
     */
    protected void setCanceledOnTouchOutside(boolean cancel) {
        mCanceledOnTouchOutside = cancel;
    }

    /**
     * 设置对话框宽度
     *
     * @param width 0.0 - 1.0
     * @return
     */
    protected void setWidth(@FloatRange(from = 0.0, to = 1.0) float width) {
        mWidth = width;
    }

    /**
     * 设置边距
     *
     * @param left   px
     * @param top    px
     * @param right  px
     * @param bottom px
     * @return
     */
    protected void setPadding(int left, int top, int right, int bottom) {
        mPadding = new int[]{left, top, right, bottom};
    }

    /**
     * 动画弹出对话框,style动画资源
     *
     * @param animStyle
     * @return
     */
    protected void setAnimations(int animStyle) {
        mAnimStyle = animStyle;
    }


    /**
     * 设置背景是否昏暗，默认true
     *
     * @param dimEnabled
     * @return
     */
    protected void setDimEnabled(boolean dimEnabled) {
        isDimEnabled = dimEnabled;
    }

    /**
     * 设置对话框背景色
     *
     * @param color
     * @return
     */
    protected void setBackgroundColor(@ColorInt int color) {
        mBackgroundColor = color;
    }

    /**
     * 设置对话框圆角
     *
     * @param radius
     * @return
     */
    protected void setRadius(int radius) {
        mRadius = radius;
    }

    /**
     * 设置对话框透明度
     *
     * @param alpha 0.0 - 1.0
     * @return
     */
    protected void setAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        mAlpha = alpha;
    }

    protected void setX(int x) {
        mX = x;
    }

    protected void setY(int y) {
        mY = y;
    }
}
