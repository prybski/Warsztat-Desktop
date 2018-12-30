package pl.edu.pwsztar.util;

import pl.edu.pwsztar.entity.Client;
import pl.edu.pwsztar.entity.Part;
import pl.edu.pwsztar.entity.Vehicle;

import java.util.List;

public class ConstraintCheckUtil {

    public static boolean checkForDuplicateVinNumber(List<Vehicle> vehicles, String vinNumber, int vehicleId) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getId() != vehicleId && vinNumber.equals(vehicle.getVinNumber())) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkForDuplicateVinNumber(List<Vehicle> vehicles, String vinNumber) {
        for (Vehicle vehicle : vehicles) {
            if (vinNumber.equals(vehicle.getVinNumber())) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkForDuplicateEmail(List<Client> clients, String email) {
        for (Client client : clients) {
            if (email.equals(client.getEmail())) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkForDuplicatePhoneNumber(List<Client> clients, String phoneNumber) {
        for (Client client : clients) {
            if (phoneNumber.equals(client.getPhoneNumber())) {
                return true;
            }
        }

        return false;
    }

    public static boolean checkForDuplicateDevelopmentCode(List<Part> parts, String developmentCode) {
        for (Part part : parts) {
            if (developmentCode.equals(part.getDevelopmentCode())) {
                return true;
            }
        }

        return false;
    }
}
