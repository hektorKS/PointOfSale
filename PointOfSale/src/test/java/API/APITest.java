package API;

import AppLogic.Barcode;
import AppLogic.Product;
import Interfaces.BarcodeScanner;
import Interfaces.Database;
import Interfaces.DisplayPanel;
import Interfaces.Printer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.LinkedList;
import java.util.ListIterator;

import static org.mockito.Mockito.*;

public class APITest {

    @Mock
    private Database database;

    @Mock
    private BarcodeScanner barcodeScanner;

    @Mock
    private DisplayPanel displayPanel;

    @Mock
    private Printer printer;


    private Barcode b0;
    private Barcode b1;
    private Barcode b2;
    private Barcode b3;
    private Barcode b4;

    private void setUpDatabase(){
        database = mock(Database.class);

        b0 = new Barcode("000000000");
        Product p0 = new Product("Milk", b0, 3.0);
        when(database.getProduct(b0)).thenReturn(p0);

        b1 = new Barcode("111111111");
        Product p1 = new Product("Water", b1, 1.5);
        when(database.getProduct(b1)).thenReturn(p1);

        b2 = new Barcode("222222222");
        Product p2 = new Product("Eggs", b2, 7.5);
        when(database.getProduct(b2)).thenReturn(p2);

        b3 = new Barcode("333333333");
        when(database.getProduct(b3)).thenReturn(null);

        b4 = new Barcode("444444444");
        Product p4 = new Product("", b4, 12);
        when(database.getProduct(b4)).thenReturn(p4);
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

    @Before
    public void setUp() throws Exception {
        this.setUpDatabase();
        this.setUpBarcodeScanner();
        this.setUpDisplayPanel();
        this.setUpPrinter();
    }


    private void scanSingleProduct(PointOfSale api, Barcode barcode){
        when(barcodeScanner.scan()).thenReturn(barcode);
        api.scanProduct();
    }


    private void checkScanLogCorrectness(PointOfSale api, int expectedSize){
        LinkedList<Barcode> scannedBarcodes = api.getCurrentScanLog().getScannedBarcodes();
        if(scannedBarcodes != null) {
            Assert.assertEquals(scannedBarcodes.size(), expectedSize);

            System.out.println("Scanned barcodes: ");
            for (Barcode scannedBarcode : scannedBarcodes) {
                System.out.println(scannedBarcode.getBarcode());
            }
        }
    }


    private void checkCartCorrectness(PointOfSale api, int expectedSize){
        LinkedList<Product> products = api.getCurrentCart().getProducts();
        Assert.assertEquals(products.size(), expectedSize);

        if(expectedSize != 0) {
            System.out.println("Products in cart: ");
            for (Product product : products) {
                System.out.println(product.getBarcode().getBarcode() + " : " + product.getName());
            }
        }
    }


    private void checkPrinterUsageCorrectness(PointOfSale api){
        verify(printer, times(1)).printReceipt(api.getCurrentCart());
    }


    private void checkDisplayPanelUsageCorrectness(PointOfSale api){
        verify(displayPanel, times(2)).displayAlert(anyString());
        verify(displayPanel, times(3)).displayProduct((Product) anyObject());
        verify(displayPanel, times(1)).displaySum(anyDouble());
    }


    /**
     * Some simple tests, that verify application correctness.
     */
    @Test
    public void testApplication(){
        PointOfSale api = new PointOfSale(database, barcodeScanner, displayPanel, printer);

        scanSingleProduct(api, b0);

        scanSingleProduct(api, b1);

        scanSingleProduct(api, b2);

        scanSingleProduct(api, b3);

        scanSingleProduct(api, b4);

        double priceSum = api.getCurrentPriceSum();
        Assert.assertEquals(12.0, priceSum, 0.000001);

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