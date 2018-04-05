package AppLogic;

import java.util.LinkedList;

public class Cart {

    private LinkedList<Product> products;
    private double priceSum;

    {
        products = new LinkedList<>();
        this.priceSum = 0.0;
    }

    public void addProduct(Product product){
        products.add(product);
        priceSum += product.getPrice();
    }

    public double getPriceSum() {
        return priceSum;
    }

    public LinkedList<Product> getProducts() {
        return products;
    }
}
