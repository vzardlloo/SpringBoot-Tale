package boot.tale.model.repository;


import boot.tale.model.dto.Archive;
import boot.tale.model.entity.Attach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach,Integer> {

    @Query(value = "select strftime('%Y年%m月', datetime(created, 'unixepoch') ) as date_str, count(*) as count  from t_contents " +
            "where type = 'post' and status = 'publish' group by date_str order by date_str desc", nativeQuery = true)
    List<Archive> getAttaches();
}
