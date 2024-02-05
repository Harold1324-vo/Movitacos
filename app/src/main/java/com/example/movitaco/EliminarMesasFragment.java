package com.example.movitaco;
import android.app.ProgressDialog;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.movitaco.Mesas;
import com.example.movitaco.Meseros;
import com.example.movitaco.VolleySingleton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EliminarMesasFragment extends Fragment {
    ImageButton btnBuscarId;
    EditText idTaqueria, etIdMesa;
    TextView etNombreMesa;
    Button btnEliminar;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    private PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_eliminar_mesas, container, false);
        idTaqueria = (EditText) vista.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());
        btnBuscarId = vista.findViewById(R.id.btnBuscarId);
        etIdMesa = vista.findViewById(R.id.IdMesa);
        etNombreMesa = vista.findViewById(R.id.nombreMesa);
        btnEliminar = vista.findViewById(R.id.btnEliminarMesa);

        btnBuscarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etIdMesa.getText().toString();

                if(idProduto.isEmpty()){
                    etIdMesa.setError("Ingresa el ID");
                }else{
                    cargarWebService();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etIdMesa.getText().toString();
                if(idProduto.isEmpty()){
                    etIdMesa.setError("Ingresa el ID");
                }else{
                    webServiceEliminar();
                }
            }
        });
        return vista;
    }
    private void cargarWebService() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //192.168.137.127
        //192.168.1.73
        String url="http://192.168.0.107/crudMesas/apiConsultarMesaID.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idMesa=" + etIdMesa.getText().toString();
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();
                Mesas mesa =new Mesas();
                JSONArray json=response.optJSONArray("mesas");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    mesa.setIdMesa(jsonObject.optInt("idMesa"));
                    mesa.setNombreMesa(jsonObject.optString("nombreMesa"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                etNombreMesa.setText(mesa.getNombreMesa());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Mesa No Encontrada", Toast.LENGTH_LONG).show();
                System.out.println();
                progressDialog.hide();
                etNombreMesa.setText("");
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }
    private void webServiceEliminar() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        //192.168.137.127
        //192.168.1.73
        String url="http://192.168.0.107/crudMesas/apiEliminarMesa.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idMesa=" + etIdMesa.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("Eliminado")){
                    etIdMesa.setText("");
                    etNombreMesa.setText("");
                    Toast.makeText(getContext(),"Mesa Eliminada",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("No eliminado")){
                        Toast.makeText(getContext(),"Mesa No Eliminada",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getContext(),"Mesa No Eliminada",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Mesa No Eliminada",Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }
}