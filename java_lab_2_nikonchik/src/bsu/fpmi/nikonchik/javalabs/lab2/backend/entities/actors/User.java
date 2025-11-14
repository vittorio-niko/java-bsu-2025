package bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.actors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

import bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.products.Product;

public class User implements Serializable, Comparable<User>, Cloneable {
    private static final long serialVersionUID = 1L;

    private final UUID userId;
    private String username;
    private final LocalDateTime createdDate;
    private List<UUID> productsOwned;

    public User(UUID userId, String username,
                LocalDateTime createdDate, List<UUID> productsOwned) {
        this.userId = Objects.requireNonNull(userId,
                "User id cannot be null");
        this.username = Objects.requireNonNull(username,
                "Username cannot be null");
        this.createdDate = Objects.requireNonNull(createdDate,
                "Created date cannot be null");
        this.productsOwned = Objects.requireNonNull(productsOwned,
                "List of products cannot be null");
    }

    @Override
    public int compareTo(User other) {
        return this.createdDate.compareTo(other.createdDate);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', created=%s}",
                userId.toString(), username, createdDate.toString());
    }

    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public List<UUID> getProductsOwned() { return productsOwned; }
}
