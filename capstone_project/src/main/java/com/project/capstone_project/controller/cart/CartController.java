package com.example.codegymfoods.controller.cart;


import com.example.codegymfoods.dto.animal.AnimalFromCartDTO;
import com.example.codegymfoods.dto.cart.CartDTO;
import com.example.codegymfoods.model.animal.Animal;
import com.example.codegymfoods.model.animal.AnimalType;
import com.example.codegymfoods.model.bill.Bill;
import com.example.codegymfoods.model.bill.BillDetail;
import com.example.codegymfoods.model.customer.Customer;
import com.example.codegymfoods.service.bill.IBillDetailService;
import com.example.codegymfoods.service.bill.IBillService;
import com.example.codegymfoods.service.cart.ICartService;
import com.example.codegymfoods.service.customer.ICustomerService;
import com.example.codegymfoods.service.animal.IAnimalService;
import com.example.codegymfoods.service.animal.IAnimalTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ICartService cartService;
    @Autowired
    private IAnimalService animalService;
    @Autowired
    private IAnimalTypeService animalTypeService;
    @Autowired
    private IBillService billService;
    @Autowired
    private IBillDetailService billDetailService;

    @GetMapping("")
    public String disPlayBlog(Model model, @PageableDefault() Pageable pageable) {
        Page<Animal> animalList = animalService.getBlogPage(pageable);
        List<AnimalType> animalTypeList = animalTypeService.getAll();
        model.addAttribute("animalTypeList", animalTypeList);
        model.addAttribute("animalList", animalList);
        return "redirect:/home/success";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable(name = "id") int id, @SessionAttribute(name = "cartDTO") CartDTO cartDTO, RedirectAttributes redirectAttributes) {
        Animal animal = animalService.getById(id);
        cartService.addToCart(animal, cartDTO);
        redirectAttributes.addFlashAttribute("message", "Thêm vào giỏ hàng thành công");
        return "redirect:/home/success";
    }

    @GetMapping("/changeQuantity")
    public String changeQuantity(@RequestParam(value = "id") int id,
                                 @RequestParam(value = "quantity") int quantity,
                                 @SessionAttribute(name = "cartDTO") CartDTO cartDTO, Model model,
                                 RedirectAttributes redirectAttributes) {
        if (cartService.checkQuantity(id, quantity)) {
            cartService.changeQuantity(id, quantity, cartDTO);
            redirectAttributes.addFlashAttribute("message", "Cập nhật số lượng thành công");
        } else {
            Animal animal = animalService.getById(id);
            model.addAttribute("quantityOfAnimal", animal.getQuantity());
            redirectAttributes.addFlashAttribute("message", "Hiện sản phẩm còn: \n" + animal.getQuantity() + " sản phẩm");
        }
        return "redirect:/cart/home";
    }

    @GetMapping("/deleteInCart")
    public String delete(@RequestParam(value = "idDelete") int id, @SessionAttribute(name = "cartDTO") CartDTO cartDTO,
                         RedirectAttributes redirectAttributes) {
        Map<Integer, Integer> cartMap = cartDTO.getSelectedAnimals();
        cartMap.remove(id);
        redirectAttributes.addFlashAttribute("message", "Xoá sản phẩm thành công");

        return "redirect:/cart/home";
    }

    @GetMapping("/home")
    public String getAnimalsFromCart(@SessionAttribute(name = "cartDTO") CartDTO cartDTO, Model model) {
        Set<Integer> animalIds = cartDTO.getSelectedAnimals().keySet();
        Map<Integer, Animal> mapAnimals = animalService.getAnimalsById(animalIds).stream().collect(Collectors.toMap(Animal::getId, animal -> animal));
        List<AnimalFromCartDTO> animalFromCartDTOList = cartDTO.getSelectedAnimals().entrySet().stream()
                .map(e -> new AnimalFromCartDTO(e.getKey(),
                        mapAnimals.get(e.getKey()).getPicture(),
                        mapAnimals.get(e.getKey()).getName(),
                        mapAnimals.get(e.getKey()).getPrice(),
                        e.getValue(),
                        mapAnimals.get(e.getKey()).getPrice() * e.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        double totalBill = cartService.totalBill(animalFromCartDTOList);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByUsername(user.getUsername());
        model.addAttribute("customer", customer.getName());
        model.addAttribute("totalBill", totalBill);
        model.addAttribute("animalFromCartDTOList", animalFromCartDTOList);
        return "/cart";
    }

    @GetMapping("/payment/{totalPrice}")
    String payment(@PathVariable(value = "totalPrice") double totalPrice, @SessionAttribute(name = "cartDTO") CartDTO cartDTO,
                   RedirectAttributes redirectAttributes) {
        Set<Integer> animalIds = cartDTO.getSelectedAnimals().keySet();
        Map<Integer, Animal> mapAnimals = animalService.getAnimalsById(animalIds).stream().collect(Collectors.toMap(Animal::getId, animal -> animal));
        List<AnimalFromCartDTO> animalFromCartDTOList = cartDTO.getSelectedAnimals().entrySet().stream()
                .map(e -> new AnimalFromCartDTO(e.getKey(),
                        mapAnimals.get(e.getKey()).getPicture(),
                        mapAnimals.get(e.getKey()).getName(),
                        mapAnimals.get(e.getKey()).getPrice(),
                        e.getValue(),
                        mapAnimals.get(e.getKey()).getPrice() * e.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        animalService.reduceQuantity(animalFromCartDTOList);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByUsername(user.getUsername());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(currentDate);
        Bill bill = new Bill(date, true, totalPrice, customer);
        billService.saveBill(bill);
        for (int i = 0; i < animalFromCartDTOList.size(); i++) {
            Animal animal = animalService.getById(animalFromCartDTOList.get(i).getId());
            BillDetail billDetail = new BillDetail(animalFromCartDTOList.get(i).getQuantityAnimalFromCartDTO(), bill, animal);
            billDetailService.saveBillDetail(billDetail);
        }
        Map<Integer, Integer> cartMap = cartDTO.getSelectedAnimals();
        cartMap.clear();
        redirectAttributes.addFlashAttribute("message", "Thanh toán thành công");

        return "redirect:/home/success";
    }

    @GetMapping("/saveBill/{totalPrice}")
    String saveBill(@PathVariable(value = "totalPrice") double totalPrice, @SessionAttribute(name = "cartDTO") CartDTO cartDTO,
                    RedirectAttributes redirectAttributes) {
        Set<Integer> animalIds = cartDTO.getSelectedAnimals().keySet();
        Map<Integer, Animal> mapAnimals = animalService.getAnimalsById(animalIds).stream().collect(Collectors.toMap(Animal::getId, animal -> animal));
        List<AnimalFromCartDTO> animalFromCartDTOList = cartDTO.getSelectedAnimals().entrySet().stream()
                .map(e -> new AnimalFromCartDTO(e.getKey(),
                        mapAnimals.get(e.getKey()).getPicture(),
                        mapAnimals.get(e.getKey()).getName(),
                        mapAnimals.get(e.getKey()).getPrice(),
                        e.getValue(),
                        mapAnimals.get(e.getKey()).getPrice() * e.getValue()))
                .collect(Collectors.toCollection(LinkedList::new));
        animalService.reduceQuantity(animalFromCartDTOList);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Customer customer = customerService.findByUsername(user.getUsername());
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(currentDate);
        Bill bill = new Bill(date, false, totalPrice, customer);
        billService.saveBill(bill);

        for (int i = 0; i < animalFromCartDTOList.size(); i++) {
            Animal animal = animalService.getById(animalFromCartDTOList.get(i).getId());
            BillDetail billDetail = new BillDetail(animalFromCartDTOList.get(i).getQuantityAnimalFromCartDTO(), bill, animal);
            billDetailService.saveBillDetail(billDetail);
        }

        Map<Integer, Integer> cartMap = cartDTO.getSelectedAnimals();
        cartMap.clear();
        redirectAttributes.addFlashAttribute("message", "Lưu Hoá đơn thành công");
        return "redirect:/home/success";
    }

    @GetMapping("/paymentAfter/{id}")
    String paymentAfter(@PathVariable(value = "id") int id, @SessionAttribute(name = "cartDTO") CartDTO cartDTO,
                        RedirectAttributes redirectAttributes) {
        Bill bill = billService.getBillById(id);
        bill.setStatus(true);
        billService.saveBill(bill);
        Map<Integer, Integer> cartMap = cartDTO.getSelectedAnimals();
        cartMap.clear();
        redirectAttributes.addFlashAttribute("message", "Thanh toán thành công");
        return "redirect:/user/detail";
    }
}
