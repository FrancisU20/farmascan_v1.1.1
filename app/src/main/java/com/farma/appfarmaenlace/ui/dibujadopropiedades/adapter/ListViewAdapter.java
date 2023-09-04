package com.farma.appfarmaenlace.ui.dibujadopropiedades.adapter;

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
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.farma.appfarmaenlace.R;
import com.farma.appfarmaenlace.ui.dibujadopropiedades.model.ItemPropiedad;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<ItemPropiedad> items;

    public ListViewAdapter(Context context, List<ItemPropiedad> items) {
        this.context = context;
        this.items = items;
    }

    public List<ItemPropiedad> getItems(){
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getPrd_codigo();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        //if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_propiedad_dibujar_item, parent, false);
        //}
        ItemPropiedad item=(ItemPropiedad)getItem(position);

        TextView etiqueta=convertView.findViewById(R.id.etiqueta);
        TextInputLayout valorString=convertView.findViewById(R.id.valorString);
        Spinner valorCombobox=convertView.findViewById(R.id.valorCombobox);
        EditText valorDate=convertView.findViewById(R.id.valorDate);

        etiqueta.setText(item.getPrd_nombre());
        switch (getItemViewType(position)){
            case 1:
                valorString.setVisibility(View.VISIBLE);
                valorString.setHint(item.getPrd_nombre());
                valorString.getEditText().addTextChangedListener(new ListViewAdapter.ValorStringTextWatcher(position));
                valorString.getEditText().setText(item.getDato());
                break;
            case 2:
            case 7:
                valorString.setVisibility(View.VISIBLE);
                valorString.setHint(item.getPrd_nombre());
                valorString.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                valorString.getEditText().addTextChangedListener(new ListViewAdapter.ValorStringTextWatcher(position));
                valorString.getEditText().setText(item.getDato());
                break;
            case 4:
               etiqueta.setVisibility(View.VISIBLE);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker;
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                valorDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
               valorDate.setVisibility(View.VISIBLE);
               valorDate.setInputType(InputType.TYPE_NULL);
               valorDate.setOnFocusChangeListener((v, hasFocus) -> {
                    if(hasFocus){
                        picker.show();
                    }
                });
               valorDate.setOnClickListener(v -> {
                    picker.show();
                });
               valorDate.addTextChangedListener(new ListViewAdapter.ValorStringTextWatcher(position));
               valorDate.setText(item.getDato());
                break;
            case 5:
               etiqueta.setVisibility(View.VISIBLE);
                valorCombobox.setVisibility(View.VISIBLE);

                ArrayAdapter<String> listadapter=new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,item.getOpciones());
                listadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                valorCombobox.setAdapter(listadapter);
                valorCombobox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                        String opt=(String)parent.getItemAtPosition(position1);
                        items.get(position).setDato(opt);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                break;
        }

        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getTpd_tipo_dato();
    }

    private class ValorStringTextWatcher implements TextWatcher {
        private final int position;
        public ValorStringTextWatcher(int position){
            this.position=position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.i("posicion:",String.valueOf(position));
            items.get(position).setDato(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
