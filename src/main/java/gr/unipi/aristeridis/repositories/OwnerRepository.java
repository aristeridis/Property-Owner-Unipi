package gr.unipi.aristeridis.repositories;

import gr.unipi.aristeridis.exceptions.OwnerNotFoundException;
import gr.unipi.aristeridis.model.Owner;
import gr.unipi.aristeridis.utility.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OwnerRepository {

    private final EntityManager entityManager;

    public OwnerRepository() {
        entityManager = JPAUtil.getEntityManager();
    }


    public Optional<Owner> save(Owner owner) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(owner);
            entityManager.getTransaction().commit();
            return Optional.of(owner);
        } catch (Exception e) {
            log.debug("Could not save user");
        }
        return Optional.empty();
    }

    public boolean deleteById(Long ownerId) {
        Owner persistentInstance = entityManager.find(getEntityClass(), ownerId);
        if (persistentInstance != null) {
            try {
                entityManager.getTransaction().begin();
                entityManager.remove(persistentInstance);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                log.debug("Could not delete Owner");
                return false;
            }
            return true;
        }
        return false;
    }

    public List<Owner> findAll() throws OwnerNotFoundException {
        try {

            TypedQuery<Owner> query = entityManager.createQuery(
                    "SELECT o FROM Owner o WHERE o.deletedOwner = false", Owner.class);
            return query.getResultList();
        } catch (OwnerNotFoundException rnfe) {
            System.out.println(rnfe.getMessage());

        }
        return null;
    }

    private Class<Owner> getEntityClass() {
        return Owner.class;
    }

    public boolean safeDeleteById(Long ownerId) {
        Owner persistentInstance = entityManager.find(getEntityClass(), ownerId);
        if (persistentInstance != null) {
            try {
                entityManager.getTransaction().begin();
                persistentInstance.setDeletedOwner(true);
                entityManager.merge(persistentInstance);
                entityManager.getTransaction().commit();
                return true;
            } catch (Exception e) {
                log.debug("Could not safely delete Owner", e);
                entityManager.getTransaction().rollback();
                return false;
            }
        }
        return false;
    }

    public Optional<Owner> update(Owner owner) {
        try {
            entityManager.getTransaction().begin();
            Owner o = entityManager.find(Owner.class, owner.getOwnerId());
            if (o != null) {
                o.setAddress(owner.getAddress());
                o.setEmail(owner.getEmail());
                o.setPassword(owner.getPassword());

                entityManager.merge(o);
                entityManager.getTransaction().commit();
                return Optional.of(o);
            } else {
                log.debug("Owner with ID: " + owner.getOwnerId() + " was not found and cannot be updated.");
            }
        } catch (Exception e) {
            log.debug("Could not update Owner", e);
            entityManager.getTransaction().rollback();
        }
        return Optional.empty();
    }
}

