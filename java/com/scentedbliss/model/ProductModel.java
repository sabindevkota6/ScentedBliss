package com.scentedbliss.model;

/**
 * A model class representing a product in the application.
 * This class encapsulates the details of a product, including identifiers, name, description,
 * price, stock, quantity (e.g., in a cart or order), brand, image, and creation/update timestamps.
 * It serves as a data transfer object (DTO) between the database and the application layers.
 * 
 * Note: The inclusion of `cartId` suggests this model might be used in a cart context, but
 * typically a `ProductModel` represents a product catalog entry. Consider separating cart-specific
 * data into a `CartItemModel` if needed.
 */
public class ProductModel {
    private int cartId; // Identifier of the cart (if this product is part of a cart); may be redundant for a general product model
    private int productId; // Unique identifier for the product
    private String productName; // Name of the product
    private String productDescription; // Description of the product
    private double price; // Price of the product per unit
    private int stock; // Available stock quantity of the product
    private int quantity; // Quantity of the product (e.g., in a cart or order context)
    private String brand; // Brand name of the product
    private String productImage; // URL or path to the product's image
    private String createdAt; // Timestamp when the product was created (stored as a string)
    private String updatedAt; // Timestamp when the product was last updated (stored as a string)

    /**
     * Default constructor for creating an empty ProductModel instance.
     * Required for frameworks (e.g., JSP, ORM) that instantiate objects via reflection.
     */
    public ProductModel() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Parameterized constructor to initialize a ProductModel instance with all fields.
     * 
     * @param cartId The identifier of the cart (if applicable)
     * @param productId The unique identifier for the product
     * @param productName The name of the product
     * @param productDescription The description of the product
     * @param price The price of the product per unit
     * @param stock The available stock quantity of the product
     * @param quantity The quantity of the product (e.g., in a cart or order)
     * @param brand The brand name of the product
     * @param productImage The URL or path to the product's image
     * @param createdAt The timestamp when the product was created (as a string)
     * @param updatedAt The timestamp when the product was last updated (as a string)
     */
    public ProductModel(int cartId, int productId, String productName, String productDescription, double price, int stock,
            int quantity, String brand, String productImage, String createdAt, String updatedAt) {
        super();
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.stock = stock;
        this.quantity = quantity;
        this.brand = brand;
        this.productImage = productImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the cart ID associated with this product (if applicable).
     * 
     * @return The cart ID
     */
    public int getCartId() {
        return cartId;
    }

    /**
     * Sets the cart ID associated with this product.
     * 
     * @param cartId The cart ID to set
     */
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    /**
     * Gets the product ID.
     * 
     * @return The product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     * 
     * @param productId The product ID to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * Gets the name of the product.
     * 
     * @return The product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product.
     * 
     * @param productName The product name to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the description of the product.
     * 
     * @return The product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Sets the description of the product.
     * 
     * @param productDescription The product description to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Gets the price per unit of the product.
     * 
     * @return The price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price per unit of the product.
     * 
     * @param price The price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets the available stock quantity of the product.
     * 
     * @return The stock quantity
     */
    public int getStock() {
        return stock;
    }

    /**
     * Sets the available stock quantity of the product.
     * 
     * @param stock The stock quantity to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Gets the quantity of the product (e.g., in a cart or order context).
     * 
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product (e.g., in a cart or order context).
     * 
     * @param quantity The quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the brand name of the product.
     * 
     * @return The brand name
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand name of the product.
     * 
     * @param brand The brand name to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * Gets the URL or path to the product's image.
     * 
     * @return The product image URL or path
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * Sets the URL or path to the product's image.
     * 
     * @param productImage The product image URL or path to set
     */
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    /**
     * Gets the creation timestamp of the product as a string.
     * Note: This assumes the date is stored in a string format compatible with the database (e.g., "YYYY-MM-DD HH:MM:SS").
     * 
     * @return The creation timestamp
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp of the product as a string.
     * Note: This assumes the date is provided in a string format compatible with the database.
     * 
     * @param createdAt The creation timestamp to set
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last updated timestamp of the product as a string.
     * Note: This assumes the date is stored in a string format compatible with the database (e.g., "YYYY-MM-DD HH:MM:SS").
     * 
     * @return The updated timestamp
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last updated timestamp of the product as a string.
     * Note: This assumes the date is provided in a string format compatible with the database.
     * 
     * @param updatedAt The updated timestamp to set
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}