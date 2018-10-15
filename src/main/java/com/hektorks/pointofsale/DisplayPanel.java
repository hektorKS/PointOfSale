package com.hektorks.pointofsale;

public interface DisplayPanel {

  void displayAlert(String alert);

  void displayProduct(Product product);

  void displaySum(double sum);

  void clearPanel();

}
