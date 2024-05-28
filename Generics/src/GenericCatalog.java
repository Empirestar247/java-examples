import java.util.ArrayList;
import java.util.List;

public class GenericCatalog<T extends LibraryItem> {
    private List<T> items;

    public GenericCatalog() {
        this.items = new ArrayList<>();
    }

    public void addItem(T item) {
        items.add(item);
    }

    public boolean removeItem(long itemID) {
        for (T item : items) {
            if (item.getItemID() == itemID) {
                items.remove(item);
                return true;
            }
        }
        return false;
    }

    public T getItem(long itemID) {
        for (T item : items) {
            if (item.getItemID() == itemID) {
                return item;
            }
        }
        return null;
    }

    public List<T> getAllItems() {
        return items;
    }
}
