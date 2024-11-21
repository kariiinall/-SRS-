import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Book {
    String title, author, isbn;
    boolean isAvailable = true;

    Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return title + " (" + author + ") - " + (isAvailable ? "Доступна" : "Арендована");
    }
}

class Reader {
    String name;
    List<Book> rentedBooks = new ArrayList<>();
    static final int MAX_BOOKS_ALLOWED = 3;

    Reader(String name) {
        this.name = name;
    }

    void rentBook(Book book) {
        if (rentedBooks.size() >= MAX_BOOKS_ALLOWED) {
            System.out.println(name + " уже взял максимальное количество книг (" + MAX_BOOKS_ALLOWED + ").");
            return;
        }
        if (book.isAvailable) {
            rentedBooks.add(book);
            book.isAvailable = false;
            System.out.println(name + " взял книгу: " + book.title);
        } else {
            System.out.println("Книга \"" + book.title + "\" недоступна.");
        }
    }

    void returnBook(Book book) {
        if (rentedBooks.remove(book)) {
            book.isAvailable = true;
            System.out.println(name + " вернул книгу: " + book.title);
        } else {
            System.out.println(name + " не арендовал книгу: " + book.title);
        }
    }

    void listRentedBooks() {
        System.out.println("Книги, взятые читателем " + name + ":");
        rentedBooks.forEach(book -> System.out.println("- " + book.title));
    }
}

class Librarian {
    String name;

    Librarian(String name) {
        this.name = name;
    }

    void addBook(Library library, Book book) {
        library.addBook(book);
        System.out.println(name + " добавил книгу: " + book.title);
    }
}

class Library {
    List<Book> books = new ArrayList<>();

    void addBook(Book book) {
        books.add(book);
    }

    void listBooks() {
        books.forEach(book -> System.out.println(book));
    }

    List<Book> searchBooks(String query) {
        return books.stream()
                .filter(book -> book.title.equalsIgnoreCase(query) || book.author.equalsIgnoreCase(query))
                .collect(Collectors.toList());
    }

    void displayClassDiagram() {
        System.out.println("""
                +------------------+         +------------------+
                |     Library      |<-------◆|      Book        |
                +------------------+         +------------------+
                | List<Book> books |         | title: String    |
                |                  |         | author: String   |
                | + addBook()      |         | isbn: String     |
                | + listBooks()    |         | isAvailable: Boolean |
                +------------------+         +------------------+
                      ^                     +------------------+
                      |
                +------------------+         +------------------+
                |     Reader       |         |    Librarian     |
                +------------------+         +------------------+
                | name: String     |         | name: String     |
                | List<Book> rented|         | + addBook()      |
                +------------------+         +------------------+
                """);
    }
}

class LibraryApp {
    public static void main(String[] args) {
        Library library = new Library();
        Librarian librarian = new Librarian("Ерке");

        librarian.addBook(library, new Book("Гарри Поттер и философский камень", "Джоан Роулинг", "978-5-17-085224-7"));
        librarian.addBook(library, new Book("Кровь и пот", "Абдижамил Нурпейсов", "978-601-03-0735-1"));

        System.out.println("\nСписок книг в библиотеке:");
        library.listBooks();

        Reader reader = new Reader("Карина");
        reader.rentBook(library.books.get(0));
        reader.rentBook(library.books.get(1));
        reader.rentBook(library.books.get(2));

        System.out.println("\nСписок книг после аренды:");
        library.listBooks();

        reader.returnBook(library.books.get(0));

        System.out.println("\nСписок книг после возврата:");
        library.listBooks();

        System.out.println("\nДиаграмма классов:");
        library.displayClassDiagram();
    }
}
