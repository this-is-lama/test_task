package my.project.dto;

import java.time.Duration;
import java.util.Objects;

/**
 * Класс UsageDataReportDTO представляет собой отчет об использовании данных,
 * который включает информацию о номере телефона (MSISDN), а также
 * данные о входящих и исходящих звонках.
 */
public class UsageDataReportDTO {

	private String msisdn;
	private IncomingCallDTO incomingCall;
	private OutcomingCallDTO outcomingCall;

	/**
	 * Конструктор по умолчанию, который инициализирует объект
	 * UsageDataReportDTO без параметров.
	 */
	public UsageDataReportDTO() {
	}

	/**
	 * Конструктор, который инициализирует объект UsageDataReportDTO
	 * с заданным номером телефона (MSISDN). Входящие и исходящие звонки
	 * инициализируются новыми экземплярами соответствующих классов.
	 *
	 * @param msisdn Номер телефона в формате MSISDN.
	 */
	public UsageDataReportDTO(String msisdn) {
		this.msisdn = msisdn;
		this.incomingCall = new IncomingCallDTO();
		this.outcomingCall = new OutcomingCallDTO();
	}

	/**
	 * Конструктор, который инициализирует объект UsageDataReportDTO
	 * с заданными параметрами: номером телефона, входящими и исходящими звонками.
	 *
	 * @param msisdn Номер телефона в формате MSISDN.
	 * @param incomingCall Объект IncomingCallDTO, содержащий данные о входящих звонках.
	 * @param outcomingCall Объект OutcomingCallDTO, содержащий данные об исходящих звонках.
	 */
	public UsageDataReportDTO(String msisdn, IncomingCallDTO incomingCall, OutcomingCallDTO outcomingCall) {
		this.msisdn = msisdn;
		this.incomingCall = incomingCall;
		this.outcomingCall = outcomingCall;
	}

	/**
	 * Добавляет заданную продолжительность к общему времени входящих звонков.
	 *
	 * @param duration Продолжительность, которую необходимо добавить к общему времени входящих звонков.
	 */
	public void plusIncomingTime(Duration duration) {
		this.incomingCall.plusTotalTime(duration);
	}

	/**
	 * Добавляет заданную продолжительность к общему времени исходящих звонков.
	 *
	 * @param duration Продолжительность, которую необходимо добавить к общему времени исходящих звонков.
	 */
	public void plusOutcomingTime(Duration duration) {
		this.outcomingCall.plusTotalTime(duration);
	}

	/**
	 * Получает номер телефона (MSISDN).
	 *
	 * @return Номер телефона.
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Устанавливает номер телефона (MSISDN).
	 *
	 * @param msisdn Номер телефона.
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * Получает объект IncomingCallDTO, содержащий данные о входящих звонках.
	 *
	 * @return Объект IncomingCallDTO.
	 */
	public IncomingCallDTO getIncomingCall() {
		return incomingCall;
	}

	/**
	 * Устанавливает объект IncomingCallDTO для данных о входящих звонках.
	 *
	 * @param incomingCall Объект IncomingCallDTO, содержащий данные о входящих звонках.
	 */
	public void setIncomingCallDTO(IncomingCallDTO incomingCall) {
		this.incomingCall = incomingCall;
	}

	/**
	 * Получает объект OutcomingCallDTO, содержащий данные об исходящих звонках.
	 *
	 * @return Объект OutcomingCallDTO.
	 */
	public OutcomingCallDTO getOutcomingCall() {
		return outcomingCall;
	}

	/**
	 * Устанавливает объект OutcomingCallDTO для данных об исходящих звонках.
	 *
	 * @param outcomingCall Объект OutcomingCallDTO, содержащий данные об исходящих звонках.
	 */
	public void setOutcomingCall(OutcomingCallDTO outcomingCall) {
		this.outcomingCall = outcomingCall;
	}

	/**
	 * Проверяет равенство текущего объекта с другим объектом.
	 *
	 * @param object Объект, с которым нужно сравнить текущий объект.
	 * @return true, если объекты равны; false в противном случае.
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof UsageDataReportDTO that)) return false;
		return Objects.equals(msisdn, that.msisdn);
	}

	/**
	 * Возвращает хэш-код для текущего объекта.
	 *
	 * @return Хэш-код для текущего объекта.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(msisdn);
	}
}

