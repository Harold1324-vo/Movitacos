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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movitaco.AdapterAlimentos;
import com.example.movitaco.AdapterMesas;
import com.example.movitaco.Mesas;
import com.example.movitaco.Productos;
import com.example.movitaco.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MesasAdministradorFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    ArrayList<Mesas> listaMesas;
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
        View view = inflater.inflate(R.layout.fragment_mesas_administrador, container, false);
        listaMesas = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.listaMostrarMesas);
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
        progress.setMessage("Consultando...");
        progress.show();
        //192.168.137.127
        //192.168.1.73

        String idTaqueria = txtReporteProducto.getText().toString();
        String url = "http://192.168.0.107/crudMesas/apiMostrarMesas.php?idTaqueria="+idTaqueria;

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(), "Agrega Mesas", Toast.LENGTH_SHORT).show();
        System.out.println();
        Log.d("Error: ", error.toString());
        progress.hide();
    }
///////////////////////////
    @Override
    public void onResponse(JSONObject response) {
        Mesas mesas = null;
        JSONArray json = response.optJSONArray("mesas");
        try {
            for (int i=0;i<json.length();i++){
                mesas = new Mesas();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                mesas.setIdMesa(jsonObject.optInt("idMesa"));
                mesas.setNombreMesa(jsonObject.optString("nombreMesa" ));
                listaMesas.add(mesas);
            }
            progress.hide();
            AdapterMesas adapter = new AdapterMesas(listaMesas);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getContext(), "Ingresa Mesas", Toast.LENGTH_SHORT).show();
            progress.hide();
        }
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        FloatingActionButton btnAgregar = view.findViewById(R.id.btnAgregarMesa);
        FloatingActionButton btnModificar = view.findViewById(R.id.btnModificarMesa);
        FloatingActionButton btnEliminar = view.findViewById(R.id.btnEliminarMesa);


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_mesasAdministradorFragment_to_agregarMesasFragment);
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mesasAdministradorFragment_to_modificarMesasFragment);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_mesasAdministradorFragment_to_eliminarMesasFragment);
            }
        });
    }

}