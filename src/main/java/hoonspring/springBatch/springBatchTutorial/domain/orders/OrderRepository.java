package hoonspring.springBatch.springBatchTutorial.domain.orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Integer> {
    /*
     * JpaRepository를 상속받기만하면, 별도의 구현없이도 아래 5가지의 CRUD 메서드를 기본적으로 제공함.
     *  1. findAll()
     *  2. findById(ID id)
     *  3. save(T entity)
     *  4. deleteById(ID id)
     *  5. count()
     *
     * 이 외 메서드가 추가로 필요할시,
     * JPA의 메서드 쿼리(Method Query) 규칙에 따라 메서드 이름을 정의하면 JPA가 자동으로 쿼리를 생성한다.
     * 더 복잡한 쿼리가 필요하다면, @Query 애노테이션을 사용하여 직접 작성.
     */
}
