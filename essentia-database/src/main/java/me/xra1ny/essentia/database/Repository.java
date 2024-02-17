package me.xra1ny.essentia.database;

import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.Session;

import java.util.*;

/**
 * The {@link Repository} class represents a generic repository for managing entities in the Vital-Framework.
 * It is intended to provide common database operations for entities.
 *
 * @param <Entity> The type of entity to be managed.
 * @param <Id>     The type of the entity's identifier.
 * @author xRa1ny
 */
public abstract class Repository<Entity, Id> {
    @Setter
    private Database database;

    /**
     * Checks if an entity with a specific identifier exists.
     *
     * @param id The identifier of the entity.
     * @return {@code true} if an entity with the specified identifier exists, otherwise {@code false}.
     */
    public final boolean existsById(@NonNull Id id) {
        return findById(id).isPresent();
    }

    /**
     * Finds an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return An {@link Optional} containing the entity if found, or an empty {@link Optional} if not found.
     */
    public final Optional<Entity> findById(@NonNull Id id) {
        try (Session session = database.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(managedEntityType(), id));
        }
    }

    /**
     * Finds all entities of a specific class.
     *
     * @return A {@link List} of entities matching the specified class.
     */
    public final List<Entity> findAll() {
        try (Session session = database.getSessionFactory().openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(managedEntityType());
            final Root<Entity> root = criteriaQuery.from(managedEntityType());

            criteriaQuery.select(root);

            final TypedQuery<Entity> typedQuery = session.createQuery(criteriaQuery);

            return typedQuery.getResultList();
        }
    }

    public final Optional<Entity> findByValue(@NonNull String attribute, @NonNull Object value) {
        return findAllByValues(Map.entry(attribute, value)).stream().findFirst();
    }

    @SafeVarargs
    public final Optional<Entity> findByValues(@NonNull Map.Entry<String, Object> @NonNull ... values) {
        return findAllByValues(values).stream().findFirst();
    }

    public final List<Entity> findAllByValue(@NonNull String attribute, @NonNull Object value) {
        return findAllByValues(Map.entry(attribute, value));
    }

    public final boolean existsAllByValues(@NonNull Map.Entry<String, Object> @NonNull ... values) {
        return !findAllByValues(values).isEmpty();
    }

    /**
     * Finds all entities matching specific criteria.
     *
     * @param values An array of column-value pairs to search for.
     * @return A {@link List} of entities matching the specified criteria.
     */
    public final List<Entity> findAllByValues(@NonNull Map.Entry<String, Object> @NonNull ... values) {
        try (Session session = database.getSessionFactory().openSession()) {
            final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            final CriteriaQuery<Entity> criteriaQuery = criteriaBuilder.createQuery(managedEntityType());
            final Root<Entity> root = criteriaQuery.from(managedEntityType());

            criteriaQuery.select(root);

            final List<Predicate> predicateList = new ArrayList<>();

            for (Map.Entry<String, Object> columValueEntry : values) {
                final Predicate predicate = criteriaBuilder.equal(root.get(columValueEntry.getKey()), columValueEntry.getValue());

                predicateList.add(predicate);
            }

            criteriaQuery.where(predicateList.toArray(new Predicate[0]));

            final TypedQuery<Entity> typedQuery = session.createQuery(criteriaQuery);

            try {
                return typedQuery.getResultList();
            } catch (NoResultException e) {
                return Collections.emptyList();
            }
        }
    }

    /**
     * Persists an entity in the database.
     *
     * @param entity The entity to be persisted.
     */
    public final void persist(@NonNull Entity entity) {
        try (Session session = database.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
        }
    }

    /**
     * Removes an entity from the database.
     *
     * @param entity The entity to be removed.
     */
    public final void remove(@NonNull Entity entity) {
        try (Session session = database.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.remove(entity);
            session.getTransaction().commit();
        }
    }

    /**
     * The type this repository manages.
     *
     * @return The type this repository manages.
     */
    public abstract Class<Entity> managedEntityType();
}
