package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.mapper.ItemMapper;
import delivery.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import delivery.models.Item;
import delivery.models.dto.*;
import delivery.models.mapper.*;
import delivery.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/item")
public class ItemController {

    private final AuthenticationService authenticationService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        var item = itemService.findItemById(id);
        if (item.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var itemDto = itemMapper.mapToDto(item.get());
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping()
    public ResponseEntity<List<ItemDto>> getAllItems() {
        var item = itemService.getAllItems();

        var itemDto = item.stream().map(itemMapper::mapToDto).toList();
        return ResponseEntity.ok(itemDto);
    }

    @PostMapping()
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
        if(itemDto.name() == null || itemService.findItemByName(itemDto.name()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        var item = itemMapper.mapFromDto(itemDto);
        item = itemService.saveItem(item);
        var res = itemMapper.mapToDto(item);
        return ResponseEntity.ok(res);
    }

    // Обновление информации о товаре
    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        var item = itemService.findItemById(id);
        if (item.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(itemDto.name() == null || itemService.findItemByName(itemDto.name()).isPresent()){
            return ResponseEntity.badRequest().build();
        }
        var updatedItem = mergeItems(item.get(), itemDto);
        updatedItem = itemService.updateItem(updatedItem);
        var res = itemMapper.mapToDto(updatedItem);
        return ResponseEntity.ok(res);
    }

    // Удаление товара
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if(itemService.findItemById(id).isEmpty()){
            return ResponseEntity.badRequest().build();
        }
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    public Item mergeItems(Item oldItem, ItemDto newItem) {
        oldItem.setName(newItem.name());
        return oldItem;
    }

}