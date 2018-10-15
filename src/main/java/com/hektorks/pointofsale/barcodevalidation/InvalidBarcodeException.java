package com.hektorks.pointofsale.barcodevalidation;

class InvalidBarcodeException extends RuntimeException {
  InvalidBarcodeException(String s) {
    super(s);
  }
}
