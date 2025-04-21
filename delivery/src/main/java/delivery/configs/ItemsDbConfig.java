package delivery.configs;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "delivery.repositories.items",
        entityManagerFactoryRef = "itemsEntityManagerFactory"
)
public class ItemsDbConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.items-datasource")
    public DataSource itemsDataSource() {
        return new AtomikosDataSourceBean();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean itemsEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(itemsDataSource());
        em.setPackagesToScan("delivery.models.items");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(jpaProperties());
        em.setPersistenceUnitName("itemsPU");
        return em;
    }

    private Properties jpaProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.transaction.jta.platform",
                "com.atomikos.icatch.jta.hibernate5.AtomikosJtaPlatform");
        properties.put("javax.persistence.transactionType", "JTA");
        properties.put("hibernate.hbm2ddl.auto", "update");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        return properties;
    }
}