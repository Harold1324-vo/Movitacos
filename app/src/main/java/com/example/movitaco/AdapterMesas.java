package com.example.movitaco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movitaco.Mesas;
import com.example.movitaco.R;
import java.util.ArrayList;

public class AdapterMesas extends RecyclerView.Adapter<AdapterMesas.ViewHolder>{
    ArrayList<Mesas> ListaMesas;

    public AdapterMesas(ArrayList<Mesas> listaMesas){
        ListaMesas = listaMesas;
    }
    @NonNull
    @Override
    public AdapterMesas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mesas,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new AdapterMesas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMesas.ViewHolder holder, int position) {
        holder.IdMesa.setText(ListaMesas.get(position).getIdMesa().toString());
        holder.nombreMesa.setText(ListaMesas.get(position).getNombreMesa());
    }

    @Override
    public int getItemCount() {
        return ListaMesas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView IdMesa, nombreMesa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            IdMesa = (TextView) itemView.findViewById(R.id.idMesa);
            nombreMesa = (TextView) itemView.findViewById(R.id.nombreMesa);
        }
    }
}
