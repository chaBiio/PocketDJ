package com.mouse.lion.pocketdj.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.FontRes;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mouse.lion.pocketdj.R;

/**
 * Created by lionm on 1/17/2018.
 */

public class ViewUtils {

    private ViewUtils() {
    }

    public static void setPaddingLeft(View view, @Px int px) {
        view.setPadding(px, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingTop(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft(), px, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setPaddingRight(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), px, view.getPaddingBottom());
    }

    public static void setPaddingBottom(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), px);
    }

    public static void addPaddingLeft(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft() + px, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void addPaddingTop(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + px, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void addPaddingRight(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight() + px, view.getPaddingBottom());
    }

    public static void addPaddingBottom(View view, @Px int px) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom() + px);
    }

    @Nullable
    public static TextView findTextViewInToolBar(Toolbar toolbar) {
        // this is gross code...
        View view = toolbar.getChildAt(0);
        return (view != null && view instanceof TextView)
                ? (TextView) view : null;
    }

    public static void setToolBarTitleFont(Context context, Toolbar toolbar, @FontRes int font) {
        TextView title = ViewUtils.findTextViewInToolBar(toolbar);
        if (title !=  null) {
            Typeface typeface = ResourcesCompat.getFont(context, font);
            title.setTypeface(typeface);
        }
    }
}
