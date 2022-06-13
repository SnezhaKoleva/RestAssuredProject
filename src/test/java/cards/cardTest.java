package cards;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.HashMap;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class cardTest {

    private String URI = "https://studentj7.kanbanize.com/api/v2/cards";
    private String apiKey = "t3a6xFky1eoEsh68JKV4tuH099DOxf5S0UWz88MW";
    private int cardID = 147;

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


        JsonPath jsonPath = res.jsonPath();
        Integer cardID = jsonPath.get("card_id");
        System.out.println(cardID + " - card ID");

    }
    @Test(priority = 2)
    public void checkCard() {

        given().contentType("application/json")
                .header("apikey", apiKey)
                .when().get(URI + "/"+cardID)
                .then().statusCode(200)
                .body("data.card_id",equalTo(cardID))
                .body("data.column_id", equalTo(7))
                .body("data.lane_id",equalTo(2))
                .body("data.title", equalTo("Try 1"))
                .body("data.color",equalTo("77569b"))
                .log().body();
    }

    @Test(priority = 3)
    public void moveCard(){

        HashMap<String,String> jsObj = new HashMap<>();
        jsObj.put("boardid","1");
        jsObj.put("column_id","8");
        jsObj.put("taskid","cardID");

        given().contentType("application/json")
                .header("apikey",apiKey)
                .body(jsObj)
                .when().patch(URI+"/"+cardID)
                .then()
                .statusCode(200).log().body();
    }
    @Test(priority = 4)
    public void checkMoveCard(){

        given().contentType("application/json")
                .header("apikey",apiKey)
                .when().get(URI+"/"+cardID)
                .then()
                .body("data.column_id",equalTo(8))
                .statusCode(200)
                .log().body();
    }
    @Test(priority = 5)
    public void deleteCard(){

        HashMap<String,String> jsObj = new HashMap<>();
        jsObj.put("boardid","1");
        jsObj.put("taskid","cardID");

        given().contentType("application/json")
                .header("apikey",apiKey)
                .body(jsObj)
                .when().delete(URI+"/"+cardID)
                .then().log().body().statusCode(204);

    }
    @Test(priority = 6)
    public void checkTheDelete() {
        given().contentType("application/json")
                .header("apikey", apiKey)
                .when().delete(URI + "/"+cardID)
                .then().statusCode(204);

    }
}

