package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.Dto.ItemInOutDto;
import com.example.yesim_spring.database.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {


    List<ItemEntity> findAllByNameContaining(String searchVal);

    Page<ItemEntity> findAllByNameContaining(String searchVal, Pageable pageable);

    int countAllByNameContaining(String searchVal);


    ItemEntity findByName(String name);

    @Query(value = "select count(*) from item where total_num <= min_num", nativeQuery = true)
    int countAllUnderMin();

    @Query(value = "select * from item where total_num <= min_num", nativeQuery = true)
    List<ItemEntity> getItemUnderMin(Pageable pageable);

    @Query(value =
            "select count(*) as inoutTotalCount " +
                    "from (" +
                    "   select p.purchase_id, i.usage_id from purchase p left join item_usage i on i.usage_id = null " +
                    "union " +
                    "   select p.purchase_id, i.usage_id from purchase p right join item_usage i on p.purchase_id = null " +
                    ") as pi",
            nativeQuery = true)
    int getInoutListCount(@Param("dateForm") String dateForm);

    @Query(value =
            "select " +
                    "pi_sum.pi_item_id as itemId," +
                    "item_info.item_name as itemName, " +
                    "item_info.company_name as companyName, " +
                    "item_info.thumbnail as thumbnail," +
                    "item_info.container_section as containerSection, " +
                    "item_info.total_num as totalNum," +
                    "ifnull(pi_sum.total_req_num, 0) as totalReqNum, " +
                    "ifnull(pi_sum.total_usage_num, 0) as totalUsageNum " +
                    "from( " +
                    "select pi_item_id, sum(req_num) as total_req_num , sum(usage_num) as total_usage_num " +
                    "from" +
                    " (" +

                    "select " +
                    "ifnull(p.item_id, i.item_id) as pi_item_id, " +
                    "p.req_num, " +
                    "i.usage_num " +
                    "from purchase p left join item_usage i on i.usage_id = null " +
                    "where date_format(p.approved_time, :dateForm) = date_format(now(), :dateForm) and p.approved_status = \"IN_STOCK\"" +

                    "union all " +

                    "select " +
                    "ifnull(p.item_id, i.item_id) as pi_item_id, " +
                    "p.req_num, " +
                    "i.usage_num " +
                    "   from purchase p right join item_usage i on p.purchase_id = null " +
                    "   where date_format(i.usage_time, :dateForm) = date_format(now(), :dateForm) " +

                    ") as pi group by pi.pi_item_id) as pi_sum " +

                    "inner join (" +
                    "select i.item_id, i.name as item_name, c.name as company_name, i.thumbnail, con.section as container_section, i.total_num from item i " +
                    "inner join company c " +
                    "on i.company_id = c.company_id " +
                    "inner join container con " +
                    "on i.container_id = con.container_id " +
                    ") as item_info " +
                    "on item_info.item_id = pi_sum.pi_item_id "
            , nativeQuery = true)
    List<interItemInOutDto> getItemInOut(Pageable pageable, @Param("dateForm") String dateForm);


    interface interItemInOutDto {

        Long getItemId();

        String getItemName();

        String getCompanyName();

        String getThumbnail();

        String getContainerSection();

        int getTotalNum();

        int getTotalReqNum();

        int getTotalUsageNum();
    }

}
