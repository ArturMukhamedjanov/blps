package delivery.camunda;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.stream.Collectors;

@Component
public class CamundaFormDeployer {

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private RepositoryService repositoryService;

    @EventListener(ApplicationReadyEvent.class)
    public void deployFormsOnStartup() throws IOException {
        // repositoryService.createDeploymentQuery()
        //         .list()
        //         .stream()
        //         .flatMap(deployment -> 
        //             repositoryService.getDeploymentResourceNames(deployment.getId())
        //                 .stream()
        //                 .filter(name -> name.endsWith(".form"))
        //         )
        //         .collect(Collectors.toList());
        // RepositoryService repositoryService = processEngine.getRepositoryService();
        
        // PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // Resource[] formResources = resolver.getResources("classpath:/static/forms/*.*");
        
        // if (formResources.length > 0) {
        //     DeploymentBuilder deploymentBuilder = repositoryService.createDeployment()
        //         .name("forms_deployment")
        //         .enableDuplicateFiltering(true);
            
        //     // Добавляем каждый файл формы в деплоймент
        //     for (Resource formResource : formResources) {
        //         String filename = formResource.getFilename();
        //         deploymentBuilder.addInputStream(filename, formResource.getInputStream());
        //     }
            
        //     // Выполняем деплой
        //     deploymentBuilder.deploy();
        //     System.out.println("Deployed " + formResources.length + " forms to Camunda");
        // }
    }
}