package API;

import AppLogic.Barcode;
import AppLogic.BarcodeScanLog;
import AppLogic.BarcodeValidator;
import AppLogic.Cart;
import Interfaces.BarcodeScanner;
import Interfaces.Database;
import Interfaces.DisplayPanel;
import Interfaces.Printer;

public class PointOfSale {

    private Database database;
    private BarcodeScanner barcodeScanner;
    private DisplayPanel displayPanel;
    private Printer printer;

    private BarcodeScanLog barcodeScanLog;
    private BarcodeValidator barcodeValidator;
    private Cart cart;

    public PointOfSale(Database database, BarcodeScanner barcodeScanner, DisplayPanel displayPanel, Printer printer) {
        this.database = database;
        this.barcodeScanner = barcodeScanner;
        this.displayPanel = displayPanel;
        this.printer = printer;

        this.cart = new Cart();

        this.barcodeScanLog = new BarcodeScanLog();
        this.barcodeValidator = new BarcodeValidator(this.database, this.displayPanel, this.cart);

        this.barcodeScanLog.addObserver(this.barcodeValidator);

    }

    public void scanProduct() {
        Barcode barcode = this.barcodeScanner.scan();
        barcodeScanLog.addBarcode(barcode);
    }

    public void end(){
        displayPanel.displaySum(cart.getPriceSum());
        printer.printReceipt(this.cart);

    }

    public double getCurrentPriceSum(){
        return cart.getPriceSum();
    }

    public BarcodeScanLog getCurrentScanLog(){
        return barcodeScanLog;
    }

    public Cart getCurrentCart(){
        return cart;
    }

    /**
     * We could also add here some kind of transactions history saving.
     * But it is not in task requirements, so i don't implement it.
     */
    public void beginNewTransaction(){
        this.displayPanel.clearPanel();
        this.cart = new Cart();
        this.barcodeScanLog = new BarcodeScanLog();

        this.barcodeScanLog.addObserver(this.barcodeValidator);
    }
}
