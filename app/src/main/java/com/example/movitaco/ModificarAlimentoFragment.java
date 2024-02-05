package com.example.movitaco;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ModificarAlimentoFragment extends Fragment  {

    ImageButton btnBuscarId;
    TextView idTaqueria, etID, etNombreProducto, etTipoProducto, etPrecio;
    Button btnActualizar;
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
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_modificar_alimento, container, false);
    }

    public void onViewCreated(@NonNull View vista, @Nullable Bundle savedInstanceState){
        super.onViewCreated(vista, savedInstanceState);
        idTaqueria = (EditText) vista.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        btnBuscarId = (ImageButton) vista.findViewById(R.id.btnBuscarId);
        etID = (EditText)vista.findViewById(R.id.etIdProducto);
        etNombreProducto = (EditText) vista.findViewById(R.id.etNombreProducto);
        etTipoProducto = (EditText) vista.findViewById(R.id.etTipoProducto);
        etPrecio = (EditText) vista.findViewById(R.id.etPrecioProducto);
        btnActualizar =(Button) vista.findViewById(R.id.btnActualizar);

        btnBuscarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etID.getText().toString();

                if(idProduto.isEmpty()){
                    etID.setError("Ingresa el ID");
                }else{
                    cargarWebService();
                    etNombreProducto.setError(null);
                    etTipoProducto.setError(null);
                    etPrecio.setText(null);
                }
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etID.getText().toString();
                String nombreProducto = etNombreProducto.getText().toString();
                String tipoProducto = etTipoProducto.getText().toString();
                String precioProducto = etPrecio.getText().toString();
                if(idProduto.isEmpty()){
                    etID.setError("Ingresa el ID");
                }
                if(nombreProducto.isEmpty()){
                    etNombreProducto.setError("Ingresa un nombre");
                }else if(nombreProducto.length() < 5){
                    etNombreProducto.setError("El nombre es muy corto");
                }else if(validarNombre(nombreProducto) == false){
                    etNombreProducto.setError("Ingresa solo letras o números");
                }
                if(tipoProducto.isEmpty()){
                    etTipoProducto.setError("Ingresa un tipo");
                }else if(tipoProducto.length() < 5){
                    etTipoProducto.setError("El tipo es muy corto");
                }else if(validarNombre(nombreProducto) == false){
                    etTipoProducto.setError("Ingresa solo letras o números");
                }
                if(precioProducto.isEmpty()){
                    etPrecio.setError("Ingresa el precio");
                }
                if((validarNombre(nombreProducto) == true && nombreProducto.length() >= 5)
                        && (tipoProducto.length() >= 5) && (!precioProducto.isEmpty())){
                    webServiceActualizar();
                }
            }
        });
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
                Toast.makeText(getContext(), "Este Producto No Existe", Toast.LENGTH_LONG).show();
                System.out.println();
                progressDialog.hide();
                etNombreProducto.setText("");
                etTipoProducto.setText("");
                etPrecio.setText("");
            }
        });

        // request.add(jsonObjectRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(jsonObjectRequest);
    }


    private void webServiceActualizar() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        //192.168.137.127
        //192.168.1.73
        String url = "http://192.168.0.107/crudAlimentos/apiActualizarProducto.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idProducto=" + etID.getText().toString();

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getContext(), "Producto Actualizado", Toast.LENGTH_SHORT).show();
                    etNombreProducto.setText("");
                    etTipoProducto.setText("");
                    etPrecio.setText("");
                } else {
                    Toast.makeText(getContext(), "Producto No Actualizado", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta: ", "" + response);
                    etNombreProducto.setText("");
                    etTipoProducto.setText("");
                    etPrecio.setText("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Producto No Actualizado", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
                etNombreProducto.setText("");
                etTipoProducto.setText("");
                etPrecio.setText("");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                String idProducto = etID.getText().toString();
                String nombreProducto = etNombreProducto.getText().toString();
                String tipoProducto = etTipoProducto.getText().toString();
                String precioProducto = etPrecio.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("idProducto", idProducto);
                parametros.put("nombreProducto", nombreProducto);
                parametros.put("tipoProducto", tipoProducto);
                parametros.put("precioProducto", precioProducto);

                return parametros;
            }

        };
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }

    boolean validarNombre(String nombre){
        for(int x = 0;x < nombre.length();x++){
            char c = nombre.charAt(x);
            if(!(nombre.length() > 25
                    || (c >= 'a' && c<= 'z') || (c >= 'A' && c<= 'Z') || c == ' ' || c == 'ñ'
                    || c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú'
                    || c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó'  || c == 'Ú'
                    || c == ',' || c == '.' || c == ':' || c == ';'
                    || c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')){
                return false;
            }
        }
        return true;
    }
}