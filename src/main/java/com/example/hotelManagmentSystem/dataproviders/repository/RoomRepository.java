package com.example.hotelManagmentSystem.dataproviders.repository;

import com.example.hotelManagmentSystem.dataproviders.entity.Room;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>
{
    List<Room> findAllByHotelId(Integer hotelId);

    @Query(value = "SELECT rm.* , result_query.total as total " +
            "FROM (" +
             " SELECT rooms.\"id\" as rid, sum(rooms.\"price\" * day_count.\"occurrences\") as total " +
               " FROM (" +
            "        SELECT r.\"id\", rp.\"day\", rp.\"price\" " +
            "        FROM \"room\" r " +
            "        INNER JOIN \"room_price\" rp ON r.\"id\" = rp.\"room_id\" " +
            "        WHERE r.\"id\" NOT IN (" +
            "            SELECT res.\"room_id\" " +
            "            FROM \"reservation\" res " +
            "            WHERE res.\"hotel_id\" = :hotelId " +
            "            AND :checkInDate < res.\"check_out\" " +
            "            AND :checkOutDate > res.\"check_in\" " +
            "        ) " +
            "        AND r.\"hotel_id\" = :hotelId " +
            "        AND r.\"kids\" >= :kids " +
            "        AND r.\"adult\" >= :adult " +
            "    ) AS rooms " +
            "    INNER JOIN (" +
            "        WITH date_series AS (" +
            "            SELECT generate_series(" +
            "                :checkInDate, " +
            "                :checkOutDate, " +
            "                '1 day'::interval " +
            "            ) AS date_column " +
            "        ) " +
            "        SELECT " +
            "            (EXTRACT(DOW FROM date_column) + 6) % 7 AS day_of_week, " +
            "            COUNT(*) AS occurrences " +
            "        FROM " +
            "            date_series " +
            "        GROUP BY " +
            "            day_of_week " +
            "        ORDER BY " +
            "            day_of_week " +
            "    ) AS day_count " +
            "    ON rooms.\"day\" = day_count.day_of_week " +
            "    GROUP BY rooms.\"id\" ) AS result_query " +
            "INNER JOIN \"room\" rm ON rm.\"id\" = result_query.rid " +
            "ORDER BY total OFFSET :pageNumber LIMIT :pageSize ",
            nativeQuery = true)
    LinkedList<Object[]> findAvailableRoomsByHotelIdAndDateRange(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("hotelId") int hotelId,
            @Param("kids") int kids,
            @Param("adult") int adult,
            @Param("pageNumber") int pageNumber,
            @Param("pageSize") int pageSize);

    @Query(value = "SELECT rm.* , result_query.total as total " +
            "FROM (" +
            " SELECT rooms.\"id\" as rid, sum(rooms.\"price\" * day_count.\"occurrences\") as total " +
            " FROM (" +
            "        SELECT r.\"id\", rp.\"day\", rp.\"price\" " +
            "        FROM \"room\" r " +
            "        INNER JOIN \"room_price\" rp ON r.\"id\" = rp.\"room_id\" " +
            "        WHERE r.\"id\" NOT IN (" +
            "            SELECT res.\"room_id\" " +
            "            FROM \"reservation\" res " +
            "            WHERE res.\"hotel_id\" = :hotelId " +
            "            AND :checkInDate < res.\"check_out\" " +
            "            AND :checkOutDate > res.\"check_in\" " +
            "        ) " +
            "        AND r.\"hotel_id\" = :hotelId " +
            "        AND r.\"kids\" >= :kids " +
            "        AND r.\"adult\" >= :adult " +
            "    ) AS rooms " +
            "    INNER JOIN (" +
            "        WITH date_series AS (" +
            "            SELECT generate_series(" +
            "                :checkInDate, " +
            "                :checkOutDate, " +
            "                '1 day'::interval " +
            "            ) AS date_column " +
            "        ) " +
            "        SELECT " +
            "            (EXTRACT(DOW FROM date_column) + 6) % 7 AS day_of_week, " +
            "            COUNT(*) AS occurrences " +
            "        FROM " +
            "            date_series " +
            "        GROUP BY " +
            "            day_of_week " +
            "        ORDER BY " +
            "            day_of_week " +
            "    ) AS day_count " +
            "    ON rooms.\"day\" = day_count.day_of_week " +
            "    GROUP BY rooms.\"id\" ) AS result_query " +
            "INNER JOIN \"room\" rm ON rm.\"id\" = result_query.rid " +
            "ORDER BY total DESC OFFSET :pageNumber LIMIT :pageSize ",
            nativeQuery = true)
    LinkedList<Object[]> findAvailableRoomsByHotelIdAndDateRangeOrderDesc(
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            @Param("kids") int kids,
            @Param("adult") int adult,
            @Param("hotelId") int hotelId,
            @Param("pageNumber") int pageNumber,
            @Param("pageSize") int pageSize);


    @Query("SELECT r FROM Room r WHERE r.id NOT IN (" +
            "SELECT r.id FROM Room r INNER JOIN Reservation rv ON r.id = rv.room.id " +
            "WHERE :newCheckIn < rv.checkOut AND :newCheckOut > rv.checkIn " +
            "AND r.adult = :adult AND r.kids = :kids) AND r.id = :roomId")
    Optional<Room> findAvailableRoom(
            @Param("newCheckIn") LocalDate newCheckIn,
            @Param("newCheckOut") LocalDate newCheckOut,
            @Param("adult") int adult,
            @Param("kids") int kids,
            @Param("roomId") int roomId);

    Optional<Room> findRoomById(int roomId);

}