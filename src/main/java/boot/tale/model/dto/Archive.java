package boot.tale.model.dto;


import boot.tale.model.entity.Contents;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Archive {

    private String          data_str;
    private Date            data;
    private String          count;
    private List<Contents>  articles;

}
