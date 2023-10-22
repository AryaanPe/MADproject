package com.example.electronicbazarmad;
public class carts {
    private String CartID;
    private String CartName;
    private String ImageURL;
    private String quantity;

    public String getCartID() {
        return CartID;
    }

    public void setCartID(String cartID) {
        CartID = cartID;
    }

    public String getCartName() {
        return CartName;
    }

    public void setCartName(String cartName) {
        CartName = cartName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public carts(String cartID, String cartName, String imageURL, String quantity) {
        this.CartID = cartID;
        this.CartName = cartName;
        this.ImageURL = imageURL;
        this.quantity = quantity;
    }
}
