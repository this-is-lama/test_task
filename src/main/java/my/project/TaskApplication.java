package my.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Основной класс приложения, который запускает Spring Boot приложение.
 * <p>
 * Этот класс помечен аннотацией {@link SpringBootApplication}, что делает его точкой входа в приложение.
 * </p>
 */
@SpringBootApplication
public class TaskApplication {

	/**
	 * Главный метод, который запускает приложение.
	 *
	 * @param args аргументы командной строки, переданные при запуске приложения.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}
}

