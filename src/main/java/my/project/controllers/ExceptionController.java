package my.project.controllers;

import my.project.util.IncorrectPhoneNumberException;
import my.project.util.NoDataToReceiveException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import java.io.IOException;
import java.time.format.DateTimeParseException;

/**
 * Контроллер для обработки исключений в приложении.
 * Этот класс использует аннотацию {@link ControllerAdvice} для глобальной обработки исключений,
 * возникающих в контроллерах приложения.
 *
 * <p>Контроллер обрабатывает следующие типы исключений:</p>
 * <ul>
 *   <li>{@link DateTimeParseException} - Исключение, связанное с неправильным форматом даты и времени.</li>
 *   <li>{@link NoDataToReceiveException} - Исключение, возникающее при отсутствии данных для получения.</li>
 *   <li>{@link IncorrectPhoneNumberException} - Исключение, связанное с неправильным номером телефона.</li>
 *   <li>{@link IOException} - Исключение, возникающее при ошибках ввода-вывода, например, при генерации отчетов.</li>
 * </ul>
 */
@ControllerAdvice
public class ExceptionController {

	/**
	 * Обрабатывает исключение {@link DateTimeParseException}, возникающее в контроллерах,
	 * связанных с форматом даты и времени.
	 *
	 * <p>Возвращает сообщение об ошибке с кодом состояния 400 (BAD REQUEST),
	 * если формат даты некорректен.</p>
	 *
	 * @param ignoredE исключение {@link DateTimeParseException}, которое было выброшено
	 * @return ResponseEntity с сообщением об ошибке и статусом BAD REQUEST
	 */
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<String> dateParseException(DateTimeParseException ignoredE) {
		String message = "Некорректный дата, введите день в формате YYYY-MM-DD или месяц в формате YYYY-MM";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	/**
	 * Обрабатывает исключение {@link NoDataToReceiveException},
	 * возникающее при отсутствии данных для получения.
	 *
	 * <p>Возвращает сообщение об ошибке с кодом состояния 404 (NOT FOUND).</p>
	 *
	 * @param ignoredE исключение {@link NoDataToReceiveException}, которое было выброшено
	 * @return ResponseEntity с сообщением об ошибке и статусом NOT FOUND
	 */
	@ExceptionHandler(NoDataToReceiveException.class)
	public ResponseEntity<String> noDataToReceiveException(NoDataToReceiveException ignoredE) {
		String message = "Нет данных для получения";
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
	}

	/**
	 * Обрабатывает исключение {@link IncorrectPhoneNumberException},
	 * возникающее при вводе некорректного номера телефона.
	 *
	 * <p>Возвращает сообщение об ошибке с кодом состояния 400 (BAD REQUEST).</p>
	 *
	 * @param ignoredE исключение {@link IncorrectPhoneNumberException}, которое было выброшено
	 * @return ResponseEntity с сообщением об ошибке и статусом BAD REQUEST
	 */
	@ExceptionHandler(IncorrectPhoneNumberException.class)
	public ResponseEntity<String> incorrectPhoneNumberException(IncorrectPhoneNumberException ignoredE) {
		String message = "Некорректный номер телефона";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
	}

	/**
	 * Обрабатывает исключение {@link IOException},
	 * возникающее при ошибках ввода-вывода, например, при генерации отчетов.
	 *
	 * <p>Возвращает сообщение об ошибке с кодом состояния 500 (INTERNAL SERVER ERROR).</p>
	 *
	 * @param ignoredE исключение {@link IOException}, которое было выброшено
	 * @return ResponseEntity с сообщением об ошибке и статусом INTERNAL SERVER ERROR
	 */
	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> ioException(IOException ignoredE) {
		String message = "Ошибка при генерации отчета";
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
	}
}



