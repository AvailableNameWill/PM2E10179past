package com.example.pm2e10179.clases;

import java.util.ArrayList;

public class paises {
    private ArrayList<String> paises;

    public  paises(){
        paises = new ArrayList<String>();
        String p1 = "Honduras (504)";
        paises.add(p1);
        String p2 = "El Salvador (503)";
        paises.add(p2);
        String p3 = "Guatemala (502)";
        paises.add(p3);
    }

    public ArrayList<String> getPaises() {
        return paises;
    }

    public void setPaises(ArrayList<String> paises) {
        this.paises = paises;
    }
}
