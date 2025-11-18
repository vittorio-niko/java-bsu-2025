package bsu.famcs.nikonchik.lab2.backend.entities.actors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//TODO:
//1) паттерн команда для транзакции и заморозки счета
//1.1) наладить связь с бд
//2) добавить админа (и абстрактный класс actor)
//

@Entity
@Table(name = "users")
public class User implements Serializable, Comparable<User> {
    private static final long serialVersionUID = 3L;

    @Id
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private final UUID userId;

    @Column(name = "username", nullable = false, updatable = false, unique = true, length = 50)
    private String username;

    @Column(name = "created_date", nullable = false, updatable = false)
    private final LocalDateTime createdDate;

    @ElementCollection
    @CollectionTable(
            name = "user_products",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "product_id")
    private List<UUID> productsOwned;

    protected User() {
        this.userId = null;
        this.username = null;
        this.createdDate = null;
        this.productsOwned = null;
    }

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
        return this.username.compareTo(other.username);
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
                userId, username, createdDate);
    }

    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public List<UUID> getProductsOwned() { return productsOwned; }

    public void addProduct(UUID product) {
        productsOwned.add(
                Objects.requireNonNull(product, "Product cannot be null")
        );
    }
}

