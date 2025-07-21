package com.scentedbliss.model;

/**
 * A model class representing a cart item in the application.
 * This class encapsulates the details of a product in a user's cart, including identifiers,
 * product information, price, and quantity. It serves as a data transfer object (DTO) between
 * the database and the application layers.
 */
public class CartModel {
    private int cartId; // Unique identifier for the cart
    private int userId; // Identifier of the user who owns the cart
    private int productId; // Identifier of the product in the cart
    private String productName; // Name of the product
    private String productImage; // URL or path to the product's image
    private double price; // Price of the product per unit
    private int quantity; // Quantity of the product in the cart

    /**
     * Default constructor for creating an empty CartModel instance.
     * Required for frameworks (e.g., JSP, ORM) that instantiate objects via reflection.
     */
    public CartModel() {}

    /**
     * Gets the cart ID.
     * 
     * @return The cart ID
     */
    public int getCartId() {
        return cartId;
    }

    /**
     * Sets the cart ID.
     * 
     * @param cartId The cart ID to set
     */
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    /**
     * Gets the user ID associated with the cart.
     * 
     * @return The user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID associated with the cart.
     * 
     * @param userId The user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the product ID of the item in the cart.
     * 
     * @return The product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID of the item in the cart.
     * 
     * @param productId The product ID to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the name of the product in the cart.
     * 
     * @return The product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product in the cart.
     * 
     * @param productName The product name to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the image URL or path of the product in the cart.
     * 
     * @return The product image URL or path
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * Sets the image URL or path of the product in the cart.
     * 
     * @param productImage The product image URL or path to set
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * Gets the price per unit of the product in the cart.
     * 
     * @return The price of the product
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price per unit of the product in the cart.
     * 
     * @param price The price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the quantity of the product in the cart.
     * 
     * @return The quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product in the cart.
     * 
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}