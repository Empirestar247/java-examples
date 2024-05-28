import java.util.InputMismatchException;
import java.util.Scanner;

public class LibraryCatalogApp {
    private static GenericCatalog<LibraryItem> catalog = new GenericCatalog<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("Library Catalog Menu:");
            System.out.println("1. Add Item");
            System.out.println("2. Remove Item");
            System.out.println("3. View Item");
            System.out.println("4. View All Items");
            System.out.println("5. Exit");
            System.out.print("Select an option: ");

            int choice = getValidatedIntInput();

            switch (choice) {
                case 1:
                    addItem();
                    break;
                case 2:
                    removeItem();
                    break;
                case 3:
                    viewItem();
                    break;
                case 4:
                    viewAllItems();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static int getValidatedIntInput() {
        int choice = -1;
        boolean valid = false;

        while (!valid) {
            try {
                choice = scanner.nextInt();
                valid = true;
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // consume the invalid input
            }
        }

        return choice;
    }

    private static void addItem() {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();

        System.out.println("Library Catalog Menu:");
        System.out.println("1. Add Book");
        System.out.println("2. Add DVD");
        System.out.println("3. Add Magazine");
        System.out.print("Select an option: ");

        int choice = getValidatedIntInput();
        LibraryItem item;
        switch (choice) {
            case 1:
                item = new Book(title, author);
                break;
            case 2:
                item = new DVD(title, author);
                break;
            case 3:
                item = new Magazine(title, author);
                break;
            default:
                System.out.println("Invalid option.");
                return;
        }

        catalog.addItem(item);
        System.out.println("Item added successfully.");
    }

    private static void removeItem() {
        System.out.print("Enter item ID to remove: ");
        int itemID = getValidatedIntInput();

        boolean removed = catalog.removeItem(itemID);
        if (removed) {
            System.out.println("Item removed successfully.");
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void viewItem() {
        System.out.print("Enter item ID to view: ");
        int itemID = getValidatedIntInput();

        LibraryItem item = catalog.getItem(itemID);
        if (item != null) {
            System.out.println(item);
        } else {
            System.out.println("Item not found.");
        }
    }

    private static void viewAllItems() {
        for (LibraryItem item : catalog.getAllItems()) {
            System.out.println(item);
        }
    }
}
