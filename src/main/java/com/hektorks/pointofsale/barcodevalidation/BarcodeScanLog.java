package com.hektorks.pointofsale.barcodevalidation;

import java.util.LinkedList;
import java.util.Observable;

public class BarcodeScanLog extends Observable {

  private LinkedList<Barcode> scannedBarcodes;

  public void addBarcode(Barcode barcode) {
    if (this.scannedBarcodes == null) {
      this.scannedBarcodes = new LinkedList<>();
      this.scannedBarcodes.add(barcode);
    } else {
      this.scannedBarcodes.add(barcode);
    }
    setChanged();
    notifyObservers(this.scannedBarcodes.getLast());
  }

  public LinkedList<Barcode> getScannedBarcodes() {
    return scannedBarcodes;
  }
}
