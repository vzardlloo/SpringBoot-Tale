package boot.tale.model.repository;


import boot.tale.model.entity.Contents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import static org.apache.coyote.http11.Constants.a;

public interface ContentsRepository extends JpaRepository<Contents,Integer> {


    List<Contents> findAllByCidInOrderByCreatedDesc(List<Integer> cid);

    Contents findByCid(Integer cid);

    int countBySlugAndType(String slug, String type);

    Page<Contents> findAllByStatusAndType(String status, String type, Pageable pageable);

    @Query(value = "select cid from t_contents tc where tc.type = :t and tc.status = :s order by random()*cid", nativeQuery = true)
    List<Contents> findAllByTypeAndStatus(@Param("t") String type, @Param("s") String status);

    long countByType(String type);

    List<Contents> findByTypeAndStatusAndCreatedIsBetweenOrderByCreatedDesc(String type, String status, int start, int end);

}
