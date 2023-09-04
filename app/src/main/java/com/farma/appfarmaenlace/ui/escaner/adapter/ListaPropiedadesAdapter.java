package com.farma.appfarmaenlace.ui.escaner.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.ui.escaner.model.ItemPropiedad;

import java.util.ArrayList;
import java.util.List;

public class ListaPropiedadesAdapter extends RecyclerView.Adapter<ListaPropiedadesAdapter.ViewHolder> {
   List<ItemPropiedad> list=new ArrayList<>();
    public ListaPropiedadesAdapter(List<ItemPropiedad>list){
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_propiedad_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String etiqueta=list.get(position).getEtiqueta();
        String valor=list.get(position).getValor();
        holder.getEtiqueta().setText(etiqueta);
        holder.getValor().setText(valor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView etiqueta;
        private final TextView valor;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etiqueta=itemView.findViewById(R.id.etiqueta);
            valor=itemView.findViewById(R.id.valor);
        }

        public TextView getEtiqueta() {
            return etiqueta;
        }

        public TextView getValor() {
            return valor;
        }
    }
}
