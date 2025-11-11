package com.example.proyectop1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.TableLayout;

import java.util.ArrayList;

public class Listado extends AppCompatActivity {

    private TableLayout tblListado;
    private String[] cabecera = {"Id", "Nombre", "Apellido"};
    private ArrayList<String[]> datos = new ArrayList<>();
    private DynamicTable creaTabla;

    FeedReaderDbHelper dbHelper;  // ✅ SOLO DECLARACIÓN

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        tblListado = findViewById(R.id.tblistado);

        dbHelper = new FeedReaderDbHelper(this);   // ✅ AHORA SÍ OK

        creaTabla = new DynamicTable(tblListado, getApplicationContext());
        creaTabla.setCabecera(cabecera);

        TraerDatos();
        creaTabla.setDatos(datos);
        creaTabla.crearFilas();
    }

    private void TraerDatos() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                null,
                null,
                null,
                null,
                FeedReaderContract.FeedEntry.column2 + " ASC"
        );

        while (cursor.moveToNext()) {

            String[] fila = new String[3];

            fila[0] = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID)) + "";
            fila[1] = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));
            fila[2] = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));

            datos.add(fila);
        }

        cursor.close();
        db.close();
    }

    public void Regresar(View vista) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
