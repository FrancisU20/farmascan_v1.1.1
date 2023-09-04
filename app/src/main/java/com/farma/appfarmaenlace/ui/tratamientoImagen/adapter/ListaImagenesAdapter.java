package com.farma.appfarmaenlace.ui.tratamientoImagen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.farma.appfarmaenlace.R;

import java.util.List;

public class ListaImagenesAdapter extends RecyclerView.Adapter<ListaImagenesAdapter.ViewHolder> {
    List<String> imagenes;
    OnDeleteListener onDeleteListener;
    Context context;

    public ListaImagenesAdapter(List<String> imagenes) {
        this.imagenes = imagenes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_imagen_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide
                .with(holder.itemView)
                .load(imagenes.get(position))
                .into(holder.imagen);
        holder.getImageButton().setOnClickListener(v -> {
            onDeleteListener.onDelete(position);
        });
                /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        String imgb64 = imagenes.get(position);*/
        /*byte[] decString= Base64.decode(imgb64,Base64.DEFAULT);
        Bitmap decByte= BitmapFactory.decodeByteArray(decString,0,decString.length,options);*/
        /*final int THUMBSIZE = 128;
        Bitmap decByte = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imgb64),
                THUMBSIZE, THUMBSIZE);*/
        //Bitmap decByte=BitmapFactory.decodeFile(imgb64);
        //holder.getImagen().setImageBitmap(decByte);
    }

    @Override
    public int getItemCount() {
        return imagenes.size();
    }

    public void clear() {
        int size = imagenes.size();
        imagenes.clear();
        notifyItemRangeRemoved(0, size);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        imagenes.remove(position);
        notifyItemRangeRemoved(position, 1);
        notifyDataSetChanged();
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.onDeleteListener = listener;
    }

    public List<String> getImagenes() {
        return this.imagenes;
    }
    public String getItem(int position){
        return this.imagenes.get(position);
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagen;
        private final ImageButton imageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen);
            imageButton = itemView.findViewById(R.id.btnEliminar);
        }

        public ImageView getImagen() {
            return imagen;
        }

        public ImageButton getImageButton() {
            return imageButton;
        }
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }
}
