package gr.unipi.aristeridis.repositories;

import gr.unipi.aristeridis.exceptions.CustomException;
import gr.unipi.aristeridis.exceptions.OwnerNotFoundException;
import gr.unipi.aristeridis.model.Property;
import gr.unipi.aristeridis.utility.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;

@Slf4j
public class PropertyRepository {

    private final EntityManager entityManager;

    public PropertyRepository() {
        entityManager = JPAUtil.getEntityManager();
    }

    public Optional<Property> findById(Long propertyId) {
        try {
            Property property = entityManager.find(Property.class, propertyId);
            return Optional.ofNullable(property);
        } catch (CustomException ce) {
            log.debug("Could not find Property with ID: " + propertyId);
            entityManager.getTransaction().rollback();
            System.out.println(ce.getMessage());
        }
        return Optional.empty();
    }

    public List<Property> findByOwnerId(Long ownerId) {
        try {
            entityManager.getTransaction().begin();
            TypedQuery<Property> query = entityManager.createQuery(
                    "SELECT p FROM Property p WHERE p.owner.OwnerId = :ownerId", Property.class); //paizei na einai ownerId
            query.setParameter("ownerId", ownerId);
            entityManager.getTransaction().commit();
            return query.getResultList();
        } catch (OwnerNotFoundException oe) {
            log.debug("Could not find Properties for Owner ID: " + ownerId);
            entityManager.getTransaction().rollback();
            System.out.println(oe.getMessage());
        }
        return List.of();
    }

    public List<Property> findAll() {
        try {
            TypedQuery<Property> query = entityManager.createQuery(
                    "SELECT p FROM Property p WHERE p.deleted = false", Property.class);
            return query.getResultList();
        } catch (Exception e) {
            log.debug("Could not retrieve all Properties", e);
            return List.of();
        }
    }

    public Optional<Property> save(Property property) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(property);
            entityManager.getTransaction().commit();
            return Optional.of(property);
        } catch (Exception e) {
            log.debug("Could not save Property", e);
            entityManager.getTransaction().rollback();
        }
        return Optional.empty();
    }

    public boolean deleteById(Long propertyId) {
        try {
            Property property = entityManager.find(Property.class, propertyId);
            if (property != null) {
                entityManager.getTransaction().begin();
                entityManager.remove(property);
                entityManager.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            log.debug("Could not delete Property with ID: " + propertyId);
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    public boolean safeDeleteById(Long propertyId) {
        try {
            Property property = entityManager.find(Property.class, propertyId);
            if (property != null) {
                entityManager.getTransaction().begin();
                property.setDeletedProperty(true);
                entityManager.merge(property);
                entityManager.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            log.debug("Could not safely delete Property with ID: " + propertyId, e);
            entityManager.getTransaction().rollback();
        }
        return false;
    }

    public Optional<Property> update(Property property) {
        try {
            entityManager.getTransaction().begin();
            Property p = entityManager.find(Property.class, property.getPropertyId());
            if (p != null) {

                p.setAddress(property.getAddress());
                p.setYearOfConstruction(property.getYearOfConstruction());
                p.setOwner(property.getOwner());

                entityManager.merge(p);
                entityManager.getTransaction().commit();
                return Optional.of(p);
            } else {
                log.debug("Property with ID: " + property.getPropertyId() + " not found for update.");
            }
        } catch (Exception e) {
            log.debug("Could not update Property", e);
            entityManager.getTransaction().rollback();
        }
        return Optional.empty();
    }
}


