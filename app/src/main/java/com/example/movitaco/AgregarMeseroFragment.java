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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.movitaco.R;

import org.json.JSONObject;

public class AgregarMeseroFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{

    EditText idTaqueria,usuarioMesero, telefonoMesero, contrasenaMesero;
    Button btnRegistrar;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agregar_mesero, container, false);
        idTaqueria = (EditText) v.findViewById(R.id.etIdTaqueria);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        usuarioMesero = (EditText) v.findViewById(R.id.etUsuarioMesero);
        telefonoMesero = (EditText) v.findViewById(R.id.etTelefonoMesero);
        contrasenaMesero = (EditText) v.findViewById(R.id.etContrasenaMesero);
        btnRegistrar = (Button) v.findViewById(R.id.btnRegistrar);

        request = Volley.newRequestQueue(getContext());

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = usuarioMesero.getText().toString();
                String telefono = telefonoMesero.getText().toString();
                String contrasena = contrasenaMesero.getText().toString();
                if(nombre.isEmpty()){
                    usuarioMesero.setError("Ingresa un nombre");
                }else if(nombre.length() < 5){
                    usuarioMesero.setError("El nombre es muy corto");
                }else if(validarNombre(nombre) == false){
                    usuarioMesero.setError("Ingresa solo letras o números");
                }
                if(telefono.isEmpty()){
                    telefonoMesero.setError("Ingresa un teléfono");
                }else if(telefono.length() < 10){
                    telefonoMesero.setError("Teléfono incorrecto");
                }else if(validarTelefono(telefono) == false){
                    telefonoMesero.setError("Ingresa solo números");
                }
                if(contrasena.isEmpty()){
                    contrasenaMesero.setError("Ingresa una contraseña");
                }else if(contrasena.length() < 6){
                    contrasenaMesero.setError("Contraseña muy débil");
                }else if(validarContraseña(contrasena) == false){
                    contrasenaMesero.setError("Ingresa letras, números o carácteres especiales");
                }
                if((validarNombre(nombre) == true && nombre.length() >= 5)
                        && (validarTelefono(telefono) == true && telefono.length() == 10)
                        && (validarContraseña(contrasena) == true && contrasena.length() >= 6)){
                    cargarWebService();
                }
            }
        });
        return v;
    }

    private void cargarWebService() {

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.show();

        //192.168.137.127
        //192.168.1.73
        String url = "http://192.168.0.107/crudMeseros/apiRegistrarMesero.php?idTaqueria=" + idTaqueria.getText().toString() +
                "&usuarioMesero=" + usuarioMesero.getText().toString() + "&telefonoMesero=" + telefonoMesero.getText().toString() +
                "&contrasenaMesero=" + contrasenaMesero.getText().toString();
        url = url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Mesero Registrado", Toast.LENGTH_SHORT).show();
        progress.hide();
        usuarioMesero.setText("");
        telefonoMesero.setText("");
        contrasenaMesero.setText("");
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progress.hide();
        Toast.makeText(getContext(), "Mesero No Registrado" , Toast.LENGTH_SHORT).show();
        Log.i("Error", error.toString());
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