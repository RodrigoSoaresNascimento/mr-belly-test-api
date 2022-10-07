package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class BaseTest implements Constantes{

    @BeforeClass
    public static void setup () {
        RestAssured.baseURI = APP_URL_BASE;
        RestAssured.basePath = APP_BASE_PATH;

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType(APP_CONTENT_TYPE);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectResponseTime(Matchers.lessThan(MAX_TIME_OUT));
        RestAssured.responseSpecification = responseSpecBuilder.build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }


}
