package com.example.proyectop1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText txtid;
    private EditText txtnombre;
    private EditText txtapellido;

    FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtid = findViewById(R.id.txtid);
        txtnombre = findViewById(R.id.txtnombre);
        txtapellido = findViewById(R.id.txtapellido);

        dbHelper = new FeedReaderDbHelper(this);
    }


    public void Listar(View vista) {
        Intent intent = new Intent(MainActivity.this, Listado.class);
        startActivity(intent);
    }



    public void Guardar(View vista) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());

        long newRowId = db.insert(FeedReaderContract.FeedEntry.nameTable, null, values);

        Toast.makeText(this,
                "Se guardó el registro con clave: " + newRowId,
                Toast.LENGTH_LONG).show();

        db.close();
    }


    public void Buscar(View vista) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.column1,
                FeedReaderContract.FeedEntry.column2
        };

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.nameTable,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            String nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column1));

            String apellido = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.column2));

            txtnombre.setText(nombre);
            txtapellido.setText(apellido);

        } else {
            Toast.makeText(this, "No se encontró el registro", Toast.LENGTH_LONG).show();
        }

        cursor.close();
        db.close();
    }

    // ✅ ELIMINAR
    public void Eliminar(View vista) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        int deletedRows = db.delete(
                FeedReaderContract.FeedEntry.nameTable,
                selection,
                selectionArgs
        );

        Toast.makeText(this,
                "Se eliminó " + deletedRows + " registro(s)",
                Toast.LENGTH_LONG).show();

        db.close();
    }

    // ✅ ACTUALIZAR
    public void Actualizar(View vista) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.column1, txtnombre.getText().toString());
        values.put(FeedReaderContract.FeedEntry.column2, txtapellido.getText().toString());

        String selection = BaseColumns._ID + " = ?";
        String[] selectionArgs = { txtid.getText().toString() };

        int count = db.update(
                FeedReaderContract.FeedEntry.nameTable,
                values,
                selection,
                selectionArgs
        );

        Toast.makeText(this,
                "Se actualizó " + count + " registro(s)",
                Toast.LENGTH_LONG).show();

        db.close();
    }
}

