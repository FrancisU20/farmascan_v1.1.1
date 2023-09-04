package com.farma.appfarmaenlace.ui.dibujadopropiedades.adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.ui.dibujadopropiedades.model.ItemPropiedad;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListaPropiedadesAdapter extends RecyclerView.Adapter<ListaPropiedadesAdapter.ViewHolder> {
   List<ItemPropiedad> list=new ArrayList<>();
   Context context;
    public ListaPropiedadesAdapter(List<ItemPropiedad>list,Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_propiedad_dibujar_item, parent, false);
        final ViewHolder holder=new ViewHolder(view, new ValorStringTextWatcher());
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final ItemPropiedad item=list.get(holder.getBindingAdapterPosition());

        holder.valorStringTextWatcher.updatePosition(holder.getBindingAdapterPosition());

        holder.getEtiqueta().setText(item.getPrd_nombre());

        holder.getValorString().setHint(item.getPrd_nombre());
        holder.getValorString().getEditText().setText(item.getDato());

        switch (getItemViewType(position)){
            case 1:
                holder.getValorString().setVisibility(View.VISIBLE);
                break;
            case 2:
            case 7:
                holder.getValorString().setVisibility(View.VISIBLE);
                holder.getValorString().getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 4:
                holder.getEtiqueta().setVisibility(View.VISIBLE);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker;
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String fecha=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                holder.getValorDate().setText(fecha);
                                item.setDato(fecha);
                            }
                        }, year, month, day);
                holder.getValorDate().setVisibility(View.VISIBLE);
                holder.getValorDate().setInputType(InputType.TYPE_NULL);
                holder.getValorDate().setOnFocusChangeListener((v, hasFocus) -> {
                    if(hasFocus){
                        picker.show();
                    }
                });
                holder.getValorDate().setOnClickListener(v -> {
                    picker.show();
                });
                holder.getValorStringE().setText(item.getDato());
                break;
            case 5:
                holder.getEtiqueta().setVisibility(View.VISIBLE);
                holder.getValorCombobox().setVisibility(View.VISIBLE);

                ArrayAdapter<String> listadapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,item.getOpciones());
                listadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.getValorCombobox().setAdapter(listadapter);
                holder.getValorCombobox().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                        String opt=(String)parent.getItemAtPosition(position1);
                        list.get(position).setDato(opt);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
       return list.get(position).getTpd_tipo_dato();
    }


    public List<ItemPropiedad> getList(){
        return list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView etiqueta;
        private final TextInputLayout valorString;
        private final TextInputEditText valorStringE;
        private final Spinner valorCombobox;
        private final EditText valorDate;
        private final ValorStringTextWatcher valorStringTextWatcher;
        public ViewHolder(@NonNull View itemView,  ValorStringTextWatcher valorStringTextWatcher) {
            super(itemView);
            this.valorStringTextWatcher=valorStringTextWatcher;
            etiqueta=itemView.findViewById(R.id.etiqueta);
            valorString=itemView.findViewById(R.id.valorString);
            valorCombobox=itemView.findViewById(R.id.valorCombobox);
            valorDate=itemView.findViewById(R.id.valorDate);
            valorStringE=itemView.findViewById(R.id.valorStringE);

            valorStringE.addTextChangedListener(valorStringTextWatcher);
        }

        public ValorStringTextWatcher getValorStringTextWatcher() {
            return valorStringTextWatcher;
        }

        public TextView getEtiqueta() {
            return etiqueta;
        }

        public TextInputLayout getValorString() {
            return valorString;
        }

        public Spinner getValorCombobox() {
            return valorCombobox;
        }

        public EditText getValorDate() {
            return valorDate;
        }

        public TextInputEditText getValorStringE() {
            return valorStringE;
        }
    }

    private class ValorStringTextWatcher implements TextWatcher {
        private int position;
        public ValorStringTextWatcher(){
        }

        public void updatePosition(int position) {
            this.position=position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("posicion:",String.valueOf(position));
            list.get(position).setDato(s.toString());
        }
    }

}
