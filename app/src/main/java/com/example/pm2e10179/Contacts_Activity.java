package com.example.pm2e10179;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e10179.Configuracion.Conexion;
import com.example.pm2e10179.clases.paises;
import com.example.pm2e10179.clases.transacs;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Contacts_Activity extends AppCompatActivity {

    ImageView imageView;
    Spinner spnPaises;
    EditText txtNameC, txtTelC,txtNoteC;
    Button btnSaveC, btnSavedC;
    ImageButton btnTake;
    //Bitmap img;
    String PathImagen;
    private paises _paises;

    public static  Integer id=0;
    public static  String pais = "", nombre = "", tel = "", nota = "";
    public static boolean isUpdt=false;

    static final int Peticion_captura_imagen = 100;
    static final int peticion_acceso_cam = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Bundle bundle = getIntent().getExtras();
        cargarObj();

        btnSaveC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUpdt) AgrPersonas();
                else{
                    updtPersonas();
                    isUpdt = false;
                }
            }
        });

        btnSavedC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), View_Contact_Activity.class);
                isUpdt=false;
                startActivity(intent);
            }
        });

        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

    }

    private void AgrPersonas(){

        if(txtNameC.getText().toString().isEmpty()){
            Toast.makeText(this, "!!!!AVISO " + "\n No puede dejar el campo de Texto vacio: " + " Nombre!!", Toast.LENGTH_SHORT).show();
        } else if(txtNoteC.getText().toString().isEmpty()){
            Toast.makeText(this, "!!!!AVISO " + "\n No puede dejar el campo de Texto vacio: " + " Nota!!", Toast.LENGTH_SHORT).show();
        } else if(txtTelC.getText().toString().isEmpty()){
            Toast.makeText(this, "!!!!AVISO " + "\n No puede dejar el campo de Texto vacio: " + " Telefono!!", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                Conexion con = new Conexion(this, transacs.dbName,null,1);
                SQLiteDatabase bd =con.getWritableDatabase();

                ContentValues values =new ContentValues();
                values.put(transacs.pais, spnPaises.getSelectedItem().toString());
                values.put(transacs.name, txtNameC.getText().toString());
                values.put(transacs.tel, txtTelC.getText().toString());
                values.put(transacs.nota, txtNoteC.getText().toString());

                Long result = bd.insert(transacs.tbContacts, transacs.id, values);

                Toast.makeText(this, "Registro ingresado correctamente" + result.toString(), Toast.LENGTH_SHORT).show();
                bd.close();
                limpiar();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void updtPersonas(){
        if(txtNameC.getText().toString().isEmpty()){
            Toast.makeText(this, "!!!!AVISO " + "\n No puede dejar el campo de Texto vacio: " + " Nombre!!", Toast.LENGTH_SHORT).show();
        } else if(txtNoteC.getText().toString().isEmpty()){
            Toast.makeText(this, "!!!!AVISO " + "\n No puede dejar el campo de Texto vacio: " + " Nota!!", Toast.LENGTH_SHORT).show();
        } else if(txtTelC.getText().toString().isEmpty()){
            Toast.makeText(this, "!!!!AVISO " + "\n No puede dejar el campo de Texto vacio: " + " Telefono!!", Toast.LENGTH_SHORT).show();
        }else{
            try{
                Conexion con = new Conexion(this, transacs.dbName,null,1);
                SQLiteDatabase bd = con.getWritableDatabase();

                ContentValues values =new ContentValues();
                values.put(transacs.id, id);
                values.put(transacs.pais, spnPaises.getSelectedItem().toString());
                values.put(transacs.name, txtNameC.getText().toString());
                values.put(transacs.tel, txtTelC.getText().toString());
                values.put(transacs.nota, txtNoteC.getText().toString());

                String[] args = new String[]{Integer.toString(id)};

                Long result = Long.valueOf(bd.update(transacs.tbContacts, values, "id = ?", args));

                Toast.makeText(this, "Registro ingresado correctamente" + result.toString(), Toast.LENGTH_SHORT).show();
                bd.close();
                limpiar();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void limpiar(){
        txtNoteC.setText("");
        txtTelC.setText("");
        txtNameC.setText("");
    }

    private void cargarObj(){
        _paises = new paises();
        imageView = (ImageView) findViewById(R.id.imageView);
        spnPaises = (Spinner) findViewById(R.id.spnPaises);
        txtNameC = (EditText) findViewById(R.id.txtNameC);
        txtNameC.setText(nombre);
        InputFilter[] filter1 = new InputFilter[1];
        filter1[0] = new InputFilter.LengthFilter(50);
        txtNameC.setFilters(filter1);
        txtTelC = (EditText) findViewById(R.id.txtTelC);
        txtTelC.setText(tel);
        InputFilter[] filter2 = new InputFilter[1];
        filter2[0] = new InputFilter.LengthFilter(15);
        txtTelC.setFilters(filter2);
        txtNoteC = (EditText) findViewById(R.id.txtNoteC);
        txtNoteC.setText(nota);
        InputFilter[] filter3 = new InputFilter[1];
        filter3[0] = new InputFilter.LengthFilter(50);
        txtNoteC.setFilters(filter3);
        btnSaveC = (Button) findViewById(R.id.btnSaveC);
        btnSavedC = (Button) findViewById(R.id.btnSavedC);
        btnTake = (ImageButton) findViewById(R.id.btnTake);
        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, _paises.getPaises());
        spnPaises.setAdapter(adp);
    }

    private void permisos(){
        //validar si el permiso esta otorgado o no para tomar fotos
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, peticion_acceso_cam);
        }else{
            //tomarFotos();
            TakePhotoDir();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == peticion_acceso_cam){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //tomarFotos();
                TakePhotoDir();
            }
        }
    }

    private void tomarFotos(){
        Intent intentFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intentFoto.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intentFoto, Peticion_captura_imagen);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Peticion_captura_imagen && resultCode == RESULT_OK){
            /*Bundle extras = data.getExtras();
            Bitmap img = (Bitmap) extras.get("data");
            imageView.setImageBitmap(img);*/
            galleryAddPic();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        PathImagen = image.getAbsolutePath();
        return image;
    }

    private void TakePhotoDir() {
        Intent Intenttakephoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Intenttakephoto.resolveActivity(getPackageManager()) != null) {
            File foto = null;

            try {
                foto = createImageFile();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (foto != null) {
                Uri fotoUri = FileProvider.getUriForFile(this, "com.example.pm2e10179.fileprovider", foto);
                Intenttakephoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(Intenttakephoto, Peticion_captura_imagen);

            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(PathImagen);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}