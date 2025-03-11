package delivery.controllers;

import delivery.auth.services.AuthenticationService;
import delivery.models.ItemSellerPool;
import delivery.models.Seller;
import delivery.models.dto.*;
import delivery.models.mapper.*;
import delivery.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {

    private final AuthenticationService authenticationService;
    private final SellerService sellerService;
    private final ItemService itemService;
    private final ItemSellerPoolService itemSellerPoolService;

    private final SellerMapper sellerMapper;
    private final ItemMapper itemMapper;

    // Получение информации о клинике для текущего пользователя
    @GetMapping()
    public ResponseEntity<SellerDto> getSeller() {
        var currentUser = authenticationService.getCurrentUser();
        var seller = sellerService.getSellerByUser(currentUser);
        if (seller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var sellerDto = sellerMapper.mapToDto(seller.get());
        sellerDto.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(sellerDto);
    }

    @PostMapping
    public ResponseEntity<SellerDto> updateSellerInfo(@Valid @RequestBody SellerDto sellerDto) {
        var currentUser = authenticationService.getCurrentUser();
        var currentSeller = sellerService.getSellerByUser(currentUser);
        if (currentSeller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedSeller = mergeSellers(currentSeller.get(), sellerDto);
        updatedSeller = sellerService.updateSeller(updatedSeller);
        var res = sellerMapper.mapToDto(updatedSeller);
        res.toBuilder().email(currentUser.getEmail());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/item/{id}")
    public ResponseEntity<ItemDto> addItem(@PathVariable Long id, @RequestBody ItemDto itemDto){
        var item = itemService.findItemById(id);
        if(item.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var seller = sellerService.getSellerByUser(authenticationService.getCurrentUser());
        if(seller.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        if(validateItem(itemDto)){
            return ResponseEntity.badRequest().build();
        }
        var itemPool = ItemSellerPool.builder()
                .item(item.get())
                .seller(seller.get())
                .count(itemDto.count())
                .price(itemDto.price())
                .build();
        itemPool = itemSellerPoolService.save(itemPool);
        var result = itemMapper.mapToDto(itemPool);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto){
        var item = itemService.findItemById(id);
        if(item.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        var seller = sellerService.getSellerByUser(authenticationService.getCurrentUser());
        if(seller.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if(validateItem(itemDto)){
            return ResponseEntity.badRequest().build();
        }
        var itemPool = itemSellerPoolService.findByItemAndSeller(item.get(), seller.get());
        if(itemPool.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        itemPool.get().setCount(itemDto.count());
        itemPool.get().setPrice(itemDto.price());
        var newItemPool = itemSellerPoolService.save(itemPool.get());
        var result = itemMapper.mapToDto(newItemPool);
        return ResponseEntity.ok(result);
    }

    public Seller mergeSellers(Seller oldSeller, SellerDto newSeller) {
        if (newSeller.name() != null) {
            oldSeller.setName(newSeller.name());
        }
        return oldSeller;
    }

    private Boolean validateItem(ItemDto itemDto){
        return itemDto.count() != null && itemDto.price() != null && itemDto.count() > 0 && itemDto.price() > 0;
    }

    private Cookie createCookie(String token){
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return cookie;
    }

}
