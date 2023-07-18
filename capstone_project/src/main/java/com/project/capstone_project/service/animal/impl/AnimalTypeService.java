package com.example.codegymfoods.service.animal.impl;

import com.example.codegymfoods.model.animal.AnimalType;
import com.example.codegymfoods.repository.animal.IAnimalTypeRepository;
import com.example.codegymfoods.service.animal.IAnimalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalTypeService implements IAnimalTypeService {
    @Autowired
    IAnimalTypeRepository animalTypeRepository;

    @Override
    public List<AnimalType> getAll() {
        return animalTypeRepository.findAll();
    }
}
