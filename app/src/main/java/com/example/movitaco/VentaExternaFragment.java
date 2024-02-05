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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class VentaExternaFragment extends Fragment {
    ArrayList<String> alimento = new ArrayList<>();
    ArrayList<String> idAlimento = new ArrayList<>();
    ArrayList<String> bebida = new ArrayList<>();
    ArrayList<String> idBebida = new ArrayList<>();
    ArrayList<String> idMesa = new ArrayList<>();
    ArrayList<String> mesa = new ArrayList<>();
    Button btnRegistrarVenta;
    EditText nombreCliente, telefono, direccion, cantidadAlimento, cantidadBebida;
    TextView ventaTotal;
    CheckBox idCebolla, idCilantro, idSalsaRoja, idSalsaVerde;
    ArrayAdapter<String> nombreAlimento, nombreBebida, nombreMesa;
    RequestQueue requestQueueAlimento, requestQueueBebida, requestQueueMesa, requestQueueVenta;
    JsonObjectRequest jsonObjectRequestAlimento, jsonObjectRequestBebida, jsonObjectRequestMesa, jsonObjectRequestVenta;
    Spinner spinnerAlimento, spinnerBebida, spinnerMesa;
    ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_venta_externa, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        nombreCliente = (EditText) view.findViewById(R.id.nombreCliente);
        telefono = (EditText) view.findViewById(R.id.telefonoCliente);
        direccion = (EditText) view.findViewById(R.id.direccionCliente);
        spinnerAlimento = (Spinner) view.findViewById(R.id.sAlimento);
        spinnerBebida = (Spinner) view.findViewById(R.id.sBebida);
        spinnerMesa = (Spinner) view.findViewById(R.id.sMesa);
        cantidadAlimento = (EditText) view.findViewById(R.id.cantidadProducto);
        cantidadBebida = (EditText) view.findViewById(R.id.cantidadBebida);
        idCebolla = (CheckBox) view.findViewById(R.id.idCebolla);
        idCilantro = (CheckBox) view.findViewById(R.id.idCilantro);
        idSalsaRoja = (CheckBox) view.findViewById(R.id.idSalsaR);
        idSalsaVerde = (CheckBox) view.findViewById(R.id.idSalsaV);
        btnRegistrarVenta = (Button) view.findViewById(R.id.btnRegistrarVenta);
        requestQueueAlimento = Volley.newRequestQueue(getContext());
        requestQueueBebida = Volley.newRequestQueue(getContext());
        requestQueueMesa = Volley.newRequestQueue(getContext());
        requestQueueVenta = Volley.newRequestQueue(getContext());

        String url = "http://192.168.0.107/apisMesero/apiSMesa.php";
        jsonObjectRequestMesa = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("mesas");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idmesa = jsonObject.optString("idMesa");
                        String nombremesa = jsonObject.optString("nombreMesa");
                        idMesa.add(idmesa);
                        mesa.add(nombremesa);
                        nombreMesa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, mesa);
                        spinnerMesa.setAdapter(nombreMesa);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueMesa.add(jsonObjectRequestMesa);
        spinnerMesa.setAdapter(nombreMesa);

        String url2 = "http://192.168.0.107/apisMesero/apiSAlimento.php";
        jsonObjectRequestAlimento = new JsonObjectRequest(Request.Method.POST,
                url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("producto");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idproducto = jsonObject.optString("idProducto");
                        String nombreproducto = jsonObject.optString("nombreProducto");
                        idAlimento.add(idproducto);
                        alimento.add(nombreproducto);
                        nombreAlimento = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, alimento);
                        spinnerAlimento.setAdapter(nombreAlimento);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueAlimento.add(jsonObjectRequestAlimento);
        spinnerAlimento.setAdapter(nombreAlimento);

        String url3 = "http://192.168.0.107/apisMesero/apiSBebida.php";
        jsonObjectRequestBebida = new JsonObjectRequest(Request.Method.POST,
                url3, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("producto");
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String idproducto = jsonObject.optString("idProducto");
                        String nombreproducto = jsonObject.optString("nombreProducto");
                        idBebida.add(idproducto);
                        bebida.add(nombreproducto);
                        nombreBebida = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, bebida);
                        spinnerBebida.setAdapter(nombreBebida);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueueBebida.add(jsonObjectRequestBebida);
        spinnerBebida.setAdapter(nombreBebida);

        btnRegistrarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarWebService();
            }
        });
    }

    private void cargarWebService() {

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.show();

        String idSMesa = idMesa.get(spinnerMesa.getSelectedItemPosition()).trim();
        String idSAlimento = idAlimento.get(spinnerAlimento.getSelectedItemPosition()).trim();
        String idSBebida = idBebida.get(spinnerBebida.getSelectedItemPosition()).trim();
        String nombre = nombreCliente.getText().toString().trim();
        String telefonoCliente = telefono.getText().toString().trim();
        String direccionCliente = direccion.getText().toString().trim();
        String cAlimento = cantidadAlimento.getText().toString().trim();
        String cBebida = cantidadBebida.getText().toString().trim();
        String url4 = "http://192.168.0.107/apisMesero/insertVentaExterna.php?idMesa=" + idSMesa +
                "&idProducto=" + idSAlimento + "&nombreCliente=" + nombre +"&telefono=" + telefonoCliente + "&direccion=" + direccionCliente + "&catidadProducto=" + cAlimento;
        url4 = url4.replace(" ","%20");

        jsonObjectRequestVenta = new JsonObjectRequest(
                Request.Method.GET, url4 , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progress.hide();
                nombreCliente.setText("");
                telefono.setText("");
                direccion.setText("");
                cantidadAlimento.setText("");
                cantidadBebida.setText("");
                idCilantro.setChecked(false);
                idCebolla.setChecked(false);
                idSalsaRoja.setChecked(false);
                idSalsaVerde.setChecked(false);
                Toast.makeText(getContext(), "Venta realizada exitosamente", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.hide();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueueVenta.add(jsonObjectRequestVenta);
    }
}