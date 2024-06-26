import java.util.ArrayList;
import java.util.Scanner;

class Item {
    String name;
    double price;
    int quantity;

    Item(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    double getTotalPrice() {
        return price * quantity;
    }
}

 class SupermarketBillingSystem1 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Item> itemList = new ArrayList<>();
        String choice;

        do {
            System.out.print("Enter item name: ");
            String name = scanner.nextLine();

            double price = 0;
            boolean validPrice = false;
            while (!validPrice) {
                try {
                    System.out.print("Enter item price: ");
                    price = Double.parseDouble(scanner.nextLine());
                    validPrice = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid price.");
                }
            }

            int quantity = 0;
            boolean validQuantity = false;
            while (!validQuantity) {
                try {
                    System.out.print("Enter item quantity: ");
                    quantity = Integer.parseInt(scanner.nextLine());
                    validQuantity = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid quantity.");
                }
            }

            itemList.add(new Item(name, price, quantity));

            System.out.print("Do you want to add another item? (yes/no): ");
            choice = scanner.nextLine().trim(); // Trim whitespace

        } while (choice.equalsIgnoreCase("yes"));

        double totalBill = 0;
        System.out.println("\nItem List:");
        for (Item item : itemList) {
            System.out.printf("%s - %d @ $%.2f each. Total: $%.2f\n",
                    item.name, item.quantity, item.price, item.getTotalPrice());
            totalBill += item.getTotalPrice();
        }

        System.out.printf("\nTotal Bill: $%.2f\n", totalBill);
    }
}