package my.project.util;

/**
 * Исключение, которое выбрасывается, когда отсутствуют данные для получения.
 * <p>
 * Это исключение является подклассом {@link RuntimeException} и используется для
 * сигнализации о том, что операция, требующая данных, не может быть выполнена,
 * потому что данные отсутствуют.
 * </p>
 */
public class NoDataToReceiveException extends RuntimeException{
}
