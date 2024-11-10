package com.dealxtra.dealxtra;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarManager {
    private static ProgressBarManager instance;
    private Dialog progressDialog;
    private Context context;

    private ProgressBarManager(Context context) {
        this.context = context;
    }

    public static synchronized ProgressBarManager getInstance(Context context) {
        if (instance == null) {
            instance = new ProgressBarManager(context);
        }
        return instance;
    }

    public void show(String message) {
        if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
            return; // Avoid showing dialog if activity is not in a valid state
        }

        if (progressDialog == null) {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.custom_progress_dialog);
            if (progressDialog.getWindow() != null) {
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            progressDialog.setCancelable(false);
        }

        TextView messageTextView = progressDialog.findViewById(R.id.messageTextView);
        if (messageTextView != null) {
            messageTextView.setText(message);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null; // Release the dialog instance to prevent memory leaks
        }
    }
}