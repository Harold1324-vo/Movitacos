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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ModificarMesasFragment extends Fragment {
    ImageButton btnBuscarId;
    EditText idTaqueria, etIdMesa, etNombreMesa;
    Button btnActualizar;
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
        View vista = inflater.inflate(R.layout.fragment_modificar_mesas, container, false);

        idTaqueria = (EditText) vista.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        btnBuscarId = vista.findViewById(R.id.btnBuscarId);
        etIdMesa = vista.findViewById(R.id.IdMesa);
        etNombreMesa = vista.findViewById(R.id.etNombreMesas);
        btnActualizar = vista.findViewById(R.id.btnActualizar);

        btnBuscarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMesa = etIdMesa.getText().toString();

                if(idMesa.isEmpty()){
                    etIdMesa.setError("Ingresa el ID");
                }else{
                    cargarWebService();
                }
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMesa = etIdMesa.getText().toString();
                String nombreMesa = etNombreMesa.getText().toString();

                if(idMesa.isEmpty()){
                    etIdMesa.setError("Ingresa el ID");
                }

                if(nombreMesa.isEmpty()){
                    etNombreMesa.setError("Ingresa un nombre");
                }else if(nombreMesa.length() < 5){
                    etNombreMesa.setError("El nombre es muy corto");
                }else if(validarNombre(nombreMesa) == false){
                    etNombreMesa.setError("Ingresa solo letras o números");
                }

                if((validarNombre(nombreMesa) == true && nombreMesa.length() >= 5)){
                    webServiceActualizar();
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

                Mesas mesas=new Mesas();

                JSONArray json=response.optJSONArray("mesas");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    mesas.setIdMesa(jsonObject.optInt("idMesa"));
                    mesas.setNombreMesa(jsonObject.optString("nombreMesa"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                etNombreMesa.setText(mesas.getNombreMesa());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Esta Mesa No Existe", Toast.LENGTH_LONG).show();
                System.out.println();
                progressDialog.hide();
                Log.d("ERROR: ", error.toString());
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
        String url = "http://192.168.0.107/crudMesas/apiActualizarMesa.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idMesa=" + etIdMesa.getText().toString();

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getContext(), "Mesa Actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Mesa No Actualizada", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta: ", "" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Mesa No Actualizada", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String idMesa = etIdMesa.getText().toString();
                String nombreMesa = etNombreMesa.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("idMesa", idMesa);
                parametros.put("nombreMesa", nombreMesa);

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