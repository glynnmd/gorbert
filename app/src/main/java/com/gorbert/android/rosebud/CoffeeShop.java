package com.gorbert.android.rosebud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gorbert.android.rosebud.Coffee;
import com.gorbert.android.rosebud.database.CoffeeBaseHelper;
import com.gorbert.android.rosebud.database.CoffeeCursorWrapper;
import com.gorbert.android.rosebud.database.CoffeeDbSchema.CoffeeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dell on 11/26/2017.
 */

public class CoffeeShop
{





    private static CoffeeShop sCoffeeShop;

    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static CoffeeShop get(Context context)
    {
        if (sCoffeeShop == null)
        {
            sCoffeeShop = new CoffeeShop(context);
        }

        return sCoffeeShop;
    }

    private CoffeeShop(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new CoffeeBaseHelper(mContext)
                .getWritableDatabase();

        if (getCoffees().size() == 0)
        {
            Coffee coffee = new Coffee();
            //sulawesi

            coffee.setDrinkType(Coffee.SULAWESI);
            coffee.setSize(Coffee.LARGE);
            coffee.setTitle("");
            coffee.setDescription("");
            addCoffee(coffee);

            //columbian
            coffee = new Coffee();
            coffee.setDrinkType(Coffee.COLUMBIAN);
            coffee.setSize(Coffee.LARGE);
            coffee.setTitle("");
            coffee.setDescription("");
            addCoffee(coffee);

            addCoffee(new ColumbianBlend(2));
            //americano
            addCoffee(new Americano(2));
            //chai latte
            addCoffee(new ChaiLatte(2));
            //cappuccino
            addCoffee(new Cappuccino(new ColumbianBlend(2)));
            //latte
            addCoffee(new Latte(new ColumbianBlend(2)));
            //hot chocolate
            addCoffee(new HotChocolate(2));
        }
    }

    public void addCoffee(Coffee c)
    {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CoffeeTable.NAME, null, values);
    }

    public List<Coffee> getCoffees()
    {
        List<Coffee> coffees = new ArrayList<>();

        CoffeeCursorWrapper cursor = queryCoffees(null, null);

        try
        {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                coffees.add(cursor.getCoffee());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }

        return coffees;
    }

    public Coffee getCoffee(UUID id)
    {
        CoffeeCursorWrapper cursor = queryCoffees(
                CoffeeTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try
        {
            if (cursor.getCount() == 0)
            {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCoffee();
        }
        finally
        {
            cursor.close();
        }
    }

    public void updateCoffee(Coffee coffee)
    {
        String uuidString = coffee.getId().toString();
        ContentValues values = getContentValues(coffee);

        mDatabase.update(CoffeeTable.NAME, values,
                CoffeeTable.Cols.UUID + " = ?",
                new String[] {uuidString});
    }

    private CoffeeCursorWrapper queryCoffees(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                CoffeeTable.NAME,
                null, //columns - null selects all columns
                whereClause,
                whereArgs,
                null, //groupBy
                null, //having
                null //orderBy
        );
        return new CoffeeCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Coffee coffee)
    {
        ContentValues values = new ContentValues();
        values.put(CoffeeTable.Cols.UUID, coffee.getId().toString());
        values.put(CoffeeTable.Cols.TITLE, coffee.getTitle());
        values.put(CoffeeTable.Cols.DESCRIPTION, coffee.getDescription());
        //values.put(CoffeeTable.Cols.ORDER, coffee.getOrder());
        values.put(CoffeeTable.Cols.FAVORITED, coffee.isFavorited() ? 1 : 0);

        return values;
    }
}
