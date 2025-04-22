package delivery.configs;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import org.springframework.boot.autoconfigure.domain.EntityScan;
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
@EntityScan(basePackages = "delivery.models.items")
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
        Properties props = new Properties();
        props.put("hibernate.transaction.jta.platform", 
            "org.hibernate.engine.transaction.jta.platform.internal.AtomikosJtaPlatform");
        props.put("javax.persistence.transactionType", "JTA");
        props.put("hibernate.hbm2ddl.auto", "update");
        props.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        props.put("hibernate.current_session_context_class", "jta");
        return props;
    }
}