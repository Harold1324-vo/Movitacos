package com.example.movitaco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterAlimentos extends RecyclerView.Adapter<AdapterAlimentos.ViewHolder> {

    ArrayList<Productos> ListaAlimentos;

    public AdapterAlimentos(ArrayList<Productos> listaAlimentos){
        ListaAlimentos = listaAlimentos;
    }
    @NonNull
    @Override
    public AdapterAlimentos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAlimentos.ViewHolder holder, int position) {
        holder.txtId.setText(ListaAlimentos.get(position).getId().toString());
        holder.txtNombreAlimento.setText(ListaAlimentos.get(position).getNombreProducto());
        holder.txtTipoProducto.setText(ListaAlimentos.get(position).getTipoProducto().toString());
        holder.txtPrecioAlimento.setText(ListaAlimentos.get(position).getPrecioProducto().toString());
    }

    @Override
    public int getItemCount() {
        return ListaAlimentos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtNombreAlimento, txtTipoProducto, txtPrecioAlimento;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = (TextView) itemView.findViewById(R.id.txtId);
            txtNombreAlimento = (TextView) itemView.findViewById(R.id.txtNombreAlimento);
            txtTipoProducto = (TextView) itemView.findViewById(R.id.txtTipoProducto);
            txtPrecioAlimento = (TextView) itemView.findViewById(R.id.txtPrecioAlimento);
        }
    }
}
