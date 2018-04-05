package Interfaces;

import AppLogic.Barcode;
import AppLogic.Product;

public interface Database {
    Product getProduct(Barcode barcode);
}
