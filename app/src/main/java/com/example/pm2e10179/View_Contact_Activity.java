package com.example.pm2e10179;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pm2e10179.Configuracion.Conexion;
import com.example.pm2e10179.clases.Contactos;
import com.example.pm2e10179.clases.transacs;

import java.util.ArrayList;

public class View_Contact_Activity extends AppCompatActivity {

    ListView listContact;
    SearchView searchV;
    Button btnDelC, btnCall, btnUpdtC,btnViewImg,btnshareC;
    Conexion con;
    ArrayList<Contactos> Clist;
    ArrayList<String> listaConcat;
    Contactos contacts;
    int id = 0;
    boolean search;
    AlertDialog.Builder builder;

    private static final int rquest_call= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        builder = new AlertDialog.Builder(View_Contact_Activity.this);
        search = false;
        cargarObj();
        con = new Conexion(this, transacs.dbName, null, 1);
        getListC();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaConcat);
        listContact.setAdapter(adp);

        listContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                fillContact(i);
                id = contacts.getId();
            }
        });

        btnUpdtC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Contacts_Activity.class);
                if(contacts!=null){
                    Contacts_Activity.id = contacts.getId();
                    Contacts_Activity.pais = contacts.getPais();
                    Contacts_Activity.nombre = contacts.getNombre();
                    Contacts_Activity.tel = contacts.getTel();
                    Contacts_Activity.nota = contacts.getNota();
                    Contacts_Activity.isUpdt = true;
                    startActivity(intent);
                } else Toast.makeText(View_Contact_Activity.this, "!!!!Alerta " + "\n No ha seleccionado ningun contacto", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contacts!=null) delC();
                else Toast.makeText(View_Contact_Activity.this, "!!!!Alerta " + "\n No ha seleccionado ningun contacto", Toast.LENGTH_SHORT).show();
            }
        });

        btnshareC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contacts!=null) shareContact();
                else Toast.makeText(View_Contact_Activity.this, "!!!!Alerta " + "\n No ha seleccionado ningun contacto", Toast.LENGTH_SHORT).show();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contacts!=null){
                    createDialog();
                }
                else Toast.makeText(View_Contact_Activity.this, "!!!!Alerta " + "\n No ha seleccionado ningun contacto", Toast.LENGTH_SHORT).show();
            }
        });

        searchV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                ArrayList<String> names = new ArrayList<String>();
                for(int i=0; i<listaConcat.size(); i++){
                    String[] n = listaConcat.get(i).split("\\ | ");
                    names.add(n[0]);
                }
                if(names.contains(s)){
                    adp.getFilter().filter(s);
                    search = true;
                }else{
                    Toast.makeText(View_Contact_Activity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty() && search){
                    updtList();
                }
                return false;
            }
        });
    }

    private void getListC(){
        SQLiteDatabase db = con.getReadableDatabase();
        Contactos listC = null;

        Clist= new ArrayList<Contactos>();
        Cursor cursor =db.rawQuery(transacs.getContacts,null);

        while (cursor.moveToNext()){
            listC = new Contactos();
            listC.setId(cursor.getInt(0));
            listC.setPais(cursor.getString(1));
            listC.setNombre(cursor.getString(2));
            listC.setTel(cursor.getString(3));
            listC.setNota(cursor.getString(4));

            Clist.add(listC);
        }
        cursor.close();
        fillList();
    }

    private void delC(){
        try{
            Conexion con = new Conexion(this, transacs.dbName,null,1);
            SQLiteDatabase bd = con.getWritableDatabase();


            String[] args = new String[]{Integer.toString(id)};

            Long result = Long.valueOf(bd.delete(transacs.tbContacts, "id = ?", args));

            Toast.makeText(this, "!!!Aviso " + "\n Contacto Eliminado" + result.toString(), Toast.LENGTH_SHORT).show();
            bd.close();
            updtList();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void fillContact(int i){
        contacts = new Contactos();
        contacts.setId(Clist.get(i).getId());
        contacts.setPais(Clist.get(i).getPais());
        contacts.setNombre(Clist.get(i).getNombre());
        contacts.setTel(Clist.get(i).getTel());
        contacts.setNota(Clist.get(i).getNota());
    }

    private void updtList(){
        Clist.clear();
        listaConcat.clear();
        getListC();
        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listaConcat);
        listContact.setAdapter(adp);
    }

    private void call(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+contacts.getTel()));
        startActivity(callIntent);

    }

    private void createDialog(){
        builder.setMessage("Desea realziar la llamada a " + contacts.getNombre());
        builder.setTitle("Accion");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                permisos();
            }
        });
        builder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void permisos(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},rquest_call);
        }else{
            call();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case rquest_call: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                }
                return;
            }
        }
    }

    private void shareContact(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        String name = "Nombre: " + contacts.getNombre() + " \n Numero: " + contacts.getTel() ;
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact));
        intent.putExtra(Intent.EXTRA_TEXT, name);
        startActivity(Intent.createChooser(intent, getString(R.string.opc)));
    }

    private void fillList(){
        listaConcat = new ArrayList<String>();

        for(int i=0; i<Clist.size(); i++){
            listaConcat.add(Clist.get(i).getNombre() + " | "
                          + Clist.get(i).getTel());
        }
    }

    private void cargarObj(){
        listContact = (ListView) findViewById(R.id.listContact);
        btnshareC = (Button) findViewById(R.id.btnShareC);
        btnViewImg = (Button) findViewById(R.id.btnViewImg);
        btnDelC = (Button) findViewById(R.id.btnDelC);
        btnUpdtC = (Button) findViewById(R.id.btnUpdtC);
        btnCall = (Button) findViewById(R.id.btnCall);
        searchV = findViewById(R.id.searchV);
    }
}