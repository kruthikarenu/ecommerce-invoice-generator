
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 1. Shippable Interface
interface Shippable {

    double calculateShippingCost();

    String generateTrackingId();
}

// 2. Abstract MarketplaceProduct Class
abstract class MarketplaceProduct {

    protected String sku;
    protected String title;
    protected double vendorPrice;
    protected String vendorName;

    // Base class constructor
    public MarketplaceProduct(String sku, String title,
                              double vendorPrice, String vendorName) {

        this.sku = sku;
        this.title = title;
        this.vendorPrice = vendorPrice;
        this.vendorName = vendorName;
    }

    // Abstract tax calculation method
    public abstract double calculateFinalTax();

    public String getSKU() {
        return sku;
    }

    public String getTitle() {
        return title;
    }

    public double getVendorPrice() {
        return vendorPrice;
    }

    public String getVendorName() {
        return vendorName;
    }

    public abstract String getProductType();
}

// 3. Physical Product Class
class PhysicalProduct extends MarketplaceProduct
        implements Shippable {

    private double weight;
    private double shippingCost;
    private double gstRate;

    public PhysicalProduct(String sku, String title,
                           double vendorPrice, String vendorName,
                           double weight, double shippingCost,
                           double gstRate) {

        super(sku, title, vendorPrice, vendorName);

        this.weight = weight;
        this.shippingCost = shippingCost;
        this.gstRate = gstRate;
    }

    @Override
    public double calculateFinalTax() {
        return vendorPrice * gstRate / 100;
    }

    @Override
    public double calculateShippingCost() {
        return shippingCost;
    }

    @Override
    public String generateTrackingId() {
        return "PHY-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String getProductType() {
        return "Physical Product";
    }

    public double getWeight() {
        return weight;
    }
}

// 4. Digital License Class
class DigitalLicense extends MarketplaceProduct {

    private String licenseKey;
    private double gstRate;

    public DigitalLicense(String sku, String title,
                          double vendorPrice, String vendorName,
                          double gstRate) {

        super(sku, title, vendorPrice, vendorName);

        this.gstRate = gstRate;

        this.licenseKey = "KEY-" + UUID.randomUUID()
                .toString().substring(0, 12).toUpperCase();
    }

    @Override
    public double calculateFinalTax() {
        return vendorPrice * gstRate / 100;
    }

    @Override
    public String getProductType() {
        return "Digital License";
    }

    public String getLicenseKey() {
        return licenseKey;
    }
}

// 5. Perishable Food Item Class
class PerishableFoodItem extends MarketplaceProduct
        implements Shippable {

    private String expiryDate;
    private double shippingCost;
    private double gstRate;

    public PerishableFoodItem(String sku, String title,
                              double vendorPrice, String vendorName,
                              String expiryDate, double shippingCost,
                              double gstRate) {

        super(sku, title, vendorPrice, vendorName);

        this.expiryDate = expiryDate;
        this.shippingCost = shippingCost;
        this.gstRate = gstRate;
    }

    @Override
    public double calculateFinalTax() {
        return vendorPrice * gstRate / 100;
    }

    @Override
    public double calculateShippingCost() {
        return shippingCost;
    }

    @Override
    public String generateTrackingId() {
        return "FOOD-" + UUID.randomUUID()
                .toString().substring(0, 8).toUpperCase();
    }

    @Override
    public String getProductType() {
        return "Perishable Food Item";
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}

// 6. Invoice Generator Class
class InvoiceGenerator {

    public void generateInvoice(List<MarketplaceProduct> cart) {

        double subtotal = 0;
        double totalTax = 0;
        double totalShipping = 0;

        System.out.println();
        System.out.println("==================================================");
        System.out.println("       MULTI-VENDOR E-COMMERCE INVOICE");
        System.out.println("==================================================");

        for (MarketplaceProduct product : cart) {

            double price = product.getVendorPrice();

            // Runtime polymorphism
            double tax = product.calculateFinalTax();

            double shipping = 0;

            System.out.println();
            System.out.println("Product Name : " + product.getTitle());
            System.out.println("Product Type : " + product.getProductType());
            System.out.println("SKU          : " + product.getSKU());
            System.out.println("Vendor       : " + product.getVendorName());

            // Check shipping requirements
            if (product instanceof Shippable) {

                Shippable item = (Shippable) product;

                shipping = item.calculateShippingCost();

                System.out.println("Tracking ID  : "
                        + item.generateTrackingId());

            } else {

                System.out.println("Tracking ID  : Not Required");
                System.out.println("Delivery     : Electronic Delivery");
            }

            System.out.printf("Base Price   : Rs. %.2f%n", price);
            System.out.printf("GST Amount   : Rs. %.2f%n", tax);
            System.out.printf("Shipping     : Rs. %.2f%n", shipping);

            double itemTotal = price + tax + shipping;

            System.out.printf("Item Total   : Rs. %.2f%n", itemTotal);

            // Display digital license key
            if (product instanceof DigitalLicense) {

                DigitalLicense digital =
                        (DigitalLicense) product;

                System.out.println("License Key  : "
                        + digital.getLicenseKey());
            }

            // Display expiry date for food items
            if (product instanceof PerishableFoodItem) {

                PerishableFoodItem food =
                        (PerishableFoodItem) product;

                System.out.println("Expiry Date  : "
                        + food.getExpiryDate());
            }

            subtotal += price;
            totalTax += tax;
            totalShipping += shipping;

            System.out.println("--------------------------------------------------");
        }

        // Calculate grand total
        double grandTotal = subtotal + totalTax + totalShipping;

        System.out.println();
        System.out.println("              INVOICE SUMMARY");
        System.out.println("==================================================");

        System.out.printf("Subtotal       : Rs. %.2f%n", subtotal);
        System.out.printf("Total GST      : Rs. %.2f%n", totalTax);
        System.out.printf("Total Shipping : Rs. %.2f%n", totalShipping);

        System.out.println("--------------------------------------------------");

        System.out.printf("GRAND TOTAL    : Rs. %.2f%n", grandTotal);

        System.out.println("==================================================");
        System.out.println("       THANK YOU FOR SHOPPING WITH US!");
        System.out.println("==================================================");
    }
}

// 7. Main Class
public class Hello {

    public static void main(String[] args) {

        System.out.println("WELCOME TO OUR E-COMMERCE MARKETPLACE");

        // Create Physical Product
        MarketplaceProduct mouse = new PhysicalProduct(
                "PHY101",
                "Wireless Mouse",
                1200.00,
                "Tech World",
                0.25,
                50.00,
                18.00
        );

        // Create Digital License
        MarketplaceProduct windowsKey = new DigitalLicense(
                "DIG201",
                "Windows 11 Key",
                4500.00,
                "Software Hub",
                18.00
        );

        // Create Perishable Food Item
        MarketplaceProduct groceries = new PerishableFoodItem(
                "FOOD301",
                "Fresh Grocery Pack",
                500.00,
                "Fresh Mart",
                "10-11-2026",
                30.00,
                5.00
        );

        // Create a mixed shopping cart
        List<MarketplaceProduct> cart = new ArrayList<>();

        cart.add(mouse);
        cart.add(windowsKey);
        cart.add(groceries);

        // Generate unified invoice
        InvoiceGenerator invoice = new InvoiceGenerator();

        invoice.generateInvoice(cart);
    }
}
