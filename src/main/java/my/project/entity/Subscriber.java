package my.project.entity;

import jakarta.persistence.*;

/**
 * Представляет абонента в системе. Этот класс отображается на таблицу "subscriber" в базе данных.
 */
@Entity
@Table(name = "subscriber")
public class Subscriber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String msisdn;

	/**
	 * Конструктор по умолчанию для класса Subscriber.
	 */
	public Subscriber() {
	}

	/**
	 * Конструктор, создающий абонента с указанным номером MSISDN.
	 *
	 * @param msisdn номер абонента
	 */
	public Subscriber(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * Возвращает уникальный идентификатор абонента.
	 *
	 * @return уникальный идентификатор как {@link Long}
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Устанавливает уникальный идентификатор абонента.
	 *
	 * @param id уникальный идентификатор для установки
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Возвращает номер абонента.
	 *
	 * @return номер абонента как {@link String}
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Устанавливает номер абонента.
	 *
	 * @param msisdn номер абонента для установки
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}
}

