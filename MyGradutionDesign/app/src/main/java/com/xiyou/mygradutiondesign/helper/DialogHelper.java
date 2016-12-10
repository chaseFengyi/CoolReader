package com.xiyou.mygradutiondesign.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.xiyou.mygradutiondesign.R;
import com.xiyou.mygradutiondesign.util.ResUtil;

/**
 * Created by fengyi on 16/3/13.
 */
public class DialogHelper {

    public interface ConfirmCallback {

        String getPositiveHint();

        void onPositive(DialogInterface dialog);

        String getItemPositive();

        void onItemPositive(DialogInterface dialog, AdapterView<?> parent, View view, int position,
                long id);

        void onNegative(DialogInterface dialog);

        String getNegativeHint();

        void onItemLongPositive(DialogInterface dialog, AdapterView<?> parent, View view,
                int position, long id);

        String getItemLongHint();
    }

    public static class BaseConfirmCallback implements ConfirmCallback {

        @Override
        public String getPositiveHint() {
            return ResUtil.getString(R.string.ok);
        }

        @Override
        public void onPositive(DialogInterface dialog) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        @Override
        public String getItemPositive() {
            return ResUtil.getString(R.string.ok);
        }

        @Override
        public void onItemPositive(DialogInterface dialog, AdapterView<?> parent, View view,
                int position, long id) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        @Override
        public void onNegative(DialogInterface dialog) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        @Override
        public String getNegativeHint() {
            return ResUtil.getString(R.string.cancel);
        }

        @Override
        public void onItemLongPositive(DialogInterface dialog, AdapterView<?> parent, View view,
                int position, long id) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }

        @Override
        public String getItemLongHint() {
            return ResUtil.getString(R.string.ok);
        }
    }

    public static Dialog showListItemDeleteDialod(Activity activity, View remarkView,
            int messageId,
            ConfirmCallback callback) {
        return showListItemDeleteDialod(activity, remarkView, ResUtil.getString(messageId),
                callback);
    }

    public static Dialog showListItemDeleteDialod(Activity activity, View remarkView,
            String messageTitle,
            ConfirmCallback callback) {
        return showListItemDeleteDialod(activity, remarkView, messageTitle, callback,
                android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static Dialog showListItemDeleteDialod(Activity activity, View remarkView,
            String messageTitle,
            final ConfirmCallback callback, int style) {
        final Dialog dialog = new Dialog(activity, style);

        View view = remarkView;
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ViewAccessor
                .create(view)
                .setText(R.id.tv_dialog_delete_message, messageTitle)
                .setOnClickListener(R.id.tv_dialog_delete_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onPositive(dialog);
                    }
                })
                .setOnClickListener(R.id.tv_dialog_delete_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onNegative(dialog);
                    }
                });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    public static Dialog showBelongTypesDialog(Activity activity, View remarkView, int messageId,
            ConfirmCallback callback) {
        return showBelongTypesDialog(activity, remarkView, ResUtil.getString(messageId),
                callback);
    }

    public static Dialog showBelongTypesDialog(Activity activity, View remarkView,
            String messageTitle, ConfirmCallback callback) {
        return showBelongTypesDialog(activity, remarkView, messageTitle, callback,
                android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static Dialog showBelongTypesDialog(Activity activity, View remarkView,
            String messageTitle, final ConfirmCallback callback, int style) {
        final Dialog dialog = new Dialog(activity, style);

        View view = remarkView;
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        ViewAccessor
                .create(view)
                .setText(R.id.tv_dialog_choice_belong, messageTitle)
                .setOnItemClickListener(R.id.list_dialog_belong_type,
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                                callback.onItemPositive(dialog, parent, view, position, id);
                            }
                        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;

    }

    private static Dialog progressDialog;

    public static void showProgressDialog(Context context, int message) {
        showProgressDialog(context, ResUtil.getString(message));
    }

    public static void showProgressDialog(Context context, String message) {
        showProgressDialog(context, message, android.R.style.Theme_Translucent_NoTitleBar);
    }

    /**
     * 显示一个正在加载的对话框，当cancel时，默认行为是推出当前Activity
     *
     * @param context
     * @param message
     * @param style
     * @return
     */
    public static Dialog showProgressDialog(final Context context, String message, int style) {
        if (context == null) {
            return null;
        }

        progressDialog = new Dialog(context, style);
        View view = LayoutInflater.from(context).inflate(R.layout.view_loading_dialog, null);
        FrameLayout layout = (FrameLayout) view.findViewById(R.id.dialog_container);
        ViewHelper.showView(layout);
        progressDialog.setContentView(view);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        if (!TextUtils.isEmpty(message)) {
            ViewHelper.setTextView(view, R.id.tv_dialog_msg, message);
        }
        progressDialog.setContentView(view);
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (context instanceof Activity) {
                    progressDialog.dismiss();
                    progressDialog = null;
                    ((Activity) context).finish();
                }
            }
        });
        return progressDialog;
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.hide();
            progressDialog = null;
        }
    }

}
