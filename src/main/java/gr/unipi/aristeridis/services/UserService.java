package gr.unipi.aristeridis.services;

import gr.unipi.aristeridis.model.Owner;
import gr.unipi.aristeridis.repositories.OwnerRepository;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class UserService {

    private final OwnerRepository ownerRepository;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public UserService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public int readOwnersCsv(String filename) {
        int rowsRead = 0;
        try (Scanner scanner = new Scanner(new File(filename))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
//                if (data.length < 9) continue;
                Owner owner = new Owner();
                owner.setUsername(data[1].trim());
                owner.setPassword(data[2].trim());
                owner.setVatNumber(parseLong(data[3]));
                owner.setName(data[4].trim());
                owner.setSurName(data[5].trim());
                owner.setAddress(data[6].trim());
                owner.setPhoneNumber(data[7].trim());
                owner.setEmail(data[8].trim());
                ownerRepository.save(owner);
                rowsRead++;
            }
        } catch (FileNotFoundException e) {
            log.error("Error reading owners from CSV file '{}'", filename, e);
        }
        return rowsRead;
    }

    public List<Owner> getAllOwners() {
        OwnerRepository getOwners = new OwnerRepository();
        return getOwners.findAll();
    }

    public void deleteById(Long id) {
        OwnerRepository owner = new OwnerRepository();
        owner.deleteById(id);
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Invalid number format for value '{}'", value);
            return -1;
        }
    }
}

