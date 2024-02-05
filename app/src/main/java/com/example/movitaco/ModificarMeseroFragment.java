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

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

public class ModificarMeseroFragment extends Fragment {
    ImageButton btnBuscarId;
    TextView idTaqueria, etIdMesero, etUsuarioMesero, etTelefonoMesero, etContrasenaMesero;
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
        View vista = inflater.inflate(R.layout.fragment_modificar_mesero, container, false);
        idTaqueria = (EditText) vista.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        btnBuscarId = vista.findViewById(R.id.btnBuscarId);
        etIdMesero = vista.findViewById(R.id.etIdMesero);
        etUsuarioMesero = vista.findViewById(R.id.etUsuarioMesero);
        etTelefonoMesero = vista.findViewById(R.id.etTelefonoMesero);
        etContrasenaMesero = vista.findViewById(R.id.etContrasenaMesero);
        btnActualizar = vista.findViewById(R.id.btnActualizar);

        btnBuscarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idProduto = etIdMesero.getText().toString();
                if(idProduto.isEmpty()){
                    etIdMesero.setError("Ingresa el ID");
                }else{
                    cargarWebService();
                    etUsuarioMesero.setError(null);
                    etTelefonoMesero.setError(null);
                    etContrasenaMesero.setText(null);
                }
            }
        });
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idMesero = etIdMesero.getText().toString();
                String usuarioMesero = etUsuarioMesero.getText().toString();
                String telefonoMesero = etTelefonoMesero.getText().toString();
                String contrasenaMesero = etContrasenaMesero.getText().toString();

                if(idMesero.isEmpty()){
                    etIdMesero.setError("Ingresa el ID");
                }

                if(usuarioMesero.isEmpty()){
                    etUsuarioMesero.setError("Ingresa un nombre");
                }else if(usuarioMesero.length() < 5){
                    etUsuarioMesero.setError("El nombre es muy corto");
                }else if(validarNombre(usuarioMesero) == false){
                    etUsuarioMesero.setError("Ingresa solo letras o números");
                }

                if(telefonoMesero.isEmpty()){
                    etTelefonoMesero.setError("Ingresa un teléfono");
                }else if(telefonoMesero.length() < 10){
                    etTelefonoMesero.setError("Teléfono incorrecto");
                }else if(validarTelefono(telefonoMesero) == false){
                    etTelefonoMesero.setError("Ingresa solo números");
                }

                if(contrasenaMesero.isEmpty()){
                    etContrasenaMesero.setError("Ingresa una contraseña");
                }else if(contrasenaMesero.length() < 6){
                    etContrasenaMesero.setError("Contraseña muy debil");
                }else if(validarContraseña(contrasenaMesero) == false){
                    etContrasenaMesero.setError("Ingresa letras, números o carácteres especiales");
                }

                if((validarNombre(usuarioMesero) == true && usuarioMesero.length() >= 5)
                        && (validarTelefono(telefonoMesero) == true && telefonoMesero.length() == 10)
                        && (validarContraseña(contrasenaMesero) == true && contrasenaMesero.length() >= 6)){
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
        String url="http://192.168.0.107/crudMeseros/apiConsultarMeseroID.php?idTaqueria=" + idTaqueria.getText().toString()
                +"&idMesero=" + etIdMesero.getText().toString();

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
                Toast.makeText(getContext(), "El Mesero No Existe", Toast.LENGTH_LONG).show();
                System.out.println();
                progressDialog.hide();
                etUsuarioMesero.setText("");
                etTelefonoMesero.setText("");
                etContrasenaMesero.setText("");
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
        String url = "http://192.168.0.107/crudMeseros/apiActualizarMesero.php?idTaqueria=" + idTaqueria.getText().toString()
                +"&idMesero=" + etIdMesero.getText().toString();

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.hide();
                if (response.trim().equalsIgnoreCase("actualiza")) {
                    Toast.makeText(getContext(), "Mesero Actualizado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Mesero No Actualizado", Toast.LENGTH_SHORT).show();
                    Log.i("Respuesta: ", "" + response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Mesero No Actualizado", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String idMesero = etIdMesero.getText().toString();
                String usuarioMesero = etUsuarioMesero.getText().toString();
                String contrasenaMesero = etContrasenaMesero.getText().toString();
                String telefonoMesero = etTelefonoMesero.getText().toString();

                Map<String,String> parametros = new HashMap<>();
                parametros.put("idMesero", idMesero);
                parametros.put("usuarioMesero", usuarioMesero);
                parametros.put("contrasenaMesero", contrasenaMesero);
                parametros.put("telefonoMesero", telefonoMesero);

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

    boolean validarTelefono(String telefono){
        for(int x=0;x<telefono.length();x++){
            char c = telefono.charAt(x);
            if(telefono.length() != 10 || (c >= 'a' && c<= 'z') || (c >= 'A' && c<= 'Z') || c == ' ' || c == '-' || c == '.' || c == '(' || c == '/'
                    || c == ')' || c == 'N' || c == ',' || c == '*' || c == ';' || c == '#' || c == '+'){
                return false;
            }
        }
        return true;
    }

    boolean validarContraseña(String contraseña){
        boolean numeros = false;
        boolean letras = false;
        for(int x=0;x<contraseña.length();x++){
            char c = contraseña.charAt(x);
            //Si no está entre a y z, ni entre A y Z, ni es un espacio
            if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == 'ñ' || c == 'Ñ' || c == 'á' || c == 'é' || c == 'í' || c == 'ó' || c == 'ú'
                    || c == 'Á' || c == 'É' || c == 'Í' || c == 'Ó'  || c == 'Ú' || c == '@' || c == '#' || c == '$' || c == '_' || c == '-'
                    || c == '&' || c == '*'){
                letras = true;
            }
            if((c >= '0' && c <= '9')){
                numeros = true;
            }
        }
        if(numeros == true && letras == true){
            return true;
        }
        return true;
    }
}