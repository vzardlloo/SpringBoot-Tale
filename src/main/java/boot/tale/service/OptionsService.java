package boot.tale.service;

import boot.tale.kit.StringKit;
import boot.tale.model.entity.Options;
import boot.tale.model.repository.OptionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class OptionsService {

    @Autowired
    OptionsRepository optionsRepository;

    /**
     * 保存配置
     *
     * @param key
     * @param value
     */
    public void saveOption(String key, String value) {
        if (StringKit.isNotBlank(key, value)) {
            int count = optionsRepository.countByName(key);
            if (count == 0) {
                Options options = new Options();
                options.setName(key);
                options.setValue(value);
                optionsRepository.save(options);
            } else {
                Options options = optionsRepository.findOneByName(key);
                options.setValue(value);
                optionsRepository.saveAndFlush(options);
            }


        }
    }


    /**
     * 获取所有系统配置
     *
     * @return
     */
    public Map<String, String> getOptions() {
        List<Options> options = optionsRepository.findAll();
        return options.stream().collect(Collectors.toMap(t -> t.getName(), t -> t.getValue()));
    }

    /**
     * 根据key前缀查找配置项
     *
     * @param key
     * @return
     */
    public Map<String, String> getOptions(String key) {
        Map<String, String> options = new ConcurrentHashMap<>();
        List<Options> optionsList;
        if (StringKit.isNotBlank(key)) {
            optionsList = optionsRepository.findByNameLike(key);
        } else {
            //key 为空则默认查找全部
            optionsList = optionsRepository.findAll();
        }

        return optionsList.stream().collect(Collectors.toMap(t -> t.getName(), t -> t.getValue()));
    }

    /**
     * 获取option的值
     *
     * @param key
     * @return
     */
    public String getOptionValue(String key) {
        Options options = optionsRepository.findOneByName(key);
        if (null != options) {
            return options.getValue();
        }
        return null;
    }

    public void deleteOoption(String key) {
        if (StringKit.isNotBlank(key)) {
            optionsRepository.deleteOptionsByName(key);
        }
    }

}
