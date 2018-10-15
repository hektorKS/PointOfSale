package com.hektorks.pointofsale;


import com.hektorks.pointofsale.barcodevalidation.Barcode;
import com.hektorks.pointofsale.barcodevalidation.BarcodeScanner;
import java.util.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class APITest {

  @Mock
  private Database database;

  @Mock
  private BarcodeScanner barcodeScanner;

  @Mock
  private DisplayPanel displayPanel;

  @Mock
  private Printer printer;


  private Barcode barcode0;
  private Barcode barcode1;
  private Barcode barcode2;
  private Barcode barcode3;
  private Barcode barcode4;

  private void setUpDatabase() {
    database = mock(Database.class);

    barcode0 = new Barcode("000000000");
    Product product0 = new Product("Milk", barcode0, 3.0);
    when(database.getProduct(barcode0)).thenReturn(product0);

    barcode1 = new Barcode("111111111");
    Product product1 = new Product("Water", barcode1, 1.5);
    when(database.getProduct(barcode1)).thenReturn(product1);

    barcode2 = new Barcode("222222222");
    Product product2 = new Product("Eggs", barcode2, 7.5);
    when(database.getProduct(barcode2)).thenReturn(product2);

    barcode3 = new Barcode("333333333");
    when(database.getProduct(barcode3)).thenReturn(null);

    barcode4 = new Barcode("444444444");
    Product product4 = new Product("", barcode4, 12);
    when(database.getProduct(barcode4)).thenReturn(product4);
  }

  private void setUpBarcodeScanner() {
    barcodeScanner = mock(BarcodeScanner.class);
  }

  private void setUpDisplayPanel() {
    displayPanel = mock(DisplayPanel.class);
  }

  private void setUpPrinter() {
    printer = mock(Printer.class);
  }

  @BeforeEach
  void setUp() {
    this.setUpDatabase();
    this.setUpBarcodeScanner();
    this.setUpDisplayPanel();
    this.setUpPrinter();
  }


  private void scanSingleProduct(PointOfSale api, Barcode barcode) {
    when(barcodeScanner.scan()).thenReturn(barcode);
    api.scanProduct();
  }


  private void checkScanLogCorrectness(PointOfSale api, int expectedSize) {
    LinkedList<Barcode> scannedBarcodes = api.getCurrentScanLog().getScannedBarcodes();
    if (scannedBarcodes != null) {
      Assertions.assertEquals(scannedBarcodes.size(), expectedSize);

      System.out.println("Scanned barcodes: ");
      for (Barcode scannedBarcode : scannedBarcodes) {
        System.out.println(scannedBarcode.getBarcode());
      }
    }
  }


  private void checkCartCorrectness(PointOfSale api, int expectedSize) {
    LinkedList<Product> products = api.getCurrentCart().getProducts();
    Assertions.assertEquals(products.size(), expectedSize);

    if (expectedSize != 0) {
      System.out.println("Products in cart: ");
      for (Product product : products) {
        System.out.println(product.getBarcode().getBarcode() + " : " + product.getName());
      }
    }
  }


  private void checkPrinterUsageCorrectness(PointOfSale api) {
    verify(printer, times(1)).printReceipt(api.getCurrentCart());
  }


  private void checkDisplayPanelUsageCorrectness(PointOfSale api) {
    verify(displayPanel, times(2)).displayAlert(anyString());
    verify(displayPanel, times(3)).displayProduct((Product) anyObject());
    verify(displayPanel, times(1)).displaySum(anyDouble());
  }


  /**
   * Some simple tests, that verify application correctness.
   */
  @Test
  void testApplication() {
    PointOfSale api = new PointOfSale(database, barcodeScanner, displayPanel, printer);

    scanSingleProduct(api, barcode0);

    scanSingleProduct(api, barcode1);

    scanSingleProduct(api, barcode2);

    scanSingleProduct(api, barcode3);

    scanSingleProduct(api, barcode4);

    double priceSum = api.getCurrentPriceSum();
    Assertions.assertEquals(12.0, priceSum, 0.000001);

    checkScanLogCorrectness(api, 5);
    checkCartCorrectness(api, 3);

    api.end();
    checkPrinterUsageCorrectness(api);
    checkDisplayPanelUsageCorrectness(api);

    api.beginNewTransaction();
    checkScanLogCorrectness(api, 0);
    checkCartCorrectness(api, 0);
  }
}