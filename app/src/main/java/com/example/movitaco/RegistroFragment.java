package com.example.movitaco;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
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

public class RegistroFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        Button btnRegistro;
        EditText nombre, correo, telefono, direccion, contrasena, validarContraseña;

        btnRegistro = v.findViewById(R.id.btnRegistrar);
        nombre = v.findViewById(R.id.edtTaqueria);
        correo = v.findViewById(R.id.edtCorreo);
        direccion = v.findViewById(R.id.edtDireccion);
        telefono = v.findViewById(R.id.edtTelefono);
        contrasena = v.findViewById(R.id.edtContraseña);
        validarContraseña = v.findViewById(R.id.edtConfirmarContraseña);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Usuario = nombre.getText().toString();
                String Correo = correo.getText().toString();
                String Telefono = telefono.getText().toString();
                String Direccion = direccion.getText().toString();
                String Contrasena = contrasena.getText().toString();
                String Validar = validarContraseña.getText().toString();

                //Declarar una URL de tipo string, pasarle la variable y con su contenido
                //192.168.137.127
                //192.168.1.73
                String url = "http://192.168.0.107/registroTaqueria/apiConsumoTaqueria.php?nombreTaqueria="+Usuario+"&correoElectronico="+Correo+"&telefonoTaqueria="+Telefono+"&direccion="+Direccion+"&contrasenaTaqueria="+Contrasena;

                if(Usuario.isEmpty()){
                    nombre.setError("Ingresa un nombre");
                }else if(Usuario.length() < 5){
                    nombre.setError("El nombre es muy corto");
                }else if(validarNombre(Usuario) == false){
                    nombre.setError("Ingresa solo letras o números");
                }

                if(Correo.isEmpty()){
                    correo.setError("Ingresa un email");
                }else if(!Patterns.EMAIL_ADDRESS.matcher(Correo).matches()){
                    correo.setError("Email incorrecto");
                }

                if(Telefono.isEmpty()){
                    telefono.setError("Ingresa un teléfono");
                }else if(Telefono.length() < 10){
                    telefono.setError("Teléfono incorrecto");
                }else if(validarTelefono(Telefono) == false){
                    telefono.setError("Ingresa solo números");
                }

                if(Direccion.isEmpty()){
                    direccion.setError("Ingresa una dirección");
                }else if(Direccion.length() < 5){
                    direccion.setError("La dirección es muy corta");
                }else if(validarDireccion(Direccion) == false){
                    direccion.setError("Ingresa solo letras o números");
                }

                if(Contrasena.isEmpty()){
                    contrasena.setError("Ingresa una contraseña");
                }else if(Contrasena.length() < 6){
                    contrasena.setError("Contraseña muy débil");
                }else if(validarContraseña(Contrasena) == false){
                    contrasena.setError("Ingresa letras, números o carácteres especiales");
                }

                if(Validar.isEmpty()){
                    validarContraseña.setError("Verifique contraseña");
                }else if(!Validar.equals(Contrasena)){
                    validarContraseña.setError("Las contraseñas no coinciden");
                }

                if((validarNombre(Usuario) == true && Usuario.length() >= 5)
                        && (Patterns.EMAIL_ADDRESS.matcher(Correo).matches())
                        && (validarTelefono(Telefono) == true && Telefono.length() == 10)
                        && (validarDireccion(Direccion) == true && Direccion.length() >= 5)
                        && (validarContraseña(Contrasena) == true && Contrasena.length() >= 6)
                        && Validar.equals(Contrasena)){
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(v.getContext(), response.toString(), Toast.LENGTH_LONG).show();
                            Navigation.findNavController(v).navigate(R.id.action_registroFragment_to_inicioSesionFragment);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                    queue.add(jsonObjectRequest);
                }
            }
        });
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

    boolean validarDireccion(String direccion){
        for(int x = 0;x < direccion.length();x++){
            char c = direccion.charAt(x);
            if(!(direccion.length() > 250
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