package com.example.movitaco;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PrincipalAdministradorFragment extends Fragment {
    private PreferenceHelper preferenceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_principal_administrador, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        Button btnProductos = view.findViewById(R.id.btnAgregarProductos);
        Button btnMesas = view.findViewById(R.id.btnAgregarMesas);
        Button btnMeseros = view.findViewById(R.id.btnAgregarMeseros);

        preferenceHelper = new PreferenceHelper(getContext());
        TextView prueba = view.findViewById(R.id.txtPruebas);
        prueba.setText(preferenceHelper.getIdTaqueria());

        btnProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_principalAdministradorFragment_to_alimentosAdministradorFragment);
            }
        });

        btnMesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_principalAdministradorFragment_to_mesasAdministradorFragment);
            }
        });

        btnMeseros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_principalAdministradorFragment_to_meserosAdministradorFragment);
            }
        });
    }
}