package com.example.codegymfoods.service.cart.impl;

import com.example.codegymfoods.dto.cart.CartDTO;
import com.example.codegymfoods.dto.animal.AnimalFromCartDTO;
import com.example.codegymfoods.model.animal.Animal;
import com.example.codegymfoods.service.cart.ICartService;
import com.example.codegymfoods.service.animal.IAnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartService implements ICartService {
    @Autowired
    private IAnimalService animalService;

    @Override
    public double totalBill(List<AnimalFromCartDTO> animalFromCartDTOList) {
        long totalBill =0;
        for (int i = 0; i < animalFromCartDTOList.size(); i++) {
            totalBill+= animalFromCartDTOList.get(i).getTotalPrice();
        }
        return totalBill;
    }

    @Override
    public boolean checkQuantity(int id, int quantity) {
        Animal animal = animalService.getById(id);
        if (animal.getQuantity() >= quantity){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void addToCart(Animal animal, CartDTO cartDTO) {
        boolean flag = false;
        for (Map.Entry<Integer, Integer> entry : cartDTO.getSelectedAnimals().entrySet()) {
            if (entry.getKey().equals(animal.getId())) {
                entry.setValue(entry.getValue() + 1);
                flag = true;
            }
        }
        if (!flag) {
            cartDTO.getSelectedAnimals().put(animal.getId(), 1);
        }
    }

    @Override
    public void changeQuantity(int id, int quantity, CartDTO cartDTO) {
        for (Map.Entry<Integer, Integer> entry : cartDTO.getSelectedAnimals().entrySet()) {
            if (entry.getKey() == id) {
                if (quantity <= 0) {
                    Map<Integer, Integer> cartMap = cartDTO.getSelectedAnimals();
                    cartMap.remove(id);
                    return;
                } else {
                    entry.setValue(quantity);
                }
            }
        }
    }
}
