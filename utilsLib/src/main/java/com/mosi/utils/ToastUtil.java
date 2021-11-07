package com.mosi.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosi.uikit.toast.ToastUtils;
import com.mosi.update.R;


/**
 * @content: Author: gjt66888
 * Description:
 * Time: 2019/12/19
 */
public class ToastUtil {
    public static final int LEVEL_W = 0;
    public static final int LEVEL_E = 1;
    public static final int LEVEL_S = 2;

    public static void showToast(String message) {
        ToastUtils.setView(R.layout.one_layout_toast);

        TextView tvTip = ToastUtils.getView().findViewById(R.id.one_tv_tip);
        tvTip.setText(message);
        ToastUtils.show(message);
    }

    public static void showToast(int resid) {
        showToast(LEVEL_W, resid);
    }

    public static void showToast(int level, String message) {
        ToastUtils.setView(R.layout.third_layout_toast);
        ImageView ivIcon = ToastUtils.getView().findViewById(R.id.iv_icon);
        ivIcon.setVisibility(View.VISIBLE);
        switch (level) {
            case LEVEL_W:
                ivIcon.setImageResource(R.drawable.ic_common_warnning);
                break;
            case LEVEL_E:
                ivIcon.setImageResource(R.drawable.ic_third_update_close);
                break;
            case LEVEL_S:
                ivIcon.setImageResource(R.drawable.ic_common_succ);
                break;
        }
        TextView tvTip = ToastUtils.getView().findViewById(R.id.tv_tip);
        tvTip.setText(message);
        ToastUtils.show(message);
    }

    public static void showToast(int level, int resid) {
        ImageView ivIcon = ToastUtils.getView().findViewById(R.id.iv_icon);
        ivIcon.setVisibility(View.VISIBLE);
        switch (level) {
            case LEVEL_W:
                ivIcon.setImageResource(R.drawable.ic_common_warnning);
                break;
            case LEVEL_E:
                ivIcon.setImageResource(R.drawable.ic_third_update_close);
                break;
            case LEVEL_S:
                ivIcon.setImageResource(R.drawable.ic_common_succ);
                break;
        }
        TextView tvTip = ToastUtils.getView().findViewById(R.id.tv_tip);
        tvTip.setText(resid);
        ToastUtils.show(resid);
    }
}