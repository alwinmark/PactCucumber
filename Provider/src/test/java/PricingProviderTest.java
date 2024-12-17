import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Consumer;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;


@Provider("ProductService")
@PactBroker(
        host="localhost",
        port="9292"
)
public class PricingProviderTest {
/**
    private static Pact testPrizingConsumer;
    @RestClient
    private ProductResource product;

    static List<Arguments> setDiscount() {
        FileSource pactFile = new FileSource(new File("../Consumer/target/contracts/discount.feature/set-discount.json"));

        ProviderInfo serviceProvider = new ProviderInfo("ProductService");
        serviceProvider.setProtocol("http");

        ConsumerInfo serviceConsumer = new ConsumerInfo();
        serviceConsumer.setName("PricingService");
        serviceConsumer.setPactSource(pactFile);

        testPrizingConsumer = DefaultPactReader.INSTANCE.loadPact(Objects.requireNonNull(serviceConsumer.getPactSource()));

        return testPrizingConsumer.getInteractions().stream().map(
                Arguments::of
        ).collect(Collectors.toList());
    }

    @BeforeClass
    static void setupProvider() {

    }

    @ParameterizedTest
    @MethodSource("setDiscount")
    void runConsumerPacts(Interaction interaction) {
        System.out.println(interaction);
    }
    **/
@TestTemplate
@ExtendWith(PactVerificationInvocationContextProvider.class)
void pactVerificationTestTemplate(PactVerificationContext context) {
    context.verifyInteraction();
}

    @BeforeAll
    static void enablePublishingPact() {
        System.setProperty("pact.verifier.publishResults", "true");
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        int port = Integer.parseInt(System.getProperty("http.port"));
        context.setTarget(new HttpTestTarget("localhost", 9080));
    }
}
