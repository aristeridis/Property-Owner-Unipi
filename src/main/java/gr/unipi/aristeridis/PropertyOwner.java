package gr.unipi.aristeridis;

import gr.unipi.aristeridis.utility.JPAUtil;
import gr.unipi.aristeridis.utility.UseCases;

import java.text.ParseException;
import java.util.Scanner;

public class PropertyOwner {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try {
            while (true) {
                System.out.println("Welcome to Property Owner");
                System.out.println("1. Populate Database");
                System.out.println("2. Get all Owners");
                System.out.println("3. Select Owner to delete with owner Id");
                System.out.println("4.Exit");

                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        UseCases.dataPopulation();
                        break;
                    case 2:
                        UseCases.selectAllOwners();
                        break;
                    case 3:
                        UseCases.deleteOwnerById(3L);
                        break;
                    case 4:
                        JPAUtil.shutdown();
                        System.out.println("EXIT");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("");
                }
            }
        } finally {
            scanner.close();
        }
    }
}