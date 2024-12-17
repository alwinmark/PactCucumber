/*package integration;

import com.examplestore.pricing.PrizingController;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.microshed.testing.jupiter.MicroShedTest;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.junit.jupiter.Container;

import static org.junit.jupiter.api.Assertions.*;


@MicroShedTest
class PrizingControllerIT {

    @Container
    public static ApplicationContainer app = new ApplicationContainer().withAppContextRoot("/prizing");

    @RestClient
    public static PrizingController prizingController;

    @Test
    public void testSetDiscount() {
        prizingController.setDiscount(new PrizingController.Discount(20));
    }
}

 */