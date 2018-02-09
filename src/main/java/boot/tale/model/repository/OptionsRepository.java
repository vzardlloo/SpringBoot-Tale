package boot.tale.model.repository;


import boot.tale.model.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionsRepository extends JpaRepository<Options, String> {

    Options findOneByName(String name);

    int countByName(String name);

    void deleteOptionsByName(String name);

    List<Options> findByNameLike(String key);
}
