package com.farma.appfarmaenlace.ui.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class EnvioDialogo extends DialogFragment {
    private String mensaje;
    private DialogInterface.OnClickListener siDI;
    private DialogInterface.OnClickListener noDI;

    public EnvioDialogo(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mensaje)
                .setPositiveButton("SI", siDI)
                .setNegativeButton("NO", noDI);
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setSiDI(DialogInterface.OnClickListener siDI) {
        this.siDI = siDI;
    }

    public void setNoDI(DialogInterface.OnClickListener noDI) {
        this.noDI = noDI;
    }
}
