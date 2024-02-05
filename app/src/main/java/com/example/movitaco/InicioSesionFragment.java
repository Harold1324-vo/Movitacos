package com.example.movitaco;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

public class InicioSesionFragment extends Fragment {
    public String KEY_SUCCESS = "status";
    private PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        EditText user = (EditText) view.findViewById(R.id.edtUsuario);
        EditText password = (EditText) view.findViewById(R.id.edtContrasena);
        Button btnInicioSesion = (Button) view.findViewById(R.id.btnIniciarSesion);
        TextView bntRegistrar = (TextView) view.findViewById(R.id.txtRegistro);

        Spinner spRol = view.findViewById(R.id.spRol);
        String [] opciones = {"Seleccione su rol:","Administrador", "Mesero"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, opciones);
        spRol.setAdapter(adapter);

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        preferenceHelper = new PreferenceHelper(getActivity());

        spRol.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position).equals("Administrador")){
                    btnInicioSesion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            validarTaqueria();
                        }

                        private void validarTaqueria() {
                            String Usuario = user.getText().toString().trim();
                            String Contrasena = password.getText().toString().trim();
                            //192.168.137.127
                            //192.168.1.73
                            String url = "http://192.168.0.107/inicioSesion/apiLoginAdministrador.php";

                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (Usuario.isEmpty() && Contrasena.isEmpty()) {
                                        user.setError("Completa el Usuario");
                                        password.setError("Completa la Contraseña");
                                    } else if (Usuario.isEmpty()) {
                                        user.setError("Complete el campo");
                                    } else if (Contrasena.isEmpty()) {
                                        password.setError("Complete el campo");
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if(jsonObject.getString(KEY_SUCCESS).equals("true")){
                                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                                for(int i=0;i<dataArray.length();i++){
                                                    Toast.makeText(getActivity().getApplicationContext(), "Bienvenido Administrador", Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(view).navigate(R.id.action_inicioSesionFragment_to_principalAdministradorFragment);
                                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                                    preferenceHelper.putID(dataobj.getString(Constants.Params.idTaqueria));
                                                }
                                            }else{
                                                Toast.makeText(getActivity().getApplicationContext(), "Usuario o Contraseña Errónea", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String,String> getParams() throws AuthFailureError{
                                    HashMap<String,String> hashMap = new HashMap<>();
                                    hashMap.put("nombreTaqueria",user.getText().toString().trim());
                                    hashMap.put("contrasenaTaqueria",password.getText().toString().trim());
                                    return hashMap;
                                }
                            };
                            queue.add(request);
                        }
                    });
                }

                if(parent.getItemAtPosition(position).equals("Mesero")){
                    btnInicioSesion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            validarMesero();
                        }

                        private void validarMesero() {
                            String Usuario = user.getText().toString().trim();
                            String Contrasena = password.getText().toString().trim();
                            //192.168.137.127
                            //192.168.1.73
                            String url = "http://192.168.0.107/inicioSesion/apiLoginMesero.php";

                            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (Usuario.isEmpty() && Contrasena.isEmpty()) {
                                        user.setError("Completa el Usuario");
                                        password.setError("Completa la Contraseña");
                                    } else if (Usuario.isEmpty()) {
                                        user.setError("Complete el campo");
                                    } else if (Contrasena.isEmpty()) {
                                        password.setError("Complete el campo");
                                    } else {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            if(jsonObject.getString(KEY_SUCCESS).equals("true")){
                                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                                for(int i=0;i<dataArray.length();i++){
                                                    Toast.makeText(getActivity().getApplicationContext(), "Bienvenido Mesero", Toast.LENGTH_SHORT).show();
                                                    Navigation.findNavController(view).navigate(R.id.action_inicioSesionFragment_to_principalMeseroFragment);
                                                    JSONObject dataobj = dataArray.getJSONObject(i);
                                                    preferenceHelper.putID(dataobj.getString(Constants.Params.idTaqueria));
                                                }
                                            }else{
                                                Toast.makeText(getActivity().getApplicationContext(), "Usuario o Contraseña Errónea", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(view.getContext(), error.toString(), Toast.LENGTH_LONG).show();
                                }
                            }){
                                @Override
                                protected Map<String,String> getParams() throws AuthFailureError{
                                    HashMap<String,String> hashMap = new HashMap<>();
                                    hashMap.put("usuarioMesero",user.getText().toString().trim());
                                    hashMap.put("contrasenaMesero",password.getText().toString().trim());
                                    return hashMap;
                                }
                            };
                            queue.add(request);
                        }
                    });
                }

                if(parent.getItemAtPosition(position).equals("Seleccione su rol:")){
                    btnInicioSesion.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String Usuario = user.getText().toString().trim();
                            String Contrasena = password.getText().toString().trim();
                            if (Usuario.isEmpty() && Contrasena.isEmpty()) {
                                user.setError("Completa el Usuario");
                                password.setError("Completa la Contraseña");
                            } else if (Usuario.isEmpty()) {
                                user.setError("Complete el campo");
                            } else if (Contrasena.isEmpty()) {
                                password.setError("Complete el campo");
                            }
                            Toast.makeText(getContext(), "Escoge tu rol y llena los campos", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bntRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_inicioSesionFragment_to_registroFragment);
            }
        });
    }
}