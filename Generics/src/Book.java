public class Book extends LibraryItem {
    public Book(String title, String author) {
        super(title, author);
    }

    @Override
    public String toString() {
        return "Kind: Book, Title: " + title + ", Author: " + author + ", ItemID: " + id;
    }
}