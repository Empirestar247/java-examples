public class LibraryItem {
    protected String title;
    protected String author;
    protected long id;
    private static long lastId = 0;

    public LibraryItem(String title, String author) {
        this.title = title;
        this.author = author;
        this.id = lastId + 1;
        lastId++;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public long getItemID() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ItemID: " + id;
    }
}
