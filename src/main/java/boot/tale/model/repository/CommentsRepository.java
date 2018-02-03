package boot.tale.model.repository;


import boot.tale.model.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments,Integer>{


}
