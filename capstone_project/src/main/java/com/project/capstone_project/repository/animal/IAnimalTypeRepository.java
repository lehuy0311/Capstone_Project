package com.example.codegymfoods.repository.animal;

import com.example.codegymfoods.model.animal.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnimalTypeRepository extends JpaRepository<AnimalType,Integer> {
}
