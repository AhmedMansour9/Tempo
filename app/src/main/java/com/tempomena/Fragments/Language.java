package com.tempomena.Fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tempomena.Activites.Home;
import com.tempomena.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Language extends Fragment {

    RelativeLayout btn_Arabic,Btn_English,Rela_Herbian;
    SharedPreferences.Editor share;
    View view;

    public Language() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_language, container, false);
        Home.Rela_Govern.setVisibility(View.GONE);
        btn_Arabic=view.findViewById(R.id.Rela_Arabic);
        Btn_English=view.findViewById(R.id.Rela_English);
        Lan_Arabic();
        Lan_English();
        return view;
    }


    public void Lan_Arabic(){
        btn_Arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share=getActivity().getSharedPreferences("Language",MODE_PRIVATE).edit();
                share.putString("Lann","ar");
                share.commit();

                startActivity(new Intent(getContext(), Home.class));
                getActivity().finish();


            }
        });
    }
    public void Lan_English(){
        Btn_English.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share=getActivity().getSharedPreferences("Language",MODE_PRIVATE).edit();
                share.putString("Lann","en");
                share.commit();
                startActivity(new Intent(getContext(), Home.class));
                getActivity().finish();


            }
        });

    }

}
