package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.dto.ItemDTO;
import com.intellias.basicsandbox.service.mapper.ItemMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
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
    public final static String PATH = "items";
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    private final ItemService itemService;


    @PostMapping
    public ResponseEntity<ItemDTO> save(@Valid @RequestBody final ItemDTO item) {
        ItemEntity savedItem = itemService.save(itemMapper.toEntity(item));
        return new ResponseEntity<>(itemMapper.toDTO(savedItem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> update(@PathVariable("id") final UUID id, @Valid @RequestBody final ItemDTO item) {
        ItemEntity updatedItem =  itemService.update(id, itemMapper.toEntity(item));
        return new ResponseEntity<>(itemMapper.toDTO(updatedItem), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getById(@PathVariable("id") final UUID id) {
        ItemEntity item = itemService.getById(id);
        return new ResponseEntity<>(itemMapper.toDTO(item), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> getAll() {
        List<ItemDTO> items = itemService.findAll()
                .stream()
                .map(itemMapper::toDTO)
                .toList();

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final UUID id) {
        itemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
