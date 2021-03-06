package boot.tale.model.repository;



import boot.tale.model.entity.Metas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetasRepository extends JpaRepository<Metas,Integer>{


    List<Metas> findAllByTypeOrderByMidDesc(String type);

    @Query(value = "select a.*, count(b.cid) as count from t_metas a left join `t_relationships` b on a.mid = b.mid \n" +
            "where a.type = ? and a.name = ? group by a.mid",nativeQuery = true)
    Metas getMetasByTypeAndAndName(String type,String name);

    Metas findByTypeAndName(String type,String name);

    Metas findByMid(Integer mid);

    long countByType(String type);

    @Query(value = "select a.*, count(b.cid) as count from t_metas a left join t_relationships b on a.mid = b.mid" +
            "where a.type = ? group by a.mid order by count desc, a.mid desc limit ?", nativeQuery = true)
    List<Metas> getLatestMetas(String string, int limit);

    @Query(value = "select a.*, count(b.cid) as count from t_metas a left join t_relationships b on a.mid = b.mid" +
            "where a.mid in(select mid from t_metas where type =  order by random() * mid limit ?) group by a.mid order by count desc, a.mid desc", nativeQuery = true)
    List<Metas> getRandomMetas(String type, int limit);

}
