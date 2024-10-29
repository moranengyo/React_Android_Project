package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.entity.PurchaseEntity;
import com.example.yesim_spring.database.entity.define.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {


    List<PurchaseEntity> findAllByUser_UserId(String userId);

    List<PurchaseEntity> findAllByUser_UserIdAndApprovedStatus(String userId, RequestStatus status);

    int countAllByItem_Id(Long itemId);



    int countAllByApprovedStatus(RequestStatus status);
    Page<PurchaseEntity> findAllByApprovedStatusOrderByReqTimeDescApprovedTimeDesc(RequestStatus status, Pageable pageable);

    Page<PurchaseEntity> findAllByApprovedStatusNotOrderByReqTimeDescApprovedTimeDesc(RequestStatus status, Pageable pageable);




    @Query(value = "select ifnull(count(*), 0) from purchase where approved_status = \"IN_STOCK\"", nativeQuery = true)
    int getTotalInStockCount();

    @Query(value = "select ifnull(count(*), 0) from purchase where approved_status = \"IN_STOCK\" and date_format(approved_time, :dateForm) = date_format(now(), :dateForm)", nativeQuery = true)
    int getTotalStockCountByDate(@Param("dateForm") String dateForm);

    @Query(value = "select * from purchase where approved_status = \"IN_STOCK\" and date_format(approved_time, :dateForm) = date_format(now(), :dateForm) order by req_time DESC, approved_time DESC", nativeQuery = true)
    List<PurchaseEntity> getTotalInStockListByDate(Pageable pageable, @Param("dateForm")String dateForm);

    @Query(value = "select * from purchase where approved_status = \"IN_STOCK\" or approved_status = \"APPROVE\" order by approved_time DESC, req_time DESC", nativeQuery = true)
    List<PurchaseEntity> getTotalListInStockOrApprove(Pageable pageable);

    @Query(value = "select ifnull(count(*), 0) from purchase where approved_status = \"IN_STOCK\" or approved_status = \"APPROVE\"", nativeQuery = true)
    int getTotalCountInStockOrApprove();

    @Query(value = "select ifnull(sum(req_num), 0) from purchase where approved_status = \"IN_STOCK\" and date_format(approved_time, :dateForm) = date_format(now(), :dateForm)", nativeQuery = true)
    int getTotalInStockSumByDate(@Param("dateForm")String dateForm);

    @Query(value = "select p.* " +
            "from purchase as p " +
                "inner join item as i " +
                "on (p.item_id) = (i.item_id) " +
                    "where (p.approved_status) = \"IN_STOCK\" " +
                    "and date_format(p.approved_time, :dateformat) = date_format(now(), :dateformat) " +
                    "and i.name like concat('%', concat(:searchVal, '%')) " +
            "order by req_time DESC, approved_time DESC ",
            nativeQuery = true)
    List<PurchaseEntity> getInStockPurchaseList(@Param("dateformat") String dateformat,
                                                @Param("searchVal") String searchVal,
                                                Pageable pageable);

    @Query(value = "select ifnull(count(*), 0) " +
            "from purchase as p " +
            "inner join item as i " +
            "on (p.item_id) = (i.item_id) " +
            "where (p.approved_status) = \"IN_STOCK\" " +
            "and date_format(p.approved_time, :dateformat) = date_format(now(), :dateformat) " +
            "and i.name like concat('%', concat(:searchVal, '%')) ",
            nativeQuery = true)
    int getSearchInStockPurchaseCountByDate(@Param("dateformat") String dateformat,
                                            @Param("searchVal") String searchVal);
}
