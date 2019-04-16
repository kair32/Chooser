package com.AKS.chooser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import static com.AKS.chooser.GLOBAL.HISTORYVARIANT;
import static com.AKS.chooser.GLOBAL.DBHEALPER;

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, 4
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mytable ("
                + "id integer primary key autoincrement,"
                + "text text,"
                + "headline blob,"
                + "variant blob,"
                + "uriimage text" + ");");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    public void DBread(){
        List<Variant> variant;
        variant = new ArrayList<>();
        SQLiteDatabase db = DBHEALPER.getWritableDatabase();
        String str = null;
        if(HISTORYVARIANT!=null) HISTORYVARIANT.clear();
        else HISTORYVARIANT = new ArrayList<>();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int nameColIndex = c.getColumnIndex("text");
            int headlineColIndex = c.getColumnIndex("headline");
            int variantColIndex = c.getColumnIndex("variant");
            int uriimageColIndex = c.getColumnIndex("uriimage");
            do {
                boolean var = false;
                if(c.getString(headlineColIndex).equals("1")){
                    if( str != null) {
                        HISTORYVARIANT.add(new VariantHistory(str, variant));
                        str = c.getString(nameColIndex);
                        variant.clear();
                    }
                    str = c.getString(nameColIndex);
                }
                    else {
                        if(c.getString(variantColIndex).equals("1")) var = true;
                        variant.add(new Variant("",c.getString(nameColIndex),var));
                        if (!c.getString(uriimageColIndex).equals("null")) {
                            variant.get(variant.size() - 1).UriImage = Uri.parse(c.getString((uriimageColIndex)));
                        }
                    }
            } while (c.moveToNext());
            HISTORYVARIANT.add(new VariantHistory(str, variant));
        } else
        c.close();
        DBHEALPER.close();
    }
    public void DBclear(){
        SQLiteDatabase db = DBHEALPER.getWritableDatabase();
        db.delete("mytable", null, null);
        DBHEALPER.close();
    }
    public void DB_add(List<Variant> variants, String headline){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = DBHEALPER.getWritableDatabase();
        if(variants!=null) {
                cv.put("text", headline);
                cv.put("headline", 1);
                cv.put("variant", 0);
                cv.put("uriimage", "null");
                db.insert("mytable", null, cv);
                for (int i = 0; i < variants.size(); i++) {
                    cv.put("text", variants.get(i).text);
                    cv.put("headline", 0);
                    cv.put("variant", variants.get(i).variant);
                    if(variants.get(i).UriImage == null)
                        cv.put("uriimage", "null");
                        else cv.put("uriimage", variants.get(i).UriImage.toString());
                    db.insert("mytable", null, cv);
                }
        }
        DBHEALPER.close();
    }
}