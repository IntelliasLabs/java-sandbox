package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.dto.ItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ItemController.PATH)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ItemController {
    final static String PATH = "items";
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> save(@Valid @RequestBody final ItemDTO item) {
        return new ResponseEntity<>(itemService.save(item), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Void> update(@Valid @RequestBody final ItemDTO item) {
        itemService.update(item);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getById(@PathVariable("id") final UUID id) {
        return new ResponseEntity<>(itemService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAll() {
        return new ResponseEntity<>(itemService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final UUID id) {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
