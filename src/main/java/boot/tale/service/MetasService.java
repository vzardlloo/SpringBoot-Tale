package boot.tale.service;


import boot.tale.exception.TaleException;
import boot.tale.kit.StringKit;
import boot.tale.model.entity.Contents;
import boot.tale.model.entity.Metas;
import boot.tale.model.entity.Relationships;
import boot.tale.model.repository.ContentsRepository;
import boot.tale.model.repository.MetasRepository;
import boot.tale.model.repository.RelationshipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MetasService {
    @Autowired
    MetasRepository metasRepository;
    @Autowired
    RelationshipsRepository relationshipsRepository;
    @Autowired
    ContentsRepository contentsRepository;

    /**
     * 根据类型查询项目列表
     * @param type 类型，tag or category
     * @return 项目列表
     */
    public List<Metas> getMetas(String type){
        if (StringKit.isNotBlank(type)){
            return metasRepository.findAllByTypeOrderByMidDesc(type);
        }
        return null;
    }

    /**
     *  查询项目映射
     * @param type 类型，tag or category
     * @return 类型名称到项目列表的映射
     */
    public Map<String,List<Contents>> getMetaMapping(String type){
        if (StringKit.isNotBlank(type)){
            List<Metas> mates = getMetas(type);
            if (null != mates){
                return mates.stream().collect(Collectors.toMap(Metas::getName,this::getMetaContents));
            }
        }
        return new HashMap<>();
    }


    public void delete(int mid){
        Metas metas = metasRepository.findByMid(mid);
    }

    private List<Contents> getMetaContents(Metas m){
        Integer mid = m.getMid();
        List<Relationships> relationships = relationshipsRepository.findAllByMid(mid);
        if (null == relationships || relationships.size() == 0){
            return new ArrayList<>();
        }
        List<Integer> cidList = relationships.stream().map(Relationships::getCid).collect(Collectors.toList());
        List<Contents> contents = contentsRepository.findAllByCidInOrderByCreatedDesc(cidList);
        return contents;
    }

    /**
     * 根据类型名称和名字查询项
     * @param type tag or category
     * @param name 类型名称
     * @return 项目
     */
    public Metas getMeta(String type,String name){
        if (StringKit.isNotBlank(type) && StringKit.isNotBlank(name)){
            return metasRepository.getMetasByTypeAndAndName(type,name);
        }
        return null;
    }


    /**
     * 保存或者更新内容
     * @param cid
     * @param type
     * @param name
     */
    private void saveOrUpdate(Integer cid,String type,String name){
        Metas metas = metasRepository.findByTypeAndName(type, name);
        int mid;
        if (null != metas){
            mid = metas.getMid();
        }else {
            //save 一个新的
            metas = new Metas();
            metas.setSlug(name);
            metas.setName(name);
            metas.setType(type);
            mid = metasRepository.save(metas).getMid();
        }
        if (mid != 0){
            //同步更新relations
            long count = relationshipsRepository.findAllByCidAndMid(mid,cid).size();
            if (count == 0){
                Relationships relationships = new Relationships();
                relationships.setCid(cid);
                relationships.setMid(mid);
                relationshipsRepository.saveAndFlush(relationships);
            }
        }

    }


    /**
     * 保存一个项目
     * @param mid
     * @param name
     * @param type
     */
    private void saveMeta(Integer mid,String name,String type){
        if (StringKit.isNotBlank(type,name)){
            Metas metas = metasRepository.findByTypeAndName(type,name);
            if (null != metas){
                throw new TaleException("已经存在该项");
            }else {
                if (null != mid){
                    metas = new Metas();
                    metas.setMid(mid);
                    metas.setName(name);
                    metasRepository.save(metas);
                }else {
                    metas.setMid(mid);
                    metas.setName(name);
                    metasRepository.saveAndFlush(metas);
                }
            }
        }
    }


    /**
     * 从metas里移除name
     * @param name
     * @param metas
     * @return
     */
    private String removeMeta(String name,String metas){
        String[]        ms  = metas.split(";");
        StringBuilder   sba = new StringBuilder();
        for (String m : ms){
            if (!name.equals(m)){
                sba.append(",").append(m);
            }
            //去掉第一个逗号
            if (sba.length() > 0){
                return sba.substring(1);
            }
        }
        return "";
    }

}
