package com.abcontenidos.www.redhost.Dbases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.abcontenidos.www.redhost.Objets.Post;

import java.util.ArrayList;

public class PostDao {

    private SQLiteDatabase db;
    private SQLiteStatement statementSave;
    private String tableName = "posts";

    public PostDao(SQLiteDatabase db)
    {
        this.db=db;
        //statementSave = db.compileStatement("INSERT INTO personas (nombre,edad) VALUES(?,?)");
    }

    public Post get(int id)
    {
        Cursor c;
        Post post = null;
        c = db.rawQuery("" +
                "SELECT " +
                    "p.id, " +
                    "p.name, " +
                    "p.details, " +
                    "p.category, " +
                    "p.image, " +
                    "p.commerce," +
                    "p.address," +
                    "p.phone" +
                " FROM posts as p " +
                "WHERE id="+id,null);

        if(c.moveToFirst())
        {
            post = new Post();
            post.setId(c.getString(0));
            post.setName(c.getString(1));
            post.setDetails(c.getString(2));
            post.setCategory(c.getString(3));
            post.setImage(c.getString(4));
            post.setCommerce(c.getString(5));
            post.setAddresscommece(c.getString(6));
            post.setCelcommerce(c.getString(7));
        }
        c.close();
        return post;
    }

    public ArrayList<Post> getall()
    {
        Cursor c;
        ArrayList<Post> list = new ArrayList<>();
        c = db.rawQuery("SELECT * FROM posts WHERE 1",null);

        if(c.moveToFirst())
        {
            do{
                Post post = new Post();
                post.setId(c.getString(0));
                post.setName(c.getString(1));
                post.setDetails(c.getString(2));
                post.setCategory(c.getString(3));
                post.setImage(c.getString(4));
                post.setCommerce(c.getString(5));
                list.add(post);
            }while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long save(Post post)
    {
        //statementSave.clearBindings();
        //statementSave.bindString(1, category.getName());
        //statementSave.bindString(2, category.getDetails());
        //return statementSave.executeInsert();
        ContentValues values = new ContentValues();
        //values.put("id", post.getId());
        values.put("name", post.getName());
        values.put("details", post.getDetails());
        values.put("image", post.getImage());
        values.put("category", post.getCategory());
        values.put("commerce", post.getCommerce());
        values.put("address", post.getAddresscommece());
        values.put("phone", post.getCelcommerce());
        return  db.insert(tableName, null, values);
    }

    public void update(Post post)
    {
        ContentValues values = new ContentValues();
        values.put("id", post.getId());
        values.put("name", post.getName());
        values.put("details", post.getDetails());
        values.put("category", post.getCategory());
        values.put("image", post.getImage());
        values.put("commerce", post.getCommerce());
        db.update("posts", values, "id="+post.getId(), null);
    }

    public void delete()
    {
        db.delete("posts",null, null);
    }

    public void clear()
    {
        db.delete("posts",null, null);
    }
}

