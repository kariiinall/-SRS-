import java.util.ArrayList;
import java.util.List;

class Book {
    String title, author, isbn;
    boolean isAvailable = true;

    Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    void markAsLoaned() {
        this.isAvailable = false;
    }

    void markAsAvailable() {
        this.isAvailable = true;
    }
}

class Reader {
    String name;
    List<Book> rentedBooks = new ArrayList<>();

    Reader(String name) {
        this.name = name;
    }

    void rentBook(Book book) {
        if (book.isAvailable) {
            rentedBooks.add(book);
            book.markAsLoaned();
            System.out.println(name + " взял книгу: " + book.title);
        } else {
            System.out.println("Книга " + book.title + " недоступна.");
        }
    }

    void returnBook(Book book) {
        if (rentedBooks.contains(book)) {
            rentedBooks.remove(book);
            book.markAsAvailable();
            System.out.println(name + " вернул книгу: " + book.title);
        } else {
            System.out.println("Вы не брали эту книгу.");
        }
    }
}

class Library {
    List<Book> books = new ArrayList<>();

    void addBook(Book book) {
        books.add(book);
        System.out.println("Добавлена книга: " + book.title);
    }

    void listBooks() {
        if (books.isEmpty()) {
            System.out.println("В библиотеке нет книг.");
        } else {
            books.forEach(book -> System.out.println(book.title + " | " + book.author + " | " + (book.isAvailable ? "Доступна" : "Недоступна")));
        }
    }

    void displayDiagram() {
        System.out.println("""
                +-------------------+         +------------------+
                |     Library       |<-------◆|      Book        |
                +-------------------+         +------------------+
                | List<Book> books  |         | title: String    |
                | + addBook()       |         | author: String   |
                | + listBooks()     |         | isbn: String     |
                +-------------------+         | isAvailable: Boolean |
                      ^                     +------------------+
                      |
                +-------------------+         +------------------+
                |     Reader        |<--------○|    Librarian     |
                +-------------------+         +------------------+
                | name: String      |         | name: String     |
                | List<Book> rented |         +------------------+
                | + rentBook()      |
                | + returnBook()    |
                +-------------------+""");
    }
}

class LibraryApp {
    public static void main(String[] args) {
        Library library = new Library();
        Book book1 = new Book("1984", "Джордж Оруэлл", "12345");
        Book book2 = new Book("Моби Дик", "Герман Мелвилл", "67890");

        library.addBook(book1);
        library.addBook(book2);

        Reader reader = new Reader("Карина");

        reader.rentBook(book1);

        library.listBooks();

        reader.returnBook(book1);

        library.listBooks();

        System.out.println("\nДиаграмма классов:");
        library.displayDiagram();
    }
}
