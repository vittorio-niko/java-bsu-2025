package bsu.famcs.nikonchik.lab2.backend.entities.actors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

//TODO:

//сделать фабрики команд, entity классов
//1.1) наладить связь с бд
//написать ответы на вопросы
//добавить корректное сохранение данных в бд
//накатить асинхронность на слой CommandHandler, возможно
//добавить блокировки в классы репозиториев для решения race conditions

//дописать абстрактный класс event и унаследовать от него все ивенты
//надо наладить передачу Id актора, вызывающего команду, в сервисный слой,
//чтобы все адекватно логировать (у каждого event должен быть initiator id)
//в метод execute у команды передавать Id актора

//проверить все аннотации спринга

//доделать лог классы

@Entity
@Table(name = "users")
public class User extends Actor implements Comparable<User> {
    @ElementCollection
    @CollectionTable(
            name = "user_products",
            joinColumns = @JoinColumn(name = "owner_id")
    )
    @Column(name = "products_id")
    private List<UUID> productsOwned;

    protected User() {
        super();
        this.productsOwned = null;
    }

    public User(UUID actorId, String username,
                LocalDateTime createdDate, List<UUID> productsOwned) {
        super(actorId, username, createdDate, PermissionLevel.USER);
        this.productsOwned = Objects.requireNonNull(productsOwned,
                "List of products cannot be null");
    }

    @Override
    public int compareTo(User other) {
        return this.username.compareTo(other.username);
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', created=%s}",
                actorId, username, createdDate);
    }

    public List<UUID> getProductsOwned() { return productsOwned; }

    public void addProduct(UUID product) {
        productsOwned.add(
                Objects.requireNonNull(product, "Product cannot be null")
        );
    }
}


