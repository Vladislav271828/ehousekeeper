package org.F105540.Apartment;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.F105540.Owner.Owner;
import org.F105540.Owner.OwnerRepository;
import org.F105540.Resident.Resident;
import org.F105540.Resident.ResidentRepository;
import org.F105540.company.Company;
import org.F105540.company.CompanyRepository;
import org.F105540.exceptions.EntityNotFoundException;
import org.F105540.exceptions.InvalidInputException;
import org.F105540.paymentLoogger.PaymentLogger;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final OwnerRepository ownerRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    @Transactional
    public List<DtoApartment> getAllApartments() {
        return apartmentRepository.findAll().stream()
                .map(apartment -> modelMapper.map(apartment, DtoApartment.class))
                .toList();
    }

    @Transactional
    public DtoApartment getApartmentById(int id) {
        return modelMapper.map(apartmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment", id)), DtoApartment.class);
    }

//    @Transactional
//    public DtoApartment createApartment(DtoApartment apartment) {
//        return  modelMapper.map(apartmentRepository.save(modelMapper.map(apartment, Apartment.class)), DtoApartment.class);
//    }
//TODO: add floor check to apartment editing and others
    @Transactional
    public DtoApartment editApartment(Integer apartmentId, DtoApartment apartment) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", apartmentId));

        modelMapper.map(apartment, existingApartment);

        Apartment updatedApartment = apartmentRepository.save(existingApartment);

        return modelMapper.map(updatedApartment, DtoApartment.class);
    }

    @Transactional
    public DtoApartment addOwnerToApartment(Integer ownerId, Integer apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", apartmentId));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner", ownerId));
        apartment.setOwner(owner);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }

    @Transactional
    public DtoApartment removeOwnerFromApartment(Integer ownerId, Integer apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", apartmentId));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Owner", ownerId));
        apartment.setOwner(null);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }

    @Transactional
    public DtoApartment addResidentToApartment(Integer residentId, Integer apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", apartmentId));
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new EntityNotFoundException("Resident", residentId));
        apartment.getResidents().add(resident);
        resident.getApartments().add(apartment);
        residentRepository.save(resident);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }

    @Transactional
    public DtoApartment removeResidentFromApartment(Integer residentId, Integer apartmentId) {
        Apartment apartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", apartmentId));
        Resident resident = residentRepository.findById(residentId)
                .orElseThrow(() -> new EntityNotFoundException("Resident", residentId));
        apartment.getResidents().remove(resident);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }
//TODO: check everything works when deleting apartment or resident thats still connected
    @Transactional
    public void deleteApartment(int id) {
        apartmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Apartment", id));
        apartmentRepository.deleteById(id);
    }

    @Transactional
    public double calculateTax(int id){
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", id));
        double tax = (apartment.getArea() * apartment.getBuilding().getTaxPerArea());
        tax = tax + residentRepository.findNumberOfResidentsInApartmentOlderThanSevenAndUsingElevator(apartment.getId()) * apartment.getBuilding().getTaxPerElevatorPerson();
        if (apartment.isHasPet()) tax = tax + apartment.getBuilding().getTaxForPet();
        return tax;
    }

    @Transactional
    public DtoApartment payTax(int id){
        Apartment apartment = apartmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", id));
        if (apartment.isTaxIsPaid()) throw new InvalidInputException("Tax is already paid for this apartment");
        double tax = calculateTax(id);
        //insert function for transferring money
        //TODO: write payment
        apartment.setTaxIsPaid(true);
        Company company = apartment.getBuilding().getEmployee().getCompany();
        company.setIncome(company.getIncome() + tax);
        companyRepository.save(company);
        PaymentLogger.logPaymentDetails(apartment, tax);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }


}
