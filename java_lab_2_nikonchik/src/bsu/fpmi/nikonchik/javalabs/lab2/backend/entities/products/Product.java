package bsu.fpmi.nikonchik.javalabs.lab2.backend.entities.products;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public abstract class Product implements Serializable, Comparable<Product> {
    private static final long serialVersionUID = 1L;

    protected final UUID productId;
    protected UUID ownerId;
    protected final LocalDateTime createdDate;

    protected Product(UUID productId, UUID ownerId, LocalDateTime createdDate) {
        this.productId = Objects.requireNonNull(productId,
                "Product id cannot be null");
        this.ownerId = Objects.requireNonNull(ownerId,
                "Owner id cannot be null");
        this.createdDate = Objects.requireNonNull(createdDate,
                "Creation date cannot be null");
    }

    @Override
    public int compareTo(Product other) {
        return this.createdDate.compareTo(other.createdDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product that = (Product) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    public UUID getProductId() { return productId; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public UUID getOwnerId() { return ownerId; }
}
