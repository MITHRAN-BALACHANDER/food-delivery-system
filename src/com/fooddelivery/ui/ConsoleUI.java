package com.fooddelivery.ui;

import com.fooddelivery.model.*;
import com.fooddelivery.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ConsoleUI {
    private final AuthService authService;
    private final RestaurantService restaurantService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final AnalyticsService analyticsService;
    private final Scanner scanner;

    public ConsoleUI(AuthService authService, RestaurantService restaurantService, OrderService orderService, DeliveryService deliveryService, AnalyticsService analyticsService) {
        this.authService = authService;
        this.restaurantService = restaurantService;
        this.orderService = orderService;
        this.deliveryService = deliveryService;
        this.analyticsService = analyticsService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Food Delivery Management System ---");
            if (authService.getCurrentUser() == null) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Reports");
                System.out.println("4. Exit");
                System.out.print("Choose option: ");
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1: login(); break;
                    case 2: register(); break;
                    case 3: runReports(); break;
                    case 4: System.exit(0);
                    default: System.out.println("Invalid choice");
                }
            } else {
                Role role = authService.getCurrentUser().getRole();
                if (role == Role.CUSTOMER) customerMenu();
                else if (role == Role.RESTAURANT) restaurantMenu();
                else if (role == Role.DELIVERY_AGENT) deliveryAgentMenu();
            }
        }
    }

    private void runReports() {
        analyticsService.printTopRestaurants();
        analyticsService.printDemandByRegion();
    }

    private void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        try {
            authService.login(email, password);
            System.out.println("Logged in as " + authService.getCurrentUser().getName());
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }
    }

    private void register() {
        System.out.println("Role (1: Customer, 2: Restaurant, 3: Delivery Agent): ");
        int roleChoice = Integer.parseInt(scanner.nextLine());
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        String id = UUID.randomUUID().toString();

        try {
            if (roleChoice == 1) {
                System.out.print("Address: ");
                String address = scanner.nextLine();
                System.out.print("Pincode: ");
                String pincode = scanner.nextLine();
                authService.register(new Customer(id, name, email, password, address, pincode));
            } else if (roleChoice == 2) {
                System.out.print("Address: ");
                String address = scanner.nextLine();
                System.out.print("Pincode: ");
                String pincode = scanner.nextLine();
                authService.register(new Restaurant(id, name, email, password, address, pincode));
            } else if (roleChoice == 3) {
                System.out.print("Current Pincode: ");
                String pincode = scanner.nextLine();
                authService.register(new DeliveryAgent(id, name, email, password, pincode));
            }
            System.out.println("Registered successfully. Please login.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void customerMenu() {
        System.out.println("\n--- Customer Menu ---");
        System.out.println("1. Browse Restaurants & Menu");
        System.out.println("2. Place Order");
        System.out.println("3. View Orders");
        System.out.println("4. Rate Delivered/Closed Order");
        System.out.println("5. Logout");
        System.out.print("Option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1:
                List<Restaurant> restaurants = restaurantService.getAllRestaurants();
                for (int i = 0; i < restaurants.size(); i++) {
                    Restaurant r = restaurants.get(i);
                    System.out.println((i + 1) + ". " + r.getName() + " (Accepting: " + r.isAcceptingOrders() + ")");
                    for (MenuItem item : r.getMenu()) {
                        System.out.println("\t- " + item.getName() + " ($" + item.getPrice() + ")");
                    }
                }
                break;
            case 2:
                placeOrderFlow();
                break;
            case 3:
                for (Order o : orderService.getOrdersForUser(authService.getCurrentUser())) {
                    System.out.println("Order [" + o.getId() + "] - " + o.getRestaurant().getName() + " - " + o.getStatus() + " - Total: $" + o.getTotalAmount());
                }
                break;
            case 4:
                System.out.print("Enter Order ID to rate: ");
                String orderId = scanner.nextLine();
                System.out.print("Restaurant Rating (1-5): ");
                int rRate = Integer.parseInt(scanner.nextLine());
                System.out.print("Agent Rating (1-5): ");
                int dRate = Integer.parseInt(scanner.nextLine());
                System.out.print("Feedback: ");
                String fb = scanner.nextLine();
                try {
                    orderService.rateOrder(orderId, rRate, dRate, fb);
                    System.out.println("Rated successfully!");
                } catch (Exception e) {
                    System.out.println("Failed to rate: " + e.getMessage());
                }
                break;
            case 5:
                authService.logout();
                break;
        }
    }

    private void placeOrderFlow() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        System.out.print("Select Restaurant Index: ");
        int rIdx = Integer.parseInt(scanner.nextLine()) - 1;
        if (rIdx < 0 || rIdx >= restaurants.size()) return;
        
        Restaurant r = restaurants.get(rIdx);
        if (!r.isAcceptingOrders()) {
             System.out.println("Restaurant closed."); return;
        }

        List<MenuItem> cart = new ArrayList<>();
        while (true) {
            System.out.print("Enter item name to add (or 'done' to finish): ");
            String itemName = scanner.nextLine();
            if (itemName.equalsIgnoreCase("done")) break;

            MenuItem item = r.getMenu().stream()
                    .filter(m -> m.getName().equalsIgnoreCase(itemName)).findFirst().orElse(null);
            if (item != null) {
                cart.add(item);
                System.out.println("Added " + item.getName());
            } else {
                System.out.println("Item not found");
            }
        }
        if (!cart.isEmpty()) {
            Order o = orderService.placeOrder((Customer) authService.getCurrentUser(), r, cart);
            System.out.println("Order placed! ID: " + o.getId());
        }
    }

    private void restaurantMenu() {
        System.out.println("\n--- Restaurant Menu ---");
        System.out.println("1. Add Menu Item");
        System.out.println("2. View Orders");
        System.out.println("3. Confirm Order");
        System.out.println("4. Toggle Accepting Orders");
        System.out.println("5. Logout");
        System.out.print("Option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        Restaurant r = (Restaurant) authService.getCurrentUser();
        switch (choice) {
            case 1:
                System.out.print("Item name: ");
                String name = scanner.nextLine();
                System.out.print("Price: ");
                double price = Double.parseDouble(scanner.nextLine());
                restaurantService.addMenuItem(r, new MenuItem(UUID.randomUUID().toString(), name, price, r.getId()));
                System.out.println("Item added.");
                break;
            case 2:
                for (Order o : orderService.getOrdersForUser(r)) {
                    System.out.println("Order [" + o.getId() + "] Status: " + o.getStatus() + " Agent: " + (o.getDeliveryAgent() != null ? o.getDeliveryAgent().getName() : "None"));
                }
                break;
            case 3:
                System.out.print("Enter Order ID to confirm: ");
                String oId = scanner.nextLine();
                try {
                    orderService.confirmOrder(oId);
                    System.out.println("Order confirmed! Agent assigned automatically.");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case 4:
                r.setAcceptingOrders(!r.isAcceptingOrders());
                System.out.println("Accepting orders: " + r.isAcceptingOrders());
                break;
            case 5:
                authService.logout();
                break;
        }
    }

    private void deliveryAgentMenu() {
        System.out.println("\n--- Delivery Agent Menu ---");
        System.out.println("1. View My Orders");
        System.out.println("2. Mark Delivered");
        System.out.println("3. Logout");
        System.out.print("Option: ");
        int choice = Integer.parseInt(scanner.nextLine());

        DeliveryAgent agent = (DeliveryAgent) authService.getCurrentUser();
        switch (choice) {
            case 1:
                for (Order o : orderService.getOrdersForUser(agent)) {
                    System.out.println("Assigned Order [" + o.getId() + "] Status: " + o.getStatus());
                }
                break;
            case 2:
                System.out.print("Enter Order ID to mark delivered: ");
                String oId = scanner.nextLine();
                try {
                    orderService.deliverOrder(oId);
                    orderService.closeOrder(oId);
                    System.out.println("Order delivered and closed!");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;
            case 3:
                authService.logout();
                break;
        }
    }
}
