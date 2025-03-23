package my.project.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Представляет запись данных вызова (CDR), которая хранит информацию о телефонном звонке.
 * Этот класс отображается на таблицу "cdr" в базе данных.
 */
@Entity
@Table(name = "cdr")
public class CallDataRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String callType;

	private String phoneOne;

	private String phoneTwo;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	/**
	 * Конструктор по умолчанию для CallDataRecord.
	 */
	public CallDataRecord() {
	}

	/**
	 * Конструктор, создающий запись данных вызова с указанными параметрами.
	 *
	 * @param callType  тип вызова (например, 01 - исходящие, 02 - входящие)
	 * @param phoneOne  номер телефона, инициирующий вызов
	 * @param phoneTwo  номер телефона, принимающий вызов
	 * @param startTime время начала вызова
	 * @param endTime   время окончания вызова
	 */
	public CallDataRecord(String callType, String phoneOne, String phoneTwo, LocalDateTime startTime, LocalDateTime endTime) {
		this.callType = callType;
		this.phoneOne = phoneOne;
		this.phoneTwo = phoneTwo;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	/**
	 * Возвращает время начала вызова.
	 *
	 * @return время начала как {@link LocalDateTime}
	 */
	public LocalDateTime getStartTime() {
		return startTime;
	}

	/**
	 * Устанавливает время начала вызова.
	 *
	 * @param startTime время начала для установки
	 */
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Возвращает время окончания вызова.
	 *
	 * @return время окончания как {@link LocalDateTime}
	 */
	public LocalDateTime getEndTime() {
		return endTime;
	}

	/**
	 * Устанавливает время окончания вызова.
	 *
	 * @param endOfCall время окончания для установки
	 */
	public void setEndTime(LocalDateTime endOfCall) {
		this.endTime = endOfCall;
	}

	/**
	 * Возвращает номер телефона, принимающего вызов.
	 *
	 * @return номер телефона, принимающего вызов, как {@link String}
	 */
	public String getPhoneTwo() {
		return phoneTwo;
	}

	/**
	 * Устанавливает номер телефона, принимающего вызов.
	 *
	 * @param receiveNumber номер телефона для установки
	 */
	public void setPhoneTwo(String receiveNumber) {
		this.phoneTwo = receiveNumber;
	}

	/**
	 * Возвращает номер телефона, инициирующий вызов.
	 *
	 * @return номер телефона, инициирующий вызов, как {@link String}
	 */
	public String getPhoneOne() {
		return phoneOne;
	}

	/**
	 * Устанавливает номер телефона, инициирующий вызов.
	 *
	 * @param initNumber номер телефона для установки
	 */
	public void setPhoneOne(String initNumber) {
		this.phoneOne = initNumber;
	}

	/**
	 * Возвращает тип вызова.
	 *
	 * @return тип вызова как {@link String}
	 */
	public String getCallType() {
		return callType;
	}

	/**
	 * Устанавливает тип вызова.
	 *
	 * @param callType тип вызова для установки
	 */
	public void setCallType(String callType) {
		this.callType = callType;
	}

	/**
	 * Возвращает уникальный идентификатор этой записи данных вызова.
	 *
	 * @return уникальный идентификатор как {@link Long}
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Устанавливает уникальный идентификатор этой записи данных вызова.
	 *
	 * @param id уникальный идентификатор для установки
	 */
	public void setId(Long id) {
		this.id = id;
	}
}

