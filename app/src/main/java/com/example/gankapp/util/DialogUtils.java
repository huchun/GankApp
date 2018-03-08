package com.example.gankapp.util;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by chunchun.hu on 2018/3/8.
 */

public class DialogUtils {


    public static MaterialDialog showMyDialog(Context context, String title, String content, String positiveBtnText, String negativeBtnText, OnDialogClickListener listener) {

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .positiveText(positiveBtnText)
                .negativeText(negativeBtnText)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
        dialog.setCancelable(false);
        return dialog;
    }

    public interface OnDialogClickListener{
        void onConfirm();
        void onCancel();
    }
}
