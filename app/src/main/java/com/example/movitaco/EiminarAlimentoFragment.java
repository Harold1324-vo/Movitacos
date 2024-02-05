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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EiminarAlimentoFragment extends Fragment {
    ImageButton btnBuscarId;
    TextView idTaqueria, etID, etNombreProducto, etTipoProducto, etPrecio;
    Button btnEliminar;
    ProgressDialog progressDialog;
    StringRequest stringRequest;
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    private PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_eiminar_alimento, container, false);
        idTaqueria = (EditText) vista.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        btnBuscarId = vista.findViewById(R.id.btnBuscarId);
        etID = vista.findViewById(R.id.etIdProducto);
        etNombreProducto = vista.findViewById(R.id.etNombreProducto);
        etTipoProducto = (TextView) vista.findViewById(R.id.etTipoProducto);
        etPrecio = vista.findViewById(R.id.etPrecioProducto);
        btnEliminar = vista.findViewById(R.id.btnEliminar);

        btnBuscarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etID.getText().toString();

                if(idProduto.isEmpty()){
                    etID.setError("Ingresa el ID");
                }else{
                    cargarWebService();
                }
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etID.getText().toString();
                if(idProduto.isEmpty()){
                    etID.setError("Ingresa el ID");
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
        String url="http://192.168.0.107/crudAlimentos/apiConsultarProductoID.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idProducto=" + etID.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                Productos miProducto=new Productos();

                JSONArray json=response.optJSONArray("producto");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    miProducto.setId(jsonObject.optInt("idProducto"));
                    miProducto.setNombreProducto(jsonObject.optString("nombreProducto"));
                    miProducto.setTipoProducto(jsonObject.optString("tipoProducto"));
                    miProducto.setPrecioProducto(jsonObject.optDouble("precioProducto"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                etNombreProducto.setText(miProducto.getNombreProducto());
                etTipoProducto.setText(miProducto.getTipoProducto());
                etPrecio.setText(miProducto.getPrecioProducto().toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Producto No Existe", Toast.LENGTH_LONG).show();
                System.out.println();
                progressDialog.hide();
                Log.d("ERROR: ", error.toString());
                etNombreProducto.setText("");
                etTipoProducto.setText("");
                etPrecio.setText("");
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
        String url="http://192.168.0.107/crudAlimentos/apiEliminarProducto.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idProducto="+etID.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("Eliminado")){
                    etID.setText("");
                    etNombreProducto.setText("");
                    etPrecio.setText("");
                    Toast.makeText(getContext(),"Producto Eliminado",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("No eliminado")){
                        Toast.makeText(getContext(),"Producto No Eliminado",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getContext(),"Producto No Eliminado",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Producto No Eliminado",Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }
}