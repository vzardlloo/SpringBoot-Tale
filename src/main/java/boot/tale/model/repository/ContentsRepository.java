package boot.tale.model.repository;


import boot.tale.model.entity.Contents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentsRepository extends JpaRepository<Contents,Integer> {


    List<Contents> findAllByCidInOrderByCreatedDesc(List<Integer> cid);
}
