package com.farma.appfarmaenlace.ui.notificaciones.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.data.api.ApiResponseNotificacion;
import com.farma.appfarmaenlace.ui.dibujadopropiedades.adapter.ListaPropiedadesAdapter;

import java.util.List;

public class ListaNotificacionesAdapter extends RecyclerView.Adapter<ListaNotificacionesAdapter.ViewHolder> {

    private final List<ApiResponseNotificacion> lista;
    private OnClickListener onClick;

    public ListaNotificacionesAdapter(List<ApiResponseNotificacion> lista){
        this.lista=lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_notificacion_item, parent, false);
        final ListaNotificacionesAdapter.ViewHolder holder=new ListaNotificacionesAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ApiResponseNotificacion notificacion=lista.get(position);
        holder.txtFecha.setText(notificacion.not_fecha_registro.split("T")[0]);
        holder.txtMensaje.setText(notificacion.not_mensaje);
        holder.txtNumero.setText(String.valueOf(position+1));
        holder.linearLayout.setOnClickListener(v -> {
            onClick.click(lista.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtNumero;
        public TextView txtMensaje;
        public TextView txtFecha;
        public LinearLayout linearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtNumero=itemView.findViewById(R.id.txtNro);
            this.txtMensaje=itemView.findViewById(R.id.txtMensaje);
            this.txtFecha=itemView.findViewById(R.id.txtFecha);
            this.linearLayout=itemView.findViewById(R.id.linearlayout);
        }
    }

    public void setOnClickListener(OnClickListener onClick) {
        this.onClick = onClick;
    }

    public interface OnClickListener{
        void click(ApiResponseNotificacion notificacion);
    }
}
