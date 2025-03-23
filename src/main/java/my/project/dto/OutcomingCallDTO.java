package my.project.dto;

import java.time.Duration;
import java.time.LocalTime;

/**
 * DTO (Data Transfer Object) для исходящего звонка.
 * Этот класс используется для представления информации о времени,
 * затраченном на исходящий звонок.
 */
public class OutcomingCallDTO {

	private LocalTime totalTime;

	/**
	 * Конструктор по умолчанию.
	 * Инициализирует {@code totalTime} значением {@link LocalTime#MIDNIGHT}.
	 */
	public OutcomingCallDTO() {
		this.totalTime = LocalTime.MIDNIGHT;
	}

	/**
	 * Конструктор с параметром.
	 * Инициализирует {@code totalTime} заданным временем.
	 *
	 * @param totalTime общее время исходящего звонка
	 */
	public OutcomingCallDTO(LocalTime totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * Получает общее время исходящего звонка.
	 *
	 * @return Общее время исходящего звонка в формате LocalTime.
	 */
	public LocalTime getTotalTime() {
		return totalTime;
	}

	/**
	 * Устанавливает общее время исходящего звонка.
	 *
	 * @param totalTime Общее время исходящего звонка в формате LocalTime.
	 */
	public void setTotalTime(LocalTime totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 * Добавляет заданную продолжительность к общему времени исходящего звонка.
	 *
	 * @param duration Продолжительность, которую необходимо добавить к общему времени.
	 */
	public void plusTotalTime(Duration duration) {
		this.totalTime = totalTime.plus(duration);
	}
}

