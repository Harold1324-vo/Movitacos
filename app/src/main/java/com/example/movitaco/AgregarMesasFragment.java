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

import org.json.JSONObject;

public class AgregarMesasFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {
    EditText idTaqueria, nombreMesa;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agregar_mesas, container, false);
        idTaqueria = (EditText) v.findViewById(R.id.etIdProducto);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        nombreMesa = (EditText) v.findViewById(R.id.etMesa);
        btnRegistrar = (Button) v.findViewById(R.id.btnRegistrar);

        request = Volley.newRequestQueue(getContext());

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreMesa.getText().toString();

                if(nombre.isEmpty()){
                    nombreMesa.setError("Ingresa un nombre");
                }else if(nombre.length() < 5){
                    nombreMesa.setError("El nombre es muy corto");
                }else if(validarNombre(nombre) == false){
                    nombreMesa.setError("Ingresa solo letras o números");
                }

                if((validarNombre(nombre) == true && nombre.length() >= 5)){
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
        String url = "http://192.168.0.107/crudMesas/apiRegistrarMesa.php?idTaqueria=" + idTaqueria.getText().toString() +
                "&nombreMesa=" + nombreMesa.getText().toString();
        url = url.replace(" ","%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Mesa Registrada", Toast.LENGTH_SHORT).show();
        progress.hide();
        nombreMesa.setText("");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progress.hide();
        Toast.makeText(getContext(), "Mesa No Registrada" , Toast.LENGTH_SHORT).show();
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
}