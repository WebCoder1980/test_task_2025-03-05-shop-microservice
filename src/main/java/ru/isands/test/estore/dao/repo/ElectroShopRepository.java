package ru.isands.test.estore.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.isands.test.estore.dao.entity.ElectroShop;
import ru.isands.test.estore.dao.entity.ElectroShopPK;

import javax.transaction.Transactional;
import java.util.List;


public interface ElectroShopRepository extends JpaRepository<ElectroShop, ElectroShopPK> {
    @Query("select p from ElectroShop p where p.electroId = :electroId and p.shopId = :shopId")
    public List<ElectroShop> findByElectroIdAndShopId(@Param("electroId") Long electroId, @Param("shopId") Long shopId);

    @Modifying
    @Transactional
    @Query("delete from ElectroShop p where p.electroId = :electroId and p.shopId = :shopId")
    public void deleteByElectroIdAndShopId(@Param("electroId") Long electroId, @Param("shopId") Long shopId);
}
