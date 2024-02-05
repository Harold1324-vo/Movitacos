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
import com.example.movitaco.Meseros;
import com.example.movitaco.R;
import com.example.movitaco.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EliminarMeseroFragment extends Fragment {
    ImageButton btnBuscarId;
    TextView idTaqueria, etIdMesero, etUsuarioMesero, etTelefonoMesero, etContrasenaMesero;
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
        View vista = inflater.inflate(R.layout.fragment_eliminar_mesero, container, false);
        idTaqueria = (EditText) vista.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        btnBuscarId = vista.findViewById(R.id.btnBuscarId);
        etIdMesero = vista.findViewById(R.id.etIdMesero);
        etUsuarioMesero = vista.findViewById(R.id.etUsuarioMesero);
        etTelefonoMesero = vista.findViewById(R.id.etTelefonoMesero);
        etContrasenaMesero = vista.findViewById(R.id.etContrasenaMesero);
        btnEliminar = vista.findViewById(R.id.btnEliminarMesero);

        btnBuscarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etIdMesero.getText().toString();

                if(idProduto.isEmpty()){
                    etIdMesero.setError("Ingresa el ID");
                }else{
                    cargarWebService();
                }
            }
        });
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etIdMesero.getText().toString();
                if(idProduto.isEmpty()){
                    etIdMesero.setError("Ingresa el ID");
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
        String url="http://192.168.0.107/crudMeseros/apiConsultarMeseroID.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idMesero=" + etIdMesero.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.hide();

                Meseros mesero=new Meseros();

                JSONArray json=response.optJSONArray("mesero");
                JSONObject jsonObject=null;

                try {
                    jsonObject=json.getJSONObject(0);
                    mesero.setIdMesero(jsonObject.optInt("idMesero"));
                    mesero.setNombreUsuario(jsonObject.optString("usuarioMesero"));
                    mesero.setTelefonoMesero(jsonObject.optString("telefonoMesero"));
                    mesero.setContrasenaMesero(jsonObject.optString("contrasenaMesero"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                etUsuarioMesero.setText(mesero.getNombreUsuario());
                etTelefonoMesero.setText(mesero.getTelefonoMesero());
                etContrasenaMesero.setText(mesero.getContrasenaMesero());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Mesero No Encontrado", Toast.LENGTH_LONG).show();
                System.out.println();
                progressDialog.hide();
                Log.d("ERROR: ", error.toString());
                etUsuarioMesero.setText("");
                etTelefonoMesero.setText("");
                etContrasenaMesero.setText("");
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
        String url="http://192.168.0.107/crudMeseros/apiEliminarMesero.php?idTaqueria=" + idTaqueria.getText().toString()
                + "&idMesero=" + etIdMesero.getText().toString();

        stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();

                if (response.trim().equalsIgnoreCase("Eliminado exitosamente")){
                    etIdMesero.setText("");
                    etUsuarioMesero.setText("");
                    etTelefonoMesero.setText("");
                    etContrasenaMesero.setText("");
                    Toast.makeText(getContext(),"Mesero Eliminado",Toast.LENGTH_SHORT).show();
                }else{
                    if (response.trim().equalsIgnoreCase("No eliminado")){
                        Toast.makeText(getContext(),"Mesero No Eliminado",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }else{
                        Toast.makeText(getContext(),"Mesero No Eliminado",Toast.LENGTH_SHORT).show();
                        Log.i("RESPUESTA: ",""+response);
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Mesero No Eliminado",Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
        //request.add(stringRequest);
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);
    }
}