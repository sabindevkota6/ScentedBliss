package com.scentedbliss.model;

/**
 * A model class representing an order in the application.
 * This class encapsulates the details of an order, including its identifier, date, user association,
 * shipping address, and total amount. It serves as a data transfer object (DTO) between the
 * database and the application layers.
 */
public class OrderModel {
    private int orderId; // Unique identifier for the order
    private String orderDate; // Date and time when the order was placed (stored as a string)
    private int userId; // Identifier of the user who placed the order
    private String shippingAddress; // Shipping address for the order
    private double totalAmount; // Total amount for the order (sum of all order items)

    /**
     * Default constructor for creating an empty OrderModel instance.
     * Required for frameworks (e.g., JSP, ORM) that instantiate objects via reflection.
     */
    public OrderModel() {}

    /**
     * Parameterized constructor to initialize an OrderModel instance with all fields.
     * 
     * @param orderId The unique identifier for the order
     * @param orderDate The date and time when the order was placed (as a string)
     * @param userId The identifier of the user who placed the order
     * @param shippingAddress The shipping address for the order
     * @param totalAmount The total amount for the order
     */
    public OrderModel(int orderId, String orderDate, int userId, String shippingAddress, double totalAmount) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the order ID.
     * 
     * @return The order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID.
     * 
     * @param orderId The order ID to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the order date and time as a string.
     * Note: This assumes the date is stored in a string format compatible with the database (e.g., "YYYY-MM-DD HH:MM:SS").
     * 
     * @return The order date as a string
     */
    public String getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the order date and time as a string.
     * Note: This assumes the date is provided in a string format compatible with the database.
     * 
     * @param orderDate The order date to set
     */
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    /**
     * Gets the user ID associated with the order.
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the order.
     * 
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the shipping address for the order.
     * 
     * @return The shipping address
     */
    public String getShippingAddress() {
        return shippingAddress;
    }

    /**
     * Sets the shipping address for the order.
     * 
     * @param shippingAddress The shipping address to set
     */
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    /**
     * Gets the total amount for the order.
     * 
     * @return The total amount
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount for the order.
     * 
     * @param totalAmount The total amount to set
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}