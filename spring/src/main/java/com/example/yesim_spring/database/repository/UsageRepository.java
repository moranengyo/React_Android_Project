package com.example.yesim_spring.database.repository;

import com.example.yesim_spring.database.entity.UsageEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsageRepository extends JpaRepository<UsageEntity, Long> {

    List<UsageEntity> findAllByUser_UserId(String userId);

    @Query(value = "select * from item_usage where date_format(usage_time, :dateForm) = date_format(now(), :dateForm) order by usage_time desc", nativeQuery = true)
    List<UsageEntity> getTotalUsageListByDate(Pageable pageable, @Param("dateForm") String dateForm);

    @Query(value = "select * " +
            "from item_usage as u " +
            "where date_format(usage_time, :dateForm) = date_format(now(), :dateForm) " +
            "and user_seq = (" +
            "select user_seq from user where user_id = :userId" +
            ")", nativeQuery = true)
    List<UsageEntity> getTotalUsageListByDateAndUser(Pageable pageable, @Param("dateForm") String dateForm, @Param("userId") String userId);

    @Query(value =
            "select i_info.item_id as itemId, " +
                    "i_info.item_name as itemName, " +
                    "i_info.com_name as companyName, " +
                    "i_info.thumbnail as thumbnail, " +
                    "i_info.section as containerSection, " +
                    "i_info.total_num as totalNum, " +
                    "ifnull(sum(u.usage_num), 0) as totalUsageNum, " +
                    "max(u.usage_time) as usageTime " +
                    "from item_usage as u " +
                    "inner join " +
                    "(select i.item_id, i.name as item_name, com.name as com_name, i.thumbnail, con.section, i.total_num " +
                    "from item as i " +
                    "inner join company as com " +
                    "on i.company_id = com.company_id " +
                    "inner join container as con " +
                    "on i.container_id = con.container_id" +
                    ") as i_info " +
                    "on i_info.item_id = u.item_id " +
                    "where date_format(u.usage_time, :dateForm) = date_format(now(), :dateForm) " +
                    "group by i_info.item_id"
            , nativeQuery = true)
    List<interItemUsageDto> getTotalUsageListByDateAndGroupedItem(Pageable pageable, @Param("dateForm") String dateForm);


    @Query(value =
            "select count(a.itemId) from (" +
                    "select i_info.item_id as itemId, " +
                    "i_info.item_name as itemName, " +
                    "i_info.com_name as companyName, " +
                    "i_info.thumbnail as thumbnail, " +
                    "i_info.section as containerSection, " +
                    "i_info.total_num as totalNum, " +
                    "ifnull(sum(u.usage_num), 0) as totalUsageNum, " +
                    "max(u.usage_time) as usageTime " +
                    "from item_usage as u " +
                    "inner join " +
                    "(select i.item_id, i.name as item_name, com.name as com_name, i.thumbnail, con.section, i.total_num " +
                    "from item as i " +
                    "inner join company as com " +
                    "on i.company_id = com.company_id " +
                    "inner join container as con " +
                    "on i.container_id = con.container_id" +
                    ") as i_info " +
                    "on i_info.item_id = u.item_id " +
                    "where date_format(u.usage_time, :dateForm) = date_format(now(), :dateForm) and u.user_seq = :userSeq " +
                    "group by i_info.item_id)  as a"
            , nativeQuery = true)
    int getTotalUsageCountByDateAndUserGroupedItem(@Param("dateForm") String dateForm, @Param("userSeq") long userSeq);


    @Query(value =
            "select i_info.item_id as itemId, " +
                    "i_info.item_name as itemName, " +
                    "i_info.com_name as companyName, " +
                    "i_info.thumbnail as thumbnail, " +
                    "i_info.section as containerSection, " +
                    "i_info.total_num as totalNum, " +
                    "ifnull(sum(u.usage_num), 0) as totalUsageNum, " +
                    "max(u.usage_time) as usageTime " +
                    "from item_usage as u " +
                    "inner join " +
                    "(select i.item_id, i.name as item_name, com.name as com_name, i.thumbnail, con.section, i.total_num " +
                    "from item as i " +
                    "inner join company as com " +
                    "on i.company_id = com.company_id " +
                    "inner join container as con " +
                    "on i.container_id = con.container_id" +
                    ") as i_info " +
                    "on i_info.item_id = u.item_id " +
                    "where date_format(u.usage_time, :dateForm) = date_format(now(), :dateForm) and u.user_seq = :userSeq " +
                    "group by i_info.item_id"
            , nativeQuery = true)
    List<interItemUsageDto> getTotalUsageListByDateAndUserGroupedItem(Pageable pageable, @Param("dateForm") String dateForm, @Param("userSeq") long userSeq);

    interface interItemUsageDto {

        Long getItemId();

        String getItemName();

        String getCompanyName();

        String getThumbnail();

        String getContainerSection();

        int getTotalNum();

        int getTotalUsageNum();
    }

    @Query(value =
            "select " +
                    "(usage_info.usage_id), " +
                    "usage_info.usage_num_sum as usage_num, " +
                    "usage_info.item_id, " +
                    "usage_info.usage_time, " +
                    "usage_info.user_seq, " +
                    "usage_info.total_code " +
                    "from (" +
                    "select iu.*, ifnull(sum(iu.usage_num), 0) as usage_num_sum " +
                    "   from item_usage as iu " +
                    "       inner join item as i on iu.item_id = i.item_id " +
                    "           inner join user as u on iu.user_seq = u.user_seq " +
                    "               where (i.name like concat('%', concat(:searchVal, '%')) or u.user_name like concat('%', concat(:searchVal, '%'))) " +
                    "                    and date_format(iu.usage_time, :dateForm) = date_format(now(), :dateForm) " +
                    "   group by i.item_id, u.user_seq " +
                    ") as usage_info "
            , nativeQuery = true)
    List<UsageEntity> getSearchUsageList(@Param("dateForm") String dateForm,
                                         @Param("searchVal") String searchVal,
                                         Pageable pageable);


    @Query(value =
            "select ifnull(count(ui.item_id), 0) from(" +
                    "select usage_info.item_id, usage_info.user_seq " +
                    "from (" +
                    "select iu.usage_time, u.user_seq, i.item_id, i.name as item_name, u.user_name " +
                    "   from item_usage as iu " +
                    "       inner join item as i on iu.item_id = i.item_id " +
                    "           inner join user as u on iu.user_seq = u.user_seq " +
                    ") as usage_info " +
                    " where (usage_info.item_name like concat('%', concat(:searchVal, '%')) or usage_info.user_name like concat('%', concat(:searchVal, '%')))  " +
                    "                    and date_format(usage_info.usage_time, :dateForm) = date_format(now(), :dateForm)  " +
                    "                    group by usage_info.item_id, usage_info.user_seq " +

                    ") as ui "
            , nativeQuery = true)
    int getSearchUsageCountByDate(@Param("dateForm") String dateForm,
                                  @Param("searchVal") String searchVal);

    @Query(value =
            "select ifnull(sum(usage_info.usage_num * usage_info.price), 0)" +
                    "from (" +
                    "select iu.*, i.price, i.name as item_name, u.user_name " +
                    "   from item_usage as iu " +
                    "       inner join item as i on iu.item_id = i.item_id " +
                    "           inner join user as u on iu.user_seq = u.user_seq " +
                    ") as usage_info " +
                    "where (usage_info.item_name like concat('%', concat(:searchVal, '%')) or usage_info.user_name like concat('%', concat(:searchVal, '%'))) " +
                    "and date_format(usage_info.usage_time, :dateForm) = date_format(now(), :dateForm) "
            , nativeQuery = true)
    int getSearchUsagePaySumByDate(@Param("dateForm") String dateForm,
                                   @Param("searchVal") String searchVal);


    @Query(value =
            "select ifnull(sum(usage_info.usage_num), 0) " +
                    "from (" +
                    "select iu.*, i.price, i.name as item_name, u.user_name " +
                    "   from item_usage as iu " +
                    "       inner join item as i on iu.item_id = i.item_id " +
                    "           inner join user as u on iu.user_seq = u.user_seq " +
                    ") as usage_info " +
                    "where (usage_info.item_name like concat('%', concat(:searchVal, '%')) or usage_info.user_name like concat('%', concat(:searchVal, '%'))) " +
                    "and date_format(usage_info.usage_time, :dateForm) = date_format(now(), :dateForm) "
            , nativeQuery = true)
    int getSearchUsageNumSumByDate(@Param("dateForm") String dateForm,
                                   @Param("searchVal") String searchVal);


    @Query(value = "select ifnull(sum(usage_num), 0) from item_usage where date_format(usage_time, :dateForm) = date_format(now(), :dateForm)", nativeQuery = true)
    int getTotalUsageSumByDate(@Param("dateForm") String dateForm);

    @Query(value = "select ifnull(sum(usage_num), 0) from item_usage " +
            "where date_format(usage_time, :dateForm) = date_format(now(), :dateForm) " +
            "and user_seq = (" +
            "select user_seq from user where user_id = :userId " +
            ")", nativeQuery = true)
    int getTotalUsageSumByDateAndUser(@Param("dateForm") String dateForm, @Param("userId") String userId);


    @Query(value = "select ifnull(count(ui.item_id), 0) from(" +
            "select usage_info.item_id, usage_info.user_seq " +
            "from (" +
            "select iu.usage_time, u.user_seq, i.item_id, i.name as item_name, u.user_name " +
            "   from item_usage as iu " +
            "       inner join item as i on iu.item_id = i.item_id " +
            "           inner join user as u on iu.user_seq = u.user_seq " +
            ") as usage_info " +
            " where date_format(usage_info.usage_time, :dateForm) = date_format(now(), :dateForm)  " +
            "                    group by usage_info.item_id, usage_info.user_seq " +

            ") as ui ", nativeQuery = true)
    int getTotalUsageCntByDate(@Param("dateForm") String dateForm);


    @Query(value = "select usage_id, " +
            "ifnull(sum(usage_num), 0) as usage_num, " +
            "total_code, " +
            "usage_time, " +
            "user_seq," +
            "item_id " +
            "from item_usage " +
            "where date_format(usage_time, :dateForm) = date_format(now(), :dateForm) " +
            "group by user_seq order by usage_time desc "
            , nativeQuery = true)
    List<UsageEntity> getTotalUsageByDateGroupedUserAndItem(Pageable pageable, @Param("dateForm") String dateForm);

    @Query(value = "select ifnull(count(us.usage_id), 0) " +
            "from (" +
            "select usage_id " +
            "from item_usage " +
            "where date_format(usage_time, :dateForm) = date_format(now(), :dateForm) " +
            "group by user_seq) as us"
            , nativeQuery = true)
    int totalCountUsageByDateGroupedUserAndItem(@Param("dateForm") String dateForm);





    // "select us.* " +
//         "from item_usage as us " +
//         "inner join (select * from item as i inner join user as u) as iu " +
//         "on us.user_seq = iu.user_seq and us.item_id = iu.item_id " +
//         "where date_format(us.usage_time, :dateformat) =  date_format(now(), :dateformat) " +
//         "and (iu.`name` like concat('%', concat(:searchVal, '%')) " +
//         "or iu.user_name like concat('%', concat(:searchVal, '%')))"

// "select us.* " +
//         "from item_usage as us " +
//         "inner join item as i " +
//         "on (us.item_id) = (i.item_id) " +
//         "inner join user as u " +
//         "on (us.user_seq) = (u.user_seq) " +
//         "where date_format(us.usage_time, :dateformat) =  date_format(now(), :dateformat) " +
//         "and (i.name like concat('%', concat(:searchVal, '%')) " +
//         "or u.user_name like concat('%', concat(:searchVal, '%')))"
}