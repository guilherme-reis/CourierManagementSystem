import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Pattern;


abstract class Package {
    private String trackingID;
    private String destination;
    private double weight;

    public Package(String trackingID, String destination, double weight) {
        if (!validateTrackingID(trackingID)) {
            throw new IllegalArgumentException("Invalid tracking ID format. Must be like PKG12345.");
        }
        if (!validateDestination(destination)) {
            throw new IllegalArgumentException("Invalid destination format. Must include street name and number.");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be a positive number.");
        }
        this.trackingID = trackingID;
        this.destination = destination;
        this.weight = weight;
    }

    public String getTrackingID() {
        return trackingID;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public abstract double calculateShippingCost();

    public static boolean validateTrackingID(String trackingID) {
        return Pattern.matches("PKG\\d{5}", trackingID);
    }

    public static boolean validateDestination(String destination) {
        return Pattern.matches("\\d+\\s+.+", destination);
    }

    @Override
    public String toString() {
        return "Tracking ID: " + trackingID + " | Destination: " + destination +
                " | Weight: " + weight + " | Cost: $" + String.format("%.2f", calculateShippingCost());
    }
}

class StandardPackage extends Package {
    public StandardPackage(String trackingID, String destination, double weight) {
        super(trackingID, destination, weight);
    }

    @Override
    public double calculateShippingCost() {
        return getWeight() * 2.5;
    }
}

class ExpressPackage extends Package {
    public ExpressPackage(String trackingID, String destination, double weight) {
        super(trackingID, destination, weight);
    }

    @Override
    public double calculateShippingCost() {
        return getWeight() * 4.0;
    }
}

class CourierManager {
    private ArrayList<Package> packages = new ArrayList<>();

    public void addPackage(Package pkg) {
        packages.add(pkg);
    }

    public void displayPackages() {
        if (packages.isEmpty()) {
            System.out.println("No packages available.");
            return;
        }
        for (Package pkg : packages) {
            System.out.println(pkg);
        }
    }

    public void sortPackagesByWeight() {
        for (int i = 0; i < packages.size() - 1; i++) {
            for (int j = 0; j < packages.size() - i - 1; j++) {
                if (packages.get(j).getWeight() > packages.get(j + 1).getWeight()) {
                    Collections.swap(packages, j, j + 1);
                }
            }
        }
    }

    public Package searchPackageByTrackingID(String trackingID) {

        packages.sort((p1, p2) -> p1.getTrackingID().compareTo(p2.getTrackingID()));

        int low = 0, high = packages.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Package midPackage = packages.get(mid);
            if (midPackage.getTrackingID().equals(trackingID)) {
                return midPackage;
            } else if (midPackage.getTrackingID().compareTo(trackingID) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }
}

public class DeliveryDilemmas {
    public static void main(String[] args) {
        CourierManager manager = new CourierManager();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("===============================");
            System.out.println(" Welcome to Delivery Dilemmas!");
            System.out.println("===============================");
            System.out.println("1. Add a new package");
            System.out.println("2. Display all packages and shipping costs");
            System.out.println("3. Sort packages by weight");
            System.out.println("4. Search for a package by Tracking ID");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Enter package type (Standard/Express): ");
                        String type = scanner.nextLine();
                        System.out.print("Enter tracking ID: ");
                        String trackingID = scanner.nextLine();
                        System.out.print("Enter destination: ");
                        String destination = scanner.nextLine();
                        System.out.print("Enter weight: ");
                        double weight = scanner.nextDouble();

                        Package pkg = type.equalsIgnoreCase("Standard")
                                ? new StandardPackage(trackingID, destination, weight)
                                : new ExpressPackage(trackingID, destination, weight);

                        manager.addPackage(pkg);
                        System.out.println("Package added successfully!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    manager.displayPackages();
                    break;

                case 3:
                    System.out.println("Sorting packages by weight...");
                    manager.sortPackagesByWeight();
                    manager.displayPackages();
                    break;

                case 4:
                    System.out.print("Enter Tracking ID: ");
                    String searchID = scanner.nextLine();
                    Package foundPackage = manager.searchPackageByTrackingID(searchID);
                    if (foundPackage != null) {
                        System.out.println("Package Found: " + foundPackage);
                    } else {
                        System.out.println("No package found with the given Tracking ID.");
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using Delivery Dilemmas!");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }
}
