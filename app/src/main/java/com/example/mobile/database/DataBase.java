package com.example.mobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.mobile.MyApp;

public class DataBase {

    private static final String DATA_BASE_LOG = "DATA_BASE";
    private static final String DATA_BASE_NAME = "projects-inf311";
    private static DataBase instance;
    protected SQLiteDatabase db;

    private DataBase() {
        final Context ctx = MyApp.getAppContext();
        this.db = ctx.openOrCreateDatabase(DATA_BASE_NAME, Context.MODE_PRIVATE, null);
        final Cursor c = this.search("sqlite_master", null, "type = 'table'", "");
        if (c.getCount() == 1) {
            final String[] scriptDataBaseCreate = new String[]{
                    """
                    CREATE TABLE Location (id INTEGER PRIMARY KEY AUTOINCREMENT,
                                           description TEXT,
                                           latitude REAL,
                                           longitude REAL);
                    """,
                    """
                    CREATE TABLE Logs (id INTEGER PRIMARY KEY AUTOINCREMENT,
                                       msg TEXT,
                                       timestamp TEXT,
                                       id_location INTEGER,
                                       CONSTRAINT fkey_location FOREIGN KEY (id_location) REFERENCES Location(id));
                    """,
                    "INSERT INTO Location (description, latitude, longitude) VALUES ('Minha casa na cidade natal', -19.4619317, -42.5872633);",
                    "INSERT INTO Location (description, latitude, longitude) VALUES ('Minha casa em Viçosa', -20.751637, -42.870514);",
                    "INSERT INTO Location (description, latitude, longitude) VALUES ('Meu departamento', -20.765000, -42.868430);",
            };
            for (final String s : scriptDataBaseCreate) {
                this.db.execSQL(s);
            }
            Log.i(DATA_BASE_LOG, "Criou tabelas do banco e as populou.");
        }
        c.close();
        Log.i(DATA_BASE_LOG, "Abriu conexão com o banco.");
    }

    public static DataBase getInstance() {
        if (instance == null)
            instance = new DataBase();
        instance.open();
        return instance;
    }

    public long insert(final String table, final ContentValues values) {
        final long id = this.db.insert(table, null, values);
        Log.i(DATA_BASE_LOG, "Cadastrou registro com o id [" + id + "]");
        return id;
    }

    public int update(final String table, final ContentValues values, final String where) {
        final int count = this.db.update(table, values, where, null);
        Log.i(DATA_BASE_LOG, "Atualizou [" + count + "] registros");
        return count;
    }

    public int delete(final String table, final String where) {
        final int count = this.db.delete(table, where, null);
        Log.i(DATA_BASE_LOG, "Deletou [" + count + "] registros");
        return count;
    }

    public Cursor search(final String table, final String[] columns, final String where, final String orderBy) {
        final Cursor c;
        if (!where.isEmpty())
            c = this.db.query(table, columns, where, null, null, null, orderBy);
        else
            c = this.db.query(table, columns, null, null, null, null, orderBy);
        Log.i(DATA_BASE_LOG, "Realizou uma busca e retornou [" + c.getCount() + "] registros.");
        return c;
    }

    private void open() {
        final Context ctx = MyApp.getAppContext();
        if (!this.db.isOpen()) {
            this.db = ctx.openOrCreateDatabase(DATA_BASE_NAME, Context.MODE_PRIVATE, null);
            Log.i(DATA_BASE_LOG, "Abriu conexão com o banco.");
        } else {
            Log.i(DATA_BASE_LOG, "Conexão com o banco já estava aberta.");
        }
    }

    public void close() {
        if (this.db != null && this.db.isOpen()) {
            this.db.close();
            Log.i(DATA_BASE_LOG, "Fechou conexão com o Banco.");
        }
    }
}
