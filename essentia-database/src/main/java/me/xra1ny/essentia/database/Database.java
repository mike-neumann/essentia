package me.xra1ny.essentia.database;

import lombok.Getter;
import lombok.NonNull;
import me.xra1ny.essentia.database.annotation.DatabaseInfo;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Optional;

/**
 * The {@link Database} class represents a component responsible for managing database connections.
 *
 * @apiNote Must be annotated with {@link DatabaseInfo}.
 * @author xRa1ny
 */
public class Database {
    /**
     * Stores the configuration for the database connection.
     */
    @Getter
    @NonNull
    private final Configuration configuration;

    /**
     * Represents the Hibernate SessionFactory for managing database sessions.
     */
    @Getter
    @NonNull
    private SessionFactory sessionFactory;

    public Database() {
        final DatabaseInfo vitalDatabaseInfo = Optional.ofNullable(getClass().getAnnotation(DatabaseInfo.class))
                .orElseThrow(() -> new RuntimeException("Database must be annotated with @DatabaseInfo"));

        this.configuration = new Configuration().configure(vitalDatabaseInfo.value());
    }

    public void registerRepository(@NonNull Repository<?,?> repository) {
        configuration.addAnnotatedClass(repository.managedEntityType());
        repository.setDatabase(this);
    }

    /**
     * Enables this database, parsing every registered repository.
     */
    public void enable() {
        sessionFactory = configuration.buildSessionFactory();
    }
}
