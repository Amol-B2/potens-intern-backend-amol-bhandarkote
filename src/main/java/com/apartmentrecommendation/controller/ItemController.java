package com.apartmentrecommendation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.apartmentrecommendation.entity.Apartment;
import com.apartmentrecommendation.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<Apartment> getItems() {
        return itemService.findAll();
    }

    @GetMapping("/{itemId}")
    public Apartment getItem(@PathVariable String itemId) {
        return itemService.findById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Apartment createItem(@Valid @RequestBody Apartment apartment) {
        return itemService.create(apartment);
    }

    @PutMapping("/{itemId}")
    public Apartment updateItem(@PathVariable String itemId, @Valid @RequestBody Apartment apartment) {
        return itemService.update(itemId, apartment);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable String itemId) {
        itemService.delete(itemId);
    }
}
