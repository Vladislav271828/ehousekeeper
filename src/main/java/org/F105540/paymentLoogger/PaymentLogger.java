package org.F105540.paymentLoogger;

import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.DtoApartment;
import org.F105540.Building.Building;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PaymentLogger {

    private static final String FILE_PATH = "payment_log.txt";

    public static  void logPaymentDetails(Apartment apartment, double paymentAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("Name of company: " + apartment.getBuilding().getCompany().getName() + ", ");
            writer.write("Name of employee: " + apartment.getBuilding().getEmployee().getName() + ", ");
            writer.write("Address: " + apartment.getBuilding().getAddress() + ", ");
            writer.write("Apartment Number: " + apartment.getNumber() + ", ");
            writer.write("Payment Amount: " + paymentAmount + ", ");
            writer.write("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + "\n");
        } catch (IOException e) {
            throw new UncheckedIOException("Error writing payment details to file", e);
        }
    }
}

