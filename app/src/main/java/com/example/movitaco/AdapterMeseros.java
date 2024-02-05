package com.example.movitaco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movitaco.Meseros;
import com.example.movitaco.R;

import java.util.ArrayList;

public class AdapterMeseros extends RecyclerView.Adapter<AdapterMeseros.ViewHolder>{
    ArrayList<Meseros> ListaMeseros;

    public AdapterMeseros(ArrayList<Meseros> listaMeseros){
        ListaMeseros = listaMeseros;
    }
    @NonNull
    @Override
    public AdapterMeseros.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_meseros,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new AdapterMeseros.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMeseros.ViewHolder holder, int position) {
        holder.txtId.setText(ListaMeseros.get(position).getIdMesero().toString());
        holder.txtUsuarioMesero.setText(ListaMeseros.get(position).getNombreUsuario());
        holder.txtTelefonoMesero.setText(ListaMeseros.get(position).getTelefonoMesero().toString());
    }

    @Override
    public int getItemCount() {
        return ListaMeseros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtUsuarioMesero, txtTelefonoMesero;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtUsuarioMesero = (TextView) itemView.findViewById(R.id.txtUsuarioMesero);
            txtTelefonoMesero = (TextView) itemView.findViewById(R.id.txtTelefonoMesero);
        }
    }
}
