package AppLogic;

public class Product {
    private String name;
    private Barcode barcode;
    private double price;

    public Product(String name, Barcode barcode, double price) {
        this.name = name;
        this.barcode = barcode;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Barcode getBarcode() {
        return barcode;
    }

    public double getPrice() {
        return price;
    }
}
