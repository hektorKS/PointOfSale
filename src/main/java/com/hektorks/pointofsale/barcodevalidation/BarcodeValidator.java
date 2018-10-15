package com.hektorks.pointofsale.barcodevalidation;

import com.hektorks.pointofsale.Cart;
import com.hektorks.pointofsale.Database;
import com.hektorks.pointofsale.DisplayPanel;
import com.hektorks.pointofsale.Product;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class BarcodeValidator implements Observer {

  private Database database;
  private DisplayPanel displayPanel;
  private Cart cart;

  public BarcodeValidator(Database database, DisplayPanel displayPanel, Cart cart) {
    this.database = database;
    this.displayPanel = displayPanel;
    this.cart = cart;
  }

  @Override
  public void update(Observable observable, Object arg) {
    Barcode scannedProduct = (Barcode) arg;
    try {
      Product product = this.validate(scannedProduct);
      cart.addProduct(product);
      displayPanel.displayProduct(product);

    } catch (InvalidBarcodeException | ProductNotFoundException e) {
      this.displayPanel.displayAlert(e.getMessage());
    }
  }

  private Product validate(Barcode barcode) throws ProductNotFoundException, InvalidBarcodeException {
    Product product = this.database.getProduct(barcode);
    if (product == null) {
      throw new ProductNotFoundException("Product not found!");
    } else if (Objects.equals(product.getName(), "")) {
      throw new InvalidBarcodeException("Invalid bar-code!");
    }
    return product;
  }
}
