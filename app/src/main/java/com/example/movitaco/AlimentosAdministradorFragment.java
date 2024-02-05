package com.example.movitaco;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlimentosAdministradorFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    ArrayList<Productos> listaAlimentos;
    RecyclerView recyclerView;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private PreferenceHelper preferenceHelper;
    TextView txtReporteProducto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alimentos_administrador, container, false);
        listaAlimentos = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.listaMostrar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        request = Volley.newRequestQueue(getContext());

        txtReporteProducto = view.findViewById(R.id.txtPruebaProductos);
        preferenceHelper = new PreferenceHelper(getContext());
        txtReporteProducto.setText(preferenceHelper.getIdTaqueria());

        cargarAPI();
        return view;
    }

    private void cargarAPI() {
        progress = new ProgressDialog(getContext());
        progress.setMessage("Consultando.");
        progress.show();
        //192.168.137.127
        //192.168.1.73
        String idTaqueria = txtReporteProducto.getText().toString();
        String url = "http://192.168.0.107/crudAlimentos/apiMostrarProductos.php?idTaqueria="+idTaqueria;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Agrega Alimentos", Toast.LENGTH_LONG).show();
        System.out.println();
        Log.d("Error: ", error.toString());
        progress.hide();
    }

    @Override
    public void onResponse(JSONObject response) {
        Productos productos = null;
        JSONArray json = response.optJSONArray("producto");
        try {
            for (int i=0;i<json.length();i++){
                productos = new Productos();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                productos.setId(jsonObject.optInt("idProducto"));
                productos.setTipoProducto(jsonObject.optString("tipoProducto"));
                productos.setNombreProducto(jsonObject.optString("nombreProducto" ));
                productos.setPrecioProducto(jsonObject.optDouble("precioProducto"));
                listaAlimentos.add(productos);
            }
            progress.hide();
            AdapterAlimentos adapter = new AdapterAlimentos(listaAlimentos);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Ingresa Alimentos", Toast.LENGTH_SHORT).show();
            progress.hide();
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregarComida);
        FloatingActionButton btnModificar = view.findViewById(R.id.btnModificarAlimento);
        FloatingActionButton btnEliminar = view.findViewById(R.id.btnEliminarAlimento);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_alimentosAdministradorFragment_to_agregarAlimentosFragment);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_alimentosAdministradorFragment_to_modificarAlimentoFragment);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_alimentosAdministradorFragment_to_eiminarAlimentoFragment);
            }
        });
    }

}