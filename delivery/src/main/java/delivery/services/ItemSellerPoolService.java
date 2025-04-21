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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSellerPoolService {

    private final ItemSellerPoolRepo itemSellerPoolRepo;
    private final ItemOrderPoolRepo itemOrderPoolRepo;
    private final ItemRepo itemRepo;

    public List<ItemSellerPool> getItemPoolsByItem(Item item) {
        return itemSellerPoolRepo.getItemSellerPoolsByItem(item);
    }

    public List<ItemSellerPool> getItemPoolsBySeller(Seller seller) {
        return itemSellerPoolRepo.getItemSellerPoolsBySeller(seller);
    }

    public Optional<ItemSellerPool> findByItemAndSeller(Item item, Seller seller) {
        return itemSellerPoolRepo.findByItemAndSeller(item, seller);
    }

    public ItemSellerPool save(ItemSellerPool itemSellerPool) {
        return itemSellerPoolRepo.save(itemSellerPool);
    }

    public void delete(ItemSellerPool itemSellerPool) {
        itemSellerPoolRepo.delete(itemSellerPool);
    }

    public Optional<ItemSellerPool> addItemCount(ItemOrderPool itemOrderPool, Seller seller){
        var itemSellerPoolOpt = findByItemAndSeller(itemOrderPool.getItem(), seller);
        if(itemSellerPoolOpt.isEmpty()){
            return Optional.empty();
        }
        var itemSellerPool = itemSellerPoolOpt.get();
        itemSellerPool.setCount(itemSellerPool.getCount() + itemOrderPool.getCount());
        return Optional.ofNullable(save(itemSellerPool));
    }

    @Transactional
    public boolean reduceItems(List<ItemDto> itemDtos, Seller seller){
        for(var itemDto : itemDtos){
            if(itemDto.count() == null){
                log.info("ItemDto count is null, itemDto: {}", itemDto);
                return false;
            }
            var itemOpt = itemRepo.findById(itemDto.id());
            if(itemOpt.isEmpty()){
                log.info("ItemDto id is not found, itemDto: {}", itemDto);
                return false;
            }
            var itemSellerPoolOpt = findByItemAndSeller(itemOpt.get(), seller);
            if(itemSellerPoolOpt.isEmpty()){
                log.info("ItemSellerPool is not found, itemDto: {}, seller: {}", itemDto, seller);
                return false;
            }
            if(itemSellerPoolOpt.get().getCount() < itemDto.count()){
                log.info("ItemSellerPool count is not enough, itemDto: {}, itemSellerPoolOpt: {}", itemDto, itemSellerPoolRepo);
                return false;
            }
            var itemSellerPool = itemSellerPoolOpt.get();
            itemSellerPool.setCount(itemSellerPoolOpt.get().getCount() - itemDto.count());
            itemSellerPoolRepo.save(itemSellerPool);
        }
        return true;
    }

    @Transactional
    public boolean update(List<ItemDto> newItems, Seller seller,  Order order){
        List<ItemOrderPool> itemsInOrder = itemOrderPoolRepo.getItemOrderPoolsByOrder(order);
        for(var itemInOrder : itemsInOrder){
            if(addItemCount(itemInOrder, seller).isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }
        itemOrderPoolRepo.deleteByOrder(order);
        var reduceItemResult = reduceItems(newItems, seller);
        if(!reduceItemResult){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        for(var itemDto : newItems){
            if(itemDto.id() == null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            if(itemDto.count() == null){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            var item = itemRepo.findById(itemDto.id());
            if(item.isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
            var itemOrderPool = ItemOrderPool.builder()
                    .order(order)
                    .item(item.get())
                    .count(itemDto.count())
                    .build();
            itemOrderPoolRepo.save(itemOrderPool);
        }
        return true;
    }
}