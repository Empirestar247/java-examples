public class DVD extends LibraryItem {
    public DVD(String title, String author) {
        super(title, author);
    }

    @Override
    public String toString() {
        return "Kind: DVD, Title: " + title + ", Author: " + author + ", ItemID: " + id;
    }
}