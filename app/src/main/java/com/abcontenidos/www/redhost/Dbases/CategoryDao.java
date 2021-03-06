package com.abcontenidos.www.redhost.Dbases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.abcontenidos.www.redhost.Objets.Category;

import java.util.ArrayList;

public class CategoryDao {

    private SQLiteDatabase db;
    private SQLiteStatement statementSave;
    private String tableName = "categories";

    public CategoryDao(SQLiteDatabase db)
    {
        this.db=db;
        //statementSave = db.compileStatement("INSERT INTO personas (nombre,edad) VALUES(?,?)");
    }

    public Category get(int id)
    {
        Cursor c;
        Category category = null;
        c = db.rawQuery("SELECT id_database,nombre,edad" +
                " FROM personas WHERE id=" +id,null);

        if(c.moveToFirst())
        {
            category = new Category();
            category.setId(c.getInt(0));
            category.setName(c.getString(1));
            category.setDetails(c.getString(2));
            category.setImage(c.getString(3));
            category.setSelected(c.getString(4));
        }
        c.close();
        return category;
    }

    public ArrayList<Category> getall()
    {
        Cursor c;
        ArrayList<Category> list = new ArrayList<>();
        Category category = null;
        c = db.rawQuery("SELECT id_database, name, details, selected, image FROM categories WHERE 1",null);

        if(c.moveToFirst())
        {
            do{
                category = new Category();
                category.setId(c.getInt(0));
                category.setName(c.getString(1));
                category.setDetails(c.getString(2));
                category.setSelected(c.getString(3));
                category.setImage(c.getString(4));
                list.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long save(Category category)
    {
        //statementSave.clearBindings();
        //statementSave.bindString(1, category.getName());
        //statementSave.bindString(2, category.getDetails());
        //return statementSave.executeInsert();
        ContentValues values = new ContentValues();
        values.put("id_database", category.getId());
        values.put("name", category.getName());
        values.put("details", category.getDetails());
        values.put("image", category.getImage());
        values.put("selected", category.getSelected());
        return  db.insert(tableName, null, values);
    }

    public void update(Category category)
    {
        ContentValues values = new ContentValues();
        values.put("id_database", category.getId());
        values.put("name", category.getName());
        values.put("details", category.getDetails());
        values.put("image", category.getImage());
        values.put("selected", category.getSelected());

        db.update("categories", values, "id="+category.getId(), null);
    }

    public void delete(Category category)
    {
        db.delete("categories","_id="+category.getId(), null);
    }
}

