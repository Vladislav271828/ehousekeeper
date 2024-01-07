package org.F105540.Apartment;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.F105540.Building.Building;
import org.F105540.Building.BuildingRepository;
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

import static java.util.Collections.emptyList;

@AllArgsConstructor
@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    private final ResidentRepository residentRepository;
    private final OwnerRepository ownerRepository;
    private final CompanyRepository companyRepository;
    private final BuildingRepository buildingRepository;
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

    @Transactional
    public DtoApartment createApartmentInBuilding(DtoApartment apartment, Integer buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new EntityNotFoundException("Building", buildingId));
        if (apartment.getFloor() != null && apartment.getFloor() > building.getNumberOfFloors()) {
            throw new IllegalArgumentException("floor of apartment can't be bigger than " + building.getNumberOfFloors());
        }
        apartment.setOwner(null);
        apartment.setBuilding(null);
        apartment.setResidents(emptyList());
        Apartment newApartment = modelMapper.map(apartment, Apartment.class);
        newApartment.setBuilding(building);

        return modelMapper.map(apartmentRepository.save(newApartment), DtoApartment.class);
    }

    @Transactional
    public DtoApartment editApartment(Integer apartmentId, DtoApartment apartment) {
        apartment.setOwner(null);
        apartment.setBuilding(null);
        apartment.setResidents(null);
        if (apartment.getFloor() != null && apartment.getBuilding() != null && apartment.getFloor() > apartment.getBuilding().getNumberOfFloors()) {
            throw new IllegalArgumentException("floor of apartment can't be bigger than " + apartment.getBuilding().getNumberOfFloors());
        }
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        Apartment existingApartment = apartmentRepository.findById(apartmentId)
                .orElseThrow(() -> new EntityNotFoundException("Apartment", apartmentId));

        modelMapper.map(apartment, existingApartment);

        return modelMapper.map(apartmentRepository.save(existingApartment), DtoApartment.class);
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
        if (resident.getApartments().contains(apartment))  throw new IllegalArgumentException("Resident already resides in this apartment");
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
        if (!resident.getApartments().contains(apartment))  throw new IllegalArgumentException("Resident is not resident of this apartment in the first place");
        apartment.getResidents().remove(resident);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }
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
        apartment.setTaxIsPaid(true);
        Company company = apartment.getBuilding().getEmployee().getCompany();
        company.setIncome(company.getIncome() + tax);
        companyRepository.save(company);
        PaymentLogger.logPaymentDetails(apartment, tax);
        return modelMapper.map(apartmentRepository.save(apartment), DtoApartment.class);
    }


}
