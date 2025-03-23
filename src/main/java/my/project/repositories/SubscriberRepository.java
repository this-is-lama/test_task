package my.project.repositories;

import my.project.entity.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для работы с абонентами (Subscriber).
 * Этот интерфейс расширяет JpaRepository и предоставляет методы для выполнения CRUD операций
 * с сущностью Subscriber в базе данных.
 */
@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}

