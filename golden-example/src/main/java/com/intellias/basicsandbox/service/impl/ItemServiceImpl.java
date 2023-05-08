package com.intellias.basicsandbox.service.impl;

//import com.intellias.basicsandbox.persistence.entity.ItemEntity;
import com.intellias.basicsandbox.service.ItemService;
import com.intellias.basicsandbox.service.dto.ItemDTO;
import com.intellias.basicsandbox.service.mapper.ItemMapper;
//import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
//import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    //private final ItemRepository itemRepository;

    @Override
    public ItemDTO save(ItemDTO item) {
//       ItemEntity itemEntity = itemMapper.toEntity(item);
//       final ItemEntity savedEntity = itemRepository.save(itemEntity);
//       return itemMapper.toDTO(savedEntity);

        return null;
    }

    @Override
    public void update(ItemDTO item) {
//        itemRepository.updatePost(item.getName(), item.getId());
    }

    @Override
    public ItemDTO getById(UUID id) {
//        final ItemEntity entity = itemRepository.getById(id)
//                .orElseThrow(EntityNotFoundException::new);
//        return itemMapper.toDTO(entity);
        return null;
    }

    @Override
    public List<ItemDTO> findAll() {
//      return itemRepository.findAll(pageable);
        return List.of();
    }

    @Override
    public void delete(UUID id) {
//        itemRepository.deleteById(id);
    }
}
