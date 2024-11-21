import java.util.ArrayList;
import java.util.List;

class User {
    int id;
    String username;
    String password;

    User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
}

class Product {
    int id;
    String name;
    double price;

    Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class Order {
    int id;
    int userId;
    List<Product> products;
    String status;

    Order(int id, int userId, List<Product> products, String status) {
        this.id = id;
        this.userId = userId;
        this.products = products;
        this.status = status;
    }
}

// Интерфейсы
interface IUserService {
    User register(String username, String password);
    User login(String username, String password);
}

interface IProductService {
    List<Product> getProducts();
    Product addProduct(Product product);
}

interface IOrderService {
    Order createOrder(int userId, List<Integer> productIds);
    Order getOrderStatus(int orderId);
}

interface IPaymentService {
    boolean processPayment(int orderId, double amount);
}

interface INotificationService {
    void sendNotification(int userId, String message);
}

// Реализация сервисов
class UserService implements IUserService {
    private final List<User> users = new ArrayList<>();
    private int currentUserId = 1;

    @Override
    public User register(String username, String password) {
        User user = new User(currentUserId++, username, password);
        users.add(user);
        return user;
    }

    @Override
    public User login(String username, String password) {
        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }
}

class ProductService implements IProductService {
    private final List<Product> products = new ArrayList<>();
    private int currentProductId = 1;

    @Override
    public List<Product> getProducts() {
        return products;
    }

    @Override
    public Product addProduct(Product product) {
        product.id = currentProductId++;
        products.add(product);
        return product;
    }
}

class PaymentService implements IPaymentService {
    @Override
    public boolean processPayment(int orderId, double amount) {
        // Пример обработки платежа
        System.out.println("Обработка платежа для заказа: " + orderId + ", сумма: " + amount);
        return true; // Платеж успешен
    }
}

class NotificationService implements INotificationService {
    @Override
    public void sendNotification(int userId, String message) {
        System.out.println("Уведомление для пользователя " + userId + ": " + message);
    }
}

class OrderService implements IOrderService {
    private final IProductService productService;
    private final IPaymentService paymentService;
    private final INotificationService notificationService;
    private int currentOrderId = 1;

    OrderService(IProductService productService, IPaymentService paymentService, INotificationService notificationService) {
        this.productService = productService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    @Override
    public Order createOrder(int userId, List<Integer> productIds) {
        List<Product> products = new ArrayList<>();
        for (Product product : productService.getProducts()) {
            if (productIds.contains(product.id)) {
                products.add(product);
            }
        }

        if (products.isEmpty()) {
            throw new IllegalArgumentException("Продукты не найдены");
        }

        Order order = new Order(currentOrderId++, userId, products, "Created");
        double totalAmount = products.stream().mapToDouble(p -> p.price).sum();

        if (paymentService.processPayment(order.id, totalAmount)) {
            order.status = "Paid";
            notificationService.sendNotification(userId, "Ваш заказ успешно оплачен.");
        } else {
            order.status = "Payment Failed";
            notificationService.sendNotification(userId, "Ошибка при оплате. Попробуйте снова.");
        }

        return order;
    }

    @Override
    public Order getOrderStatus(int orderId) {
        return new Order(orderId, 1, new ArrayList<>(), "In Progress");
    }
}

class ECommerceApp {
    public static void main(String[] args) {
        IUserService userService = new UserService();
        IProductService productService = new ProductService();
        IPaymentService paymentService = new PaymentService();
        INotificationService notificationService = new NotificationService();
        IOrderService orderService = new OrderService(productService, paymentService, notificationService);

        User user = userService.register("Карина", "kaka12346");

        Product product1 = productService.addProduct(new Product(0, "MacBook Pro", 1200.00));
        Product product2 = productService.addProduct(new Product(0, "Phone 15 Pro Max", 800.00));

        List<Integer> productIds = List.of(product1.id, product2.id);
        Order order = orderService.createOrder(user.id, productIds);

        System.out.println("Статус заказа: " + order.status);
    }
}
