package com.example.pm2e10179.clases;

public class Contactos {

    private Integer id;
    private String pais;
    private String nombre;
    private String tel;
    private String nota;
    //private String foto;
    //private byte[] img;

    public Contactos(){}

    public Contactos(Integer _id,String _pais, String _nombre, String _tel, String _nota){// String _foto){//, byte [] _img){
        id = _id;
        pais = _pais;
        nombre = _nombre;
        tel = _tel;
        nota = _nota;
        //img = img;
        //foto = _foto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    /*public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }*/
}
