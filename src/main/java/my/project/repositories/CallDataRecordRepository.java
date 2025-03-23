package my.project.repositories;

import my.project.entity.CallDataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с записями данных вызовов CDR (Call Data Records).
 * Этот интерфейс расширяет JpaRepository и предоставляет методы для выполнения запросов к базе данных.
 */
@Repository
public interface CallDataRecordRepository extends JpaRepository<CallDataRecord, Long> {

	/**
	 * Находит записи данных вызовов по номеру абонента (MSISDN) и диапазону дат.
	 *
	 * @param msisdn номер абонента, по которому выполняется поиск
	 * @param start  начальная дата и время диапазона
	 * @param end    конечная дата и время диапазона
	 * @return список записей данных вызовов, соответствующих указанному номеру и диапазону дат
	 */
	@Query("SELECT c FROM CallDataRecord c WHERE c.startTime BETWEEN :start AND :end AND (c.phoneOne = :msisdn OR c.phoneTwo = :msisdn)")
	List<CallDataRecord> findByMsisdnAndDateRange(@Param("msisdn") String msisdn,
												  @Param("start") LocalDateTime start,
												  @Param("end") LocalDateTime end);

	/**
	 * Находит все записи данных вызовов по номеру абонента (MSISDN).
	 *
	 * @param msisdn номер абонента, по которому выполняется поиск
	 * @return список записей данных вызовов, соответствующих указанному номеру
	 */
	@Query("SELECT c FROM CallDataRecord c WHERE c.phoneOne = :msisdn OR c.phoneTwo = :msisdn")
	List<CallDataRecord> findByMsisdn(@Param("msisdn") String msisdn);

	/**
	 * Находит все записи данных вызовов в указанном диапазоне дат.
	 *
	 * @param start начальная дата и время диапазона
	 * @param end   конечная дата и время диапазона
	 * @return список всех записей данных вызовов, соответствующих указанному диапазону дат
	 */
	@Query("SELECT c FROM CallDataRecord c WHERE c.startTime BETWEEN :start AND :end")
	List<CallDataRecord> findAllByDateRange(@Param("start") LocalDateTime start,
											@Param("end") LocalDateTime end);

	/**
	 * Находит последнюю запись данных вызовов по времени окончания вызова
	 *
	 * @return последняя запись данных вызовов, если такая существует, иначе {@link Optional#empty()}
	 */
	Optional<CallDataRecord> findFirstByOrderByEndTimeDesc();
}

