package delivery.services;

import delivery.models.dto.ItemDto;
import delivery.models.items.Item;
import delivery.models.items.ItemSellerPool;
import delivery.models.orders.ItemOrderPool;
import delivery.models.orders.Order;
import delivery.models.orders.Seller;
import delivery.repositories.orders.ItemOrderPoolRepo;
import delivery.repositories.items.ItemRepo;
import delivery.repositories.items.ItemSellerPoolRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSellerPoolService {

    private final ItemSellerPoolRepo itemSellerPoolRepo;
    private final ItemOrderPoolRepo itemOrderPoolRepo;
    private final ItemRepo itemRepo;
    private final JtaTransactionManager jtaTransactionManager;

    public List<ItemSellerPool> getItemPoolsByItem(Item item) {
        return itemSellerPoolRepo.getItemSellerPoolsByItem(item);
    }

    public List<ItemSellerPool> getItemPoolsBySeller(Seller seller) {
        return itemSellerPoolRepo.getItemSellerPoolsBySellerId(seller.getId());
    }

    public Optional<ItemSellerPool> findByItemAndSeller(Item item, Seller seller) {
        return itemSellerPoolRepo.findByItemAndSellerId(item, seller.getId());
    }

    public ItemSellerPool save(ItemSellerPool itemSellerPool) {
        return itemSellerPoolRepo.save(itemSellerPool);
    }

    public void delete(ItemSellerPool itemSellerPool) {
        itemSellerPoolRepo.delete(itemSellerPool);
    }

    public Optional<ItemSellerPool> addItemCount(ItemOrderPool itemOrderPool, Seller seller){
        var item = itemRepo.findById(itemOrderPool.getItemId());
        if(item.isEmpty()){
            return Optional.empty();
        }
        var itemSellerPoolOpt = findByItemAndSeller(item.get(), seller);
        if(itemSellerPoolOpt.isEmpty()){
            return Optional.empty();
        }
        var itemSellerPool = itemSellerPoolOpt.get();
        itemSellerPool.setCount(itemSellerPool.getCount() + itemOrderPool.getCount());
        return Optional.ofNullable(save(itemSellerPool));
    }

    public boolean reduceItems(List<ItemDto> itemDtos, Seller seller) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(jtaTransactionManager);
        
        return transactionTemplate.execute(status -> {
            try {
                // Валидация
                for (var itemDto : itemDtos) {
                    if (itemDto.count() == null) {
                        log.error("ItemDto count is null, itemDto: {}", itemDto);
                        status.setRollbackOnly();
                        return false;
                    }
                    var itemOpt = itemRepo.findById(itemDto.id());
                    if (itemOpt.isEmpty()) {
                        log.error("Item not found, id: {}", itemDto.id());
                        status.setRollbackOnly();
                        return false;
                    }
                    var itemSellerPoolOpt = findByItemAndSeller(itemOpt.get(), seller);
                    if (itemSellerPoolOpt.isEmpty()) {
                        log.error("ItemSellerPool not found for item: {} and seller: {}", itemDto.id(), seller.getId());
                        status.setRollbackOnly();
                        return false;
                    }
                    
                    if (itemSellerPoolOpt.get().getCount() < itemDto.count()) {
                        log.error("Not enough items in stock. Available: {}, requested: {}", 
                                 itemSellerPoolOpt.get().getCount(), itemDto.count());
                        status.setRollbackOnly();
                        return false;
                    }
                }
                
                // Обновление
                for (var itemDto : itemDtos) {
                    var item = itemRepo.findById(itemDto.id()).get();
                    var itemSellerPool = findByItemAndSeller(item, seller).get();
                    
                    itemSellerPool.setCount(itemSellerPool.getCount() - itemDto.count());
                    itemSellerPoolRepo.save(itemSellerPool);
                }
                
                return true;
            } catch (Exception e) {
                log.error("Error reducing items: {}", e.getMessage());
                status.setRollbackOnly();
                return false;
            }
        });
    }
    
    public boolean update(List<ItemDto> newItems, Seller seller, Order order) {
        TransactionTemplate transactionTemplate = new TransactionTemplate(jtaTransactionManager);
        
        return transactionTemplate.execute(status -> {
            try {
                List<ItemOrderPool> itemsInOrder = itemOrderPoolRepo.getItemOrderPoolsByOrder(order);
                for (var itemInOrder : itemsInOrder) {
                    if (addItemCount(itemInOrder, seller).isEmpty()) {
                        status.setRollbackOnly();
                        return false;
                    }
                }
                
                itemOrderPoolRepo.deleteByOrder(order);
                
                var reduceItemResult = reduceItems(newItems, seller);
                if (!reduceItemResult) {
                    status.setRollbackOnly();
                    return false;
                }
                
                for (var itemDto : newItems) {
                    if (itemDto.id() == null) {
                        status.setRollbackOnly();
                        return false;
                    }
                    if (itemDto.count() == null) {
                        status.setRollbackOnly();
                        return false;
                    }
                    var item = itemRepo.findById(itemDto.id());
                    if (item.isEmpty()) {
                        status.setRollbackOnly();
                        return false;
                    }
                    
                    var itemOrderPool = ItemOrderPool.builder()
                            .order(order)
                            .itemId(item.get().getId())
                            .count(itemDto.count())
                            .build();
                    itemOrderPoolRepo.save(itemOrderPool);
                }
                
                return true;
            } catch (Exception e) {
                log.error("Error in update: {}", e.getMessage());
                status.setRollbackOnly();
                return false;
            }
        });
    }
}