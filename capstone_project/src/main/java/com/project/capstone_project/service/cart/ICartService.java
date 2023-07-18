package com.example.codegymfoods.service.cart;

import com.example.codegymfoods.dto.animal.AnimalFromCartDTO;
import com.example.codegymfoods.dto.cart.CartDTO;
import com.example.codegymfoods.model.animal.Animal;

import java.util.List;

public interface ICartService {

    void addToCart(Animal animal, CartDTO cartDTO);
    void changeQuantity(int id, int quantity, CartDTO cartDTO);

    double totalBill(List<AnimalFromCartDTO> animalFromCartDTOList);

    boolean checkQuantity(int id, int quantity);
}
