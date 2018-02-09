package boot.tale.model.repository;


import boot.tale.model.entity.Comments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Integer>{


    List<Comments> findByParentOrderByCoidDesc(Integer parent);

    Comments findByCoid(Integer coid);

    void deleteByCid(Integer cid);

}
