package edu.MD.Fx_final.service;

import edu.MD.Fx_final.model.dto.BookOrderDTO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public final class OrderRegistry {
    public static final long CANCELLATION_WINDOW_MILLIS = 30L * 60L * 1000L;
    private static final Path ORDERS_FILE = Path.of(System.getProperty("user.home"), ".bookworm-orders.db");
    private static final List<BookOrderDTO> ORDERS = new ArrayList<>();

    static {
        loadFromDisk();
    }

    private OrderRegistry() {
    }

    public static synchronized void addOrder(BookOrderDTO order) {
        ORDERS.add(0, order);
        saveToDisk();
    }

    public static synchronized List<BookOrderDTO> getAllOrders() {
        return new ArrayList<>(ORDERS);
    }

    public static synchronized CancelResult cancelOrder(String userNic, String orderId) {
        long now = System.currentTimeMillis();
        for (int i = 0; i < ORDERS.size(); i++) {
            BookOrderDTO order = ORDERS.get(i);
            if (!order.getOrderId().equals(orderId)) {
                continue;
            }
            if (!order.getUserNic().equals(userNic)) {
                return new CancelResult(false, "You can only delete your own orders.", null);
            }

            long age = now - order.getCreatedAtMillis();
            if (age > CANCELLATION_WINDOW_MILLIS) {
                return new CancelResult(false, "This order is older than 30 minutes and cannot be deleted.", null);
            }

            ORDERS.remove(i);
            saveToDisk();
            return new CancelResult(true, "Order deleted successfully.", order);
        }
        return new CancelResult(false, "Selected order was not found.", null);
    }

    public record CancelResult(boolean success, String message, BookOrderDTO order) {}

    private static void loadFromDisk() {
        if (!Files.exists(ORDERS_FILE)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(ORDERS_FILE, StandardCharsets.UTF_8);
            ORDERS.clear();
            for (String line : lines) {
                BookOrderDTO order = parseLine(line);
                if (order != null) {
                    ORDERS.add(order);
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static void saveToDisk() {
        List<String> lines = new ArrayList<>();
        for (BookOrderDTO order : ORDERS) {
            lines.add(toLine(order));
        }
        try {
            Files.write(
                    ORDERS_FILE,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException ignored) {
        }
    }

    private static String toLine(BookOrderDTO order) {
        return encode(order.getOrderId()) + ","
                + encode(order.getUserNic()) + ","
                + encode(order.getUserName()) + ","
                + encode(order.getBookTitle()) + ","
                + encode(order.getBookAuthor()) + ","
                + encode(order.getOrderedAt()) + ","
                + encode(order.getStatus()) + ","
                + order.getCreatedAtMillis();
    }

    private static BookOrderDTO parseLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length != 8) {
            return null;
        }
        try {
            return new BookOrderDTO(
                    decode(parts[0]),
                    decode(parts[1]),
                    decode(parts[2]),
                    decode(parts[3]),
                    decode(parts[4]),
                    decode(parts[5]),
                    decode(parts[6]),
                    Long.parseLong(parts[7])
            );
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String encode(String value) {
        if (value == null) {
            value = "";
        }
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
