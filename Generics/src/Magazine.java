public class Magazine extends LibraryItem {
    public Magazine(String title, String author) {
        super(title, author);
    }

    @Override
    public String toString() {
        return "Kind: Magazine, Title: " + title + ", Author: " + author + ", ItemID: " + id;
    }
}