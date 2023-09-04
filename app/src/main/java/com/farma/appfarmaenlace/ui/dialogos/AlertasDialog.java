package com.farma.appfarmaenlace.ui.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.farma.appfarmaenlace.R;

public class AlertasDialog extends DialogFragment {
    private String mensaje;
    private DialogInterface.OnClickListener aceptar;
    public AlertasDialog(){

    }

    public static AlertasDialog newInstance(String titulo, String mensaje) {
        AlertasDialog frag = new AlertasDialog();
        Bundle args = new Bundle();
        args.putString("titulo", titulo);
        args.putString("mensaje",mensaje);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alertas_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        view.setMinimumHeight((int) (displayRectangle.width() * 0.3f));
        view.setMinimumWidth((int) (displayRectangle.height() * 0.9f));

        TextView textViewt= view.findViewById(R.id.alert_titulo);
        textViewt.setText(getArguments().getString("titulo"));
        TextView textView= view.findViewById(R.id.alert_mensaje);
        textView.setText(getArguments().getString("mensaje"));

        Button button=view.findViewById(R.id.alert_btn_ok);
        button.setOnClickListener(v -> {
            getDialog().dismiss();
        });
    }
}
