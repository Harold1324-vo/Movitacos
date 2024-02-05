package com.example.movitaco;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class AgregarAlimentosFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener{
    EditText idTaqueria, nombreProducto, tipoProducto, precioProducto;
    Button btnRegistrar;
    ProgressDialog progress;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    private PreferenceHelper preferenceHelper;

    public AgregarAlimentosFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_agregar_alimentos, container, false);
        idTaqueria = (EditText) v.findViewById(R.id.etIdProducto);
        preferenceHelper = new PreferenceHelper(getContext());
        idTaqueria.setText(preferenceHelper.getIdTaqueria());

        nombreProducto = (EditText) v.findViewById(R.id.etNombreProducto);
        tipoProducto = (EditText) v.findViewById(R.id.etTipoProducto);
        precioProducto = (EditText) v.findViewById(R.id.etPrecioProducto);
        btnRegistrar = (Button) v.findViewById(R.id.btnActualizar);

        request = Volley.newRequestQueue(getContext());

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombreProducto.getText().toString();
                String tipo = tipoProducto.getText().toString();
                String precio = precioProducto.getText().toString();

                if(nombre.isEmpty()){
                    nombreProducto.setError("Ingresa un nombre");
                }else if(nombre.length() < 5){
                    nombreProducto.setError("El nombre es muy corto");
                }else if(validarNombre(nombre) == false){
                    nombreProducto.setError("Ingresa solo letras o números");
                }

                if(tipo.isEmpty()){
                    tipoProducto.setError("Ingresa un tipo");
                }else if(tipo.length() < 5){
                    tipoProducto.setError("El tipo es muy corto");
                }else if(validarNombre(nombre) == false){
                    tipoProducto.setError("Ingresa solo letras o números");
                }

                if(precio.isEmpty()){
                    precioProducto.setError("Ingresa el precio");
                }

                if((validarNombre(nombre) == true && nombre.length() >= 5)
                        && (tipo.length() >= 5) && (!precio.isEmpty())){
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
        String url = "http://192.168.0.107/crudAlimentos/apiRegistrarProducto.php?idTaqueria=" + idTaqueria.getText().toString() +
                "&nombreProducto=" + nombreProducto.getText().toString() + "&tipoProducto=" + tipoProducto.getText().toString()
                + "&precioProducto=" + precioProducto.getText().toString();
        url = url.replace(" ","%20");

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onResponse(JSONObject response) {
        Toast.makeText(getContext(), "Producto Agregado", Toast.LENGTH_SHORT).show();
        progress.hide();
        idTaqueria.setText("");
        nombreProducto.setText("");
        tipoProducto.setText("");
        precioProducto.setText("");
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        progress.hide();
        Toast.makeText(getContext(), "Producto No Agregado", Toast.LENGTH_SHORT).show();
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