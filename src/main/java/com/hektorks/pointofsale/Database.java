package com.hektorks.pointofsale;

import com.hektorks.pointofsale.barcodevalidation.Barcode;

public interface Database {
  Product getProduct(Barcode barcode);
}
