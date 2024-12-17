package io.cucumber.skeleton;



import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;



@ApplicationScoped
@AllArgsConstructor
@Getter
public class World {

    @Inject
    private ScenarioContext scenarioContext;

    @Inject
    private ProductServiceAdapterPactMock productServiceAdapter;

    @Before
    public void initScenarioContext(Scenario scenario) {
        scenarioContext.setScenario(scenario);
        productServiceAdapter.initScenario(scenario);
    }

    @After
    public void writePacts(Scenario scenario) {
        String featureFilePath = scenario.getUri().toString();
        String featureFile = featureFilePath.substring(featureFilePath.lastIndexOf("/")+1);
        this.productServiceAdapter.writePactFile(featureFile);
    }

}