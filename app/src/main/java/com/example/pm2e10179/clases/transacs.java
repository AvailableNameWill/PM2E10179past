package com.example.pm2e10179.clases;

public class transacs {

    public static final String dbName = "Exam";

    public  static final String tbContacts = "contacts";

    public static final String id = "id";
    public static final String pais = "pais";
    public static final String name = "name";
    public static final String tel = "tel";
    public static final String nota = "nota";
    //public static final String foto = "foto";

    public static final String crearTblContacts = "CREATE TABLE contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                +" pais TEXT, name TEXT, tel TEXT, nota TEXT)";

    public static final String getContacts = "SELECT * FROM " + transacs.tbContacts;

    public static final  String DropTcontacts = "DROP TABLE IF EXISTS contacts";
}
