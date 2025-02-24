package gr.unipi.aristeridis.services;


import gr.unipi.aristeridis.model.Property;
import gr.unipi.aristeridis.repositories.OwnerRepository;
import gr.unipi.aristeridis.repositories.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
public class UserService {

    private final PropertyRepository propertyRepository;
    private final OwnerRepository ownerRepository;



    public UserService(PropertyRepository propertyRepository, OwnerRepository ownerRepository) {
        this.propertyRepository = propertyRepository;
        this.ownerRepository = ownerRepository;
    }

    public List<Property> getPropertiesByOwnerId(Long ownerId) {
        return propertyRepository.findByOwnerId(ownerId);
    }
}
