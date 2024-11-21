import java.util.*;

interface UserManagementService {
    boolean registerUser(String username, String password);
    boolean loginUser(String username, String password);
}

interface HotelService {
    void addHotel(Hotel hotel);
    List<Hotel> searchHotels(String location);
}

interface BookingService {
    boolean bookRoom(String hotelName, String userId, Date checkIn, Date checkOut);
    List<String> getUserBookings(String userId);
}

interface PaymentService {
    boolean processPayment(String userId, double amount);
}

interface NotificationService {
    void sendNotification(String userId, String message);
}

class UserManagementServiceImpl implements UserManagementService {
    private Map<String, String> users = new HashMap<>();

    @Override
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) return false;
        users.put(username, password);
        return true;
    }

    @Override
    public boolean loginUser(String username, String password) {
        return users.getOrDefault(username, "").equals(password);
    }
}

class Hotel {
    String name;
    String location;

    public Hotel(String name, String location) {
        this.name = name;
        this.location = location;
    }
}

class HotelServiceImpl implements HotelService {
    private List<Hotel> hotels = new ArrayList<>();

    @Override
    public void addHotel(Hotel hotel) {
        hotels.add(hotel);
    }

    @Override
    public List<Hotel> searchHotels(String location) {
        List<Hotel> results = new ArrayList<>();
        for (Hotel hotel : hotels) {
            if (hotel.location.equalsIgnoreCase(location)) {
                results.add(hotel);
            }
        }
        return results;
    }
}

class BookingServiceImpl implements BookingService {
    private List<String> bookings = new ArrayList<>();

    @Override
    public boolean bookRoom(String hotelName, String userId, Date checkIn, Date checkOut) {
        bookings.add("Пользователь: " + userId + ", Отель: " + hotelName + ", Даты: " + checkIn + " - " + checkOut);
        return true;
    }

    @Override
    public List<String> getUserBookings(String userId) {
        return bookings;
    }
}

class PaymentServiceImpl implements PaymentService {
    @Override
    public boolean processPayment(String userId, double amount) {
        System.out.println("Оплата успешна для пользователя " + userId + ", сумма: " + amount);
        return true;
    }
}

class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(String userId, String message) {
        System.out.println("Уведомление для " + userId + ": " + message);
    }
}

class HotelBookingApp {
    private static UserManagementService userService = new UserManagementServiceImpl();
    private static HotelService hotelService = new HotelServiceImpl();
    private static BookingService bookingService = new BookingServiceImpl();
    private static PaymentService paymentService = new PaymentServiceImpl();
    private static NotificationService notificationService = new NotificationServiceImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Добро пожаловать в систему бронирования отелей!");

        while (true) {
            System.out.println("\n1. Регистрация\n2. Вход\n3. Добавить отель (админ)\n4. Поиск отелей\n5. Выйти");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: register(scanner); break;
                case 2: login(scanner); break;
                case 3: addHotel(scanner); break;
                case 4: searchHotels(scanner); break;
                case 5: System.exit(0);
                default: System.out.println("Неверный ввод!");
            }
        }
    }

    private static void register(Scanner scanner) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        if (userService.registerUser(username, password)) {
            System.out.println("Пользователь успешно зарегистрирован!");
        } else {
            System.out.println("Ошибка: пользователь уже существует.");
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        if (userService.loginUser(username, password)) {
            System.out.println("Вход выполнен успешно!");
        } else {
            System.out.println("Ошибка входа!");
        }
    }

    private static void addHotel(Scanner scanner) {
        System.out.print("Название отеля: ");
        String name = scanner.nextLine();
        System.out.print("Местоположение: ");
        String location = scanner.nextLine();
        hotelService.addHotel(new Hotel(name, location));
        System.out.println("Отель добавлен успешно!");
    }

    private static void searchHotels(Scanner scanner) {
        System.out.print("Введите местоположение для поиска: ");
        String location = scanner.nextLine();
        List<Hotel> results = hotelService.searchHotels(location);
        if (results.isEmpty()) {
            System.out.println("Отели не найдены.");
        } else {
            System.out.println("Доступные отели:");
            for (Hotel hotel : results) {
                System.out.println("- " + hotel.name);
            }
        }
    }
}
