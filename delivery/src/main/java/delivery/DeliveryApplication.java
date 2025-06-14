package delivery;

import java.io.FileInputStream;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }

    // @Bean
    // public CommandLineRunner manualDeploy(RepositoryService repositoryService) {
    //     return args -> {
    //         String absolutePath = "D:\\studyng\\3 year\\БЛПС\\blps\\delivery\\src\\main\\resources\\process.bpmn";
    //         try {
    //             Deployment deployment = repositoryService.createDeployment()
    //                 .addInputStream("process.bpmn", 
    //                     new FileInputStream(absolutePath))
    //                 .name("Absolute Path Deployment")
    //                 .deploy();
                
    //             System.out.println("Deployed from: " + absolutePath);
    //         } catch (Exception e) {
    //             System.err.println("Failed to deploy from: " + absolutePath);
    //             e.printStackTrace();
    //         }
    //     };
}