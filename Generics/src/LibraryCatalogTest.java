public class LibraryCatalogTest {
    public static void main(String[] args) {
        GenericCatalog<LibraryItem> catalog = new GenericCatalog<>();

        LibraryItem book = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        LibraryItem dvd = new DVD("Inception", "Christopher Nolan");
        LibraryItem magazine = new Magazine("National Geographic", "Various");

        catalog.addItem(book);
        catalog.addItem(dvd);
        catalog.addItem(magazine);

        System.out.println("All items:");
        for (LibraryItem item : catalog.getAllItems()) {
            System.out.println(item);
        }

        System.out.println("\nRemoving item with ID 1:");
        catalog.removeItem(1);

        System.out.println("All items after removal:");
        for (LibraryItem item : catalog.getAllItems()) {
            System.out.println(item);
        }

        System.out.println("\nViewing item with ID: 2");
        LibraryItem retrievedItem = catalog.getItem(2);
        if (retrievedItem != null) {
            System.out.println(retrievedItem);
        } else {
            System.out.println("Item not found.");
        }
    }
}
