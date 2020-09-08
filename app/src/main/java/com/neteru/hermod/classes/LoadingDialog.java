package com.neteru.hermod.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neteru.hermod.R;

@SuppressWarnings("unused")
public class LoadingDialog {
    private AlertDialog LoadingBox;
    private TextView msgText;

    @SuppressLint("InflateParams")
    public LoadingDialog(Context context){

        LoadingBox = new AlertDialog.Builder(context).create();

        LayoutInflater factory = LayoutInflater.from(context);
        View loadingView = factory.inflate(R.layout.loading_layout, null);

        ProgressBar progressBar = loadingView.findViewById(R.id.progress);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        msgText = loadingView.findViewById(R.id.msg);
        String msg = context.getResources().getString(R.string.loadingMsg);
        msgText.setText(msg);

        LoadingBox.setView(loadingView);
        LoadingBox.setCancelable(false);
    }

    public void setCancelable(){
        LoadingBox.setCancelable(true);
    }

    public boolean isShowing(){
        return LoadingBox.isShowing();
    }

    public void show(){
        this.LoadingBox.show();
    }

    public void dismiss(){
        this.LoadingBox.dismiss();
    }

    public void setMsg(String m){
        this.msgText.setText(m);
    }

}
