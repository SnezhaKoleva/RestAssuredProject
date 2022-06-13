package cards;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class cardTest {

    public String URI = "https://studentj7.kanbanize.com/api/v2/cards";
    public String apiKey = "jinthBEt7dblJ1UjXCkMkMsf1rl83XuJdwCDaate";

    @Test(priority = 1)
    public void createCard() {

        String data ="{\"boardid\":\"1\",\"title\":\"Try 1\"" +
                ",\"color\":\"77569b\",\"column_id\":\"7\",\"lane_id\":\"2\"}";

        Response res =
                given().contentType("application/json")
                        .header("apikey",apiKey)
                        .body(data)
                        .when().post(URI)
                        .then().statusCode(200).log().body().
                        extract().response();


//        JsonPath jsonPath = res.jsonPath();
//        Integer cardId = jsonPath.get("card_id");
//        System.out.println(cardId + " - card ID");
    }
    @Test(priority = 2)
    public void checkCard() {

        final ValidatableResponse body = given().contentType("application/json")
                .header("apikey", apiKey)
                .when().get(URI + "/139")
                .then().statusCode(200)
                .body("data.card_id",equalTo(139))
                .body("data.title", equalTo("Try 1"))
                .body("data.column_id", equalTo(7))
                .log().body();
    }

    @Test(priority = 3)
    public void moveCard(){

        HashMap<String,String> jsObj = new HashMap<>();
        jsObj.put("boardid","1");
        jsObj.put("column_id","8");
        jsObj.put("taskid","94");

        given().contentType("application/json")
                .header("apikey",apiKey)
                .body(jsObj)
                .when().patch(URI+"/94")
                .then()
                .statusCode(200).log().body();
    }
    @Test(priority = 4)
    public void checkMoveCard(){

        given().contentType("application/json")
                .header("apikey",apiKey)
                .when().get(URI+"/94")
                .then()
                .body("data.column_id",equalTo(8))
                .statusCode(200)
                .log().body();
    }
    @Test(priority = 5)
    public void deleteCard(){

        HashMap<String,String> jsObj = new HashMap<>();
        jsObj.put("boardid","1");
        jsObj.put("taskid","132");

        given().contentType("application/json")
                .header("apikey",apiKey)
                .body(jsObj)
                .when().delete(URI+"/91")
                .then().log().body().statusCode(204);

    }
    @Test(priority = 6)
    public void checkTheDelete() {
        given().contentType("application/json")
                .header("apikey", apiKey)
                .when().delete(URI + "/132")
                .then().statusCode(204);

    }
}

