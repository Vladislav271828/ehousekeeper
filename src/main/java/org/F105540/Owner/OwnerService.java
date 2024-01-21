package org.F105540.Owner;

import jakarta.transaction.Transactional;
import org.F105540.Apartment.Apartment;
import org.F105540.Apartment.ApartmentRepository;
import org.F105540.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class OwnerService {

  private final OwnerRepository ownerRepository;
  private final ApartmentRepository apartmentRepository;
  private final ModelMapper modelMapper = new ModelMapper();


  public OwnerService(OwnerRepository ownerRepository, ApartmentRepository apartmentRepository) {
    this.ownerRepository = ownerRepository;
    this.apartmentRepository = apartmentRepository;
  }

  @Transactional
  public List<DtoOwner> getAllOwners() {
    return ownerRepository.findAll().stream()
            .map(owner -> modelMapper.map(owner, DtoOwner.class))
            .toList();
  }

  @Transactional
  public DtoOwner getOwnerById(int id) {
    return modelMapper.map(ownerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Owner", id)), DtoOwner.class);

  }

  @Transactional
  public DtoOwner createOwner(DtoOwner owner) {
    owner.setApartments(emptyList());
    return modelMapper.map(ownerRepository.save(modelMapper.map(owner, Owner.class)), DtoOwner.class);
  }

  @Transactional
  public DtoOwner editOwner(int id, DtoOwner owner) {
    owner.setApartments(null);
    modelMapper.getConfiguration().setSkipNullEnabled(true);
    Owner existingOwner = ownerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Owner", id));

    modelMapper.map(owner, existingOwner);
    
    Owner updatedOwner = ownerRepository.save(existingOwner);

    return modelMapper.map(updatedOwner, DtoOwner.class);
    }

  @Transactional
  public void deleteOwner(int id) {

    Owner owner = ownerRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Owner", id));
    if (owner.getApartments() != null) for (Apartment apartment : owner.getApartments()) {
      apartment.setOwner(null);
      apartmentRepository.save(apartment);
    };
    ownerRepository.deleteById(id);
  }

  
}
