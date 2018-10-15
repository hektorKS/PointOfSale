package com.hektorks.pointofsale.barcodevalidation;

class ProductNotFoundException extends RuntimeException {
  ProductNotFoundException(String s) {
    super(s);
  }
}
