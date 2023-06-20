package com.intellias.basicsandbox.controller;

import com.intellias.basicsandbox.controller.dto.ErrorDTO;
import com.intellias.basicsandbox.controller.dto.ItemDTO;
import com.intellias.basicsandbox.controller.dto.LocalizedItemDTO;
import com.intellias.basicsandbox.controller.mapper.ItemMapper;
import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ItemController.API_VERSION + ItemController.PATH)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Slf4j
public class ItemController {
    public final static String PATH = "items";
    public final static String API_VERSION = "/api/v1/";

    private final ItemService itemService;

    @Operation(summary = "Create new item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created item",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)) }) })
    @PostMapping
    public ResponseEntity<ItemDTO> save(@Valid @RequestBody final ItemDTO item) {
        log.debug("Item to save: {}", item);
        final ItemEntity savedItem = itemService.save(ItemMapper.INSTANCE.toEntity(item));
        log.info("Item successfully saved: {}", savedItem);

        return new ResponseEntity<>(ItemMapper.INSTANCE.toDTO(savedItem), HttpStatus.CREATED);
    }

    @Operation(summary = "Change item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated item",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)) }) })
    @PutMapping("/{id}")
    public ItemDTO update(@PathVariable("id") final UUID id, @Valid @RequestBody final ItemDTO item) {
        log.debug("Item with id {} to update: {}", id, item);
        final ItemEntity updatedItem = itemService.update(id, ItemMapper.INSTANCE.toEntity(item));
        log.info("Item with id {} successfully updated: {}", id, updatedItem);
        return ItemMapper.INSTANCE.toDTO(updatedItem);
    }

    @Operation(summary = "Find item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found item",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)) }) })
    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable("id") final UUID id) {
        final ItemEntity item = itemService.getById(id);

        return ItemMapper.INSTANCE.toDTO(item);
    }

    @Operation(summary = "Find item by id with localized fields (currency code)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found item",
                    content = {@Content(mediaType = "application/hal+json;charset=utf8", schema = @Schema(implementation = ItemDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Error",
                    content = {@Content(mediaType = "application/hal+json;charset=utf8", schema = @Schema(implementation = ErrorDTO.class)) }) })
    // UTF-8 response encoding is important for serialization of translated values
    @GetMapping(value = "/{id}/{locale}", produces = "application/hal+json;charset=utf8")
    public LocalizedItemDTO getByIdLocalized(@PathVariable("id") final UUID id,
            @PathVariable("locale") final Locale locale) {
        final ItemEntity item = itemService.getById(id);
        ResourceBundle localizedResource = ResourceBundle.getBundle("resourcebundle.messages", locale);

        ItemDTO itemDTO = ItemMapper.INSTANCE.toDTO(item);
        final String localizationKeyPrefix = "currency.";
        String translatedValue = localizedResource.getString(localizationKeyPrefix + itemDTO.getCurrencyCode());
        return new LocalizedItemDTO(itemDTO, translatedValue);
    }

    @Operation(summary = "Get all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found items",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Error",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class)) }) })
    @GetMapping
    public List<ItemDTO> getAll() {
        List<ItemDTO> items = itemService.findAll()
                .stream()
                .map(ItemMapper.INSTANCE::toDTO)
                .toList();

        log.info("Items found: {}", items);
        return items;
    }

    @Operation(summary = "Delete item by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item deleted",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ItemDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Item not found", content = @Content) })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") final UUID id) {
        log.debug("Item with id {} to delete", id);
        itemService.delete(id);
        log.info("Item with id {} successfully deleted", id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
