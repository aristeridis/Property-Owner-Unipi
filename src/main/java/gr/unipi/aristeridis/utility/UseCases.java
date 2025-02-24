package gr.unipi.aristeridis.utility;

import gr.unipi.aristeridis.exceptions.OwnerNotFoundException;
import gr.unipi.aristeridis.model.Owner;
import gr.unipi.aristeridis.repositories.OwnerRepository;
import gr.unipi.aristeridis.services.UserService;

import static java.io.File.separator;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class UseCases {

    private static final OwnerRepository ownerRepo = new OwnerRepository();

    private static final UserService userService = new UserService(ownerRepo);

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    private static Owner owner;
    private static final Scanner scanner = new Scanner(System.in);

    public static void dataPopulation() {
        System.out.println("|-----------------------------START-----------------------------------|");
        System.out.println("|-----------------------------USE CASE 4.1-----------------------------|");

        userService.readOwnersCsv("data" + separator + "owner.csv");

        System.out.println();
        System.out.println("|-----------------------------data population achieved-----------------------------------|");
    }

    public static List<Owner> selectAllOwners() {
        List<Owner> users;
        try {
            UserService userService = new UserService(ownerRepo);
            users = userService.getAllOwners();
        } catch (Exception e) {
            throw new OwnerNotFoundException("Owner not Found");
        }
        for (Owner user : users) {
            System.out.println("Owner: " + user.toString());

            System.out.println("|-----------------------------END-----------------------------------|");

        }
        return users;
    }
    public static void deleteOwnerById(Long id){
        UserService userService=new UserService(ownerRepo);
        userService.deleteById(id);
    }

}
