package org.F105540.Resident;

import jakarta.transaction.Transactional;
import org.F105540.exceptions.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final ModelMapper modelMapper = new ModelMapper();


    public ResidentService(ResidentRepository residentRepository) {
        this.residentRepository = residentRepository;
    }

    @Transactional
    public List<DtoResident> getAllResidents() {
        return residentRepository.findAll().stream()
                .map(resident -> modelMapper.map(resident, DtoResident.class))
                .toList();
    }

    @Transactional
    public DtoResident getResidentById(int id) {
        return modelMapper.map(residentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resident", id)), DtoResident.class);
        }

    @Transactional
    public DtoResident createResident(DtoResident resident) {
        return modelMapper.map(residentRepository.save(modelMapper.map(resident, Resident.class)), DtoResident.class);
    }

    @Transactional
    public DtoResident editResident(int id, DtoResident resident) {
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        Resident existingResident = residentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resident", id));

        modelMapper.map(resident, existingResident);
        
        Resident updatedResident = residentRepository.save(existingResident);

        return modelMapper.map(updatedResident, DtoResident.class);
        }


    @Transactional
    public void deleteResident(int id) {
        residentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resident", id));
        residentRepository.deleteById(id);
    }

  

}