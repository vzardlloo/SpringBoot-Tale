package boot.tale.configuration;

import boot.tale.kit.ConstKit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.File;

@Configuration
public class DataSourceConfiguration {

    public static String DB_NAME = "tale.db";
    public static String DB_PATH = ConstKit.CLASSPATH + File.separatorChar + DB_NAME;
    public static String DB_SRC = "jdbc:sqlite://" + DB_PATH;


    @Bean
    public DataSource dataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        dataSourceBuilder.url("jdbc:sqlite:tale.db");
        return dataSourceBuilder.build();
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }

}
