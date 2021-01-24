package com.danielcirilo.preferenciasficheros;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity {
    private Button btEscribirInterno;
    private Button btLeerInterno;
    private Button btEscribirExterno;
    private Button btLeerExterno;
    private Toolbar toolbar;
    private Intent intent;
    private final int REQUEST_WRITE_SD = 2;
    private final int REQUEST_LECTURA_SD = 4;
    private View.OnClickListener myListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.appbar);
        btEscribirInterno = findViewById(R.id.btEscribirInterna);
        btLeerInterno = findViewById(R.id.btLeerInterna);
        btEscribirExterno = findViewById(R.id.btEscribirExterna);
        btLeerExterno = findViewById(R.id.btLeerExterna);
        setSupportActionBar(toolbar);
        SharedPreferences preferences = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btEscribirInterna:
                        escrituraMemoriaInterna();
                        break;
                    case R.id.btLeerInterna:
                        leerArchivoInterno();
                        break;
                    case R.id.btEscribirExterna:
                        escrituraTarjetaSD();
                        break;
                    case R.id.btLeerExterna:
                        lecturaMemoriaExterna();
                        break;
                }
            }
        };
        btEscribirInterno.setOnClickListener(myListener);
        btLeerInterno.setOnClickListener(myListener);
        btEscribirExterno.setOnClickListener(myListener);
        btLeerExterno.setOnClickListener(myListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_preferencias : {
                intent = new Intent(this, PreferencesActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public void escrituraMemoriaInterna(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nomArchivo = sharedPreferences.getString("NombreMemoriaInterna","default");
        try{
            OutputStreamWriter fout = new OutputStreamWriter(openFileOutput(nomArchivo+".txt",Context.MODE_PRIVATE));
            fout.write("Lectura desde memoria Interna");
            fout.close();

        }catch (Exception e){
            Log.e("Ficheros","Error al escribir fichero a memoria interna");
        }
    }

    public void leerArchivoInterno(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nomArchivo = sharedPreferences.getString("NombreMemoriaInterna","default");
        try{
            BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(nomArchivo+".txt")));
            String texto = fin.readLine();
            fin.close();
            Toast.makeText(this,texto,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Log.e("Ficheros", "Error al leer el fichero desde la memoria interna");


        }
    }
    public void escrituraTarjetaSD(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nomArchivo = sharedPreferences.getString("NombreMemoriaExterna","default");
        String rutaArchivo = sharedPreferences.getString("rutaMemoriaExterna","default");
        int permision = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(permision!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_SD);
            }else{

                try{
                    File d = new File(getExternalFilesDir(rutaArchivo),nomArchivo);
                    if(!d.exists()){
                        d.createNewFile();
                    }
                    OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(d+".txt"));
                    BufferedWriter bw = new BufferedWriter(fout);
                    bw.write("Esta es la memoria externa");
                    bw.close();
                }catch (Exception e){
                    Log.e("Ficheros", "Error al escribir el fichero desde la memoria externa");


                }
            }


        }

    }
    public void lecturaMemoriaExterna(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nomArchivo = sharedPreferences.getString("NombreMemoriaExterna","default");
        String rutaArchivo = sharedPreferences.getString("rutaMemoriaExterna","default");
        int permision = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(permision!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_LECTURA_SD);
            }else{

                try{
                    File d = new File(getExternalFilesDir(rutaArchivo),nomArchivo );
                    if(!d.exists()){
                        Toast.makeText(this,"No existe el archivo",Toast.LENGTH_LONG).show();
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(d+ ".txt")));
                    String linea;
                    while((linea = br.readLine ()) != null){
                        Toast.makeText(this,linea,Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Log.e("Ficheros", "Error al escribir el fichero desde la memoria externa");


                }
            }


        }

    }

}