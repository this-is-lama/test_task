package my.project.dto;

import java.time.Duration;
import java.time.LocalTime;

/**
 * DTO (Data Transfer Object) для входящего звонка.
 * Этот класс используется для представления информации о времени,
 * затраченном на входящий звонок.
 */
public class IncomingCallDTO {

	private LocalTime totalTime;

	/**
	 * Конструктор по умолчанию.
	 * Инициализирует {@code totalTime} значением {@link LocalTime#MIDNIGHT}.
	 */
	public IncomingCallDTO() {
		this.totalTime = LocalTime.MIDNIGHT;
	}

	/**
	 * Конструктор с параметром.
	 * Инициализирует {@code totalTime} заданным временем.
	 *
	 * @param totalTime общее время входящего звонка
	 */
	public IncomingCallDTO(LocalTime totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * Получает общее время входящего звонка.
	 *
	 * @return общее время в формате {@link LocalTime}
	 */
	public LocalTime getTotalTime() {
		return totalTime;
	}

	/**
	 * Устанавливает общее время входящего звонка.
	 *
	 * @param totalTime общее время, которое нужно установить
	 */
	public void setTotalTime(LocalTime totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * Добавляет заданную продолжительность к общему времени входящего звонка.
	 *
	 * @param duration продолжительность, которую нужно добавить к общему времени
	 */
	public void plusTotalTime(Duration duration) {
		this.totalTime = totalTime.plus(duration);
	}
}

