package boot.tale.model.repository;


import boot.tale.model.entity.Relationships;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelationshipsRepository extends JpaRepository<Relationships,Integer> {

    List<Relationships> findAllByMid(Integer mid);

    List<Relationships> findAllByCidAndMid(Integer cid,Integer mid);


}
