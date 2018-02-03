package boot.tale.kit;


import org.springframework.data.domain.Sort;

public class SortKit {

    public static Sort sortBy(String orderType,String orderField){
        return new Sort(Sort.Direction.fromString(orderType),orderField);
    }
}
