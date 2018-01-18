package com.mouse.lion.pocketdj.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class ResUtils {

    private ResUtils() {}

    public static int getStatusBarHeight(Context context) {
        int height;
        Resources myResources = context.getResources();
        int idStatusBarHeight = myResources.getIdentifier(
                "status_bar_height", "dimen", "android");
        if (idStatusBarHeight > 0) {
            height = context.getResources().getDimensionPixelSize(idStatusBarHeight);
        }else{
            height = 0;
            Logger.w("resource was not found");
        }

        return height;
    }

    public static int getActionBarHeight(Context context) {
        TypedArray styledAttributes = context.getTheme()
                .obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }
}
