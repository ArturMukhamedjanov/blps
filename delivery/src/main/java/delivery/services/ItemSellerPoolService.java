package delivery.services;

import delivery.models.Item;
import delivery.models.ItemSellerPool;
import delivery.models.Seller;
import delivery.models.dto.ItemDto;
import delivery.repos.ItemRepo;
import delivery.repos.ItemSellerPoolRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemSellerPoolService {

    private final ItemSellerPoolRepo itemSellerPoolRepo;
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
}