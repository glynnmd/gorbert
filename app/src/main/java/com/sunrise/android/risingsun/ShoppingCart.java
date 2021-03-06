package com.sunrise.android.risingsun;

import com.sunrise.android.risingsun.Coffee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dell on 12/1/2017.
 */

public class ShoppingCart
{
    private List<Coffee> order;
    private static ShoppingCart cartInstance = new ShoppingCart();

    private ShoppingCart()
    {
        order = new ArrayList<Coffee>();
    }

    public void add(Coffee coffee)
    {
        order.add(coffee);
    }

    public static ShoppingCart getInstance()
    {
        return cartInstance;
    }

    public void remove(Coffee coffee)
    {

        for (int i = 0; i < order.size(); i++)
        {
            if (coffee.getId() == order.get(i).getId())
            {
                order.remove(i);
            }
        }
    }

    public List<Coffee> getOrder()
    {
        return order;
    }

    public Coffee getCoffee(UUID id)
    {
        for (int i = 0; i < order.size(); i++)
        {
            if (order.get(i).getId() == id)
            {
                return order.get(i);
            }
        }

        return null;
    }

    public void updateCartItem(Coffee coffee)
    {
        for(Coffee element:order)
        {
            if (coffee.getId() == element.getId())
            {
                element.setSize(coffee.getSize());
                element.setAlmondMilk(coffee.getAlmondMilk());
                element.setWhippedCream(coffee.getWhippedCream());
                element.setEspressoShots(coffee.getEspressoShots());
                element.setCaramelShots(coffee.getCaramelShots());
                element.setChocolateShots(coffee.getChocolateShots());
                element.setHazelnutShots(coffee.getHazelnutShots());
                element.setVanillaShots(coffee.getVanillaShots());
            }
        }
    }

    public double total()
    {
        double total = 0;

        for (Coffee coffee : order)
        {
            total += coffee.cost();
        }

        return total;
    }


}
