package com.scentedbliss.model;

/**
 * A model class representing an order item in the application.
 * This class encapsulates the details of a single item within an order, including identifiers,
 * quantity, unit price, and subtotal. It serves as a data transfer object (DTO) between the
 * database and the application layers.
 */
public class OrderItemModel {
    private int orderItemId; // Unique identifier for the order item
    private int orderId; // Identifier of the order this item belongs to
    private int productId; // Identifier of the product associated with this order item
    private int quantity; // Quantity of the product ordered
    private double unitPrice; // Price per unit of the product at the time of the order
    private double subTotal; // Subtotal for this order item (quantity * unitPrice)

    /**
     * Default constructor for creating an empty OrderItemModel instance.
     * Required for frameworks (e.g., JSP, ORM) that instantiate objects via reflection.
     */
    public OrderItemModel() {}

    /**
     * Parameterized constructor to initialize an OrderItemModel instance with all fields.
     * 
     * @param orderItemId The unique identifier for the order item
     * @param orderId The identifier of the order this item belongs to
     * @param productId The identifier of the product
     * @param quantity The quantity of the product ordered
     * @param unitPrice The price per unit of the product
     * @param subTotal The subtotal for this order item (quantity * unitPrice)
     */
    public OrderItemModel(int orderItemId, int orderId, int productId, int quantity, double unitPrice, double subTotal) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
    }

    /**
     * Gets the order item ID.
     * 
     * @return The order item ID
     */
    public int getOrderItemId() {
        return orderItemId;
    }

    /**
     * Sets the order item ID.
     * 
     * @param orderItemId The order item ID to set
     */
    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * Gets the order ID associated with this order item.
     * 
     * @return The order ID
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Sets the order ID associated with this order item.
     * 
     * @param orderId The order ID to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the product ID associated with this order item.
     * 
     * @return The product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID associated with this order item.
     * 
     * @param productId The product ID to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the quantity of the product ordered.
     * 
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product ordered.
     * 
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the unit price of the product at the time of the order.
     * 
     * @return The unit price
     */
    public double getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price of the product at the time of the order.
     * 
     * @param unitPrice The unit price to set
     */
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * Gets the subtotal for this order item (quantity * unitPrice).
     * 
     * @return The subtotal
     */
    public double getSubTotal() {
        return subTotal;
    }

    /**
     * Sets the subtotal for this order item (quantity * unitPrice).
     * 
     * @param subTotal The subtotal to set
     */
    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}