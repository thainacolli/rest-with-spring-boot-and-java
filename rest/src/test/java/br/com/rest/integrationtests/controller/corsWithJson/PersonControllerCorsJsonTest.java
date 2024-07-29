package br.com.rest.integrationtests.controller.corsWithJson;

import br.com.rest.configs.TestConfigs;
import br.com.rest.integrationtests.testcontainer.AbstractIntegrationTest;
import br.com.rest.integrationtests.vo.AccountCredentialsVO;
import br.com.rest.integrationtests.vo.PersonVO;
import br.com.rest.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerCorsJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup(){
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonVO();


	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO("thaina", "admin123");


		var accessToken =
				given()
						.basePath("/auth/signin")
						.port(TestConfigs.SERVER_PORT)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
							.body(user)
							.when()
							.post()
						.then()
							.statusCode(200)
								.extract()
								.body()
									.as(TokenVO.class)
						.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

	}

	@Test
	@Order(1)
	public void testCreate() throws JsonProcessingException {
		mockPerson();


		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_REST)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO persistedPerson;

			persistedPerson = objectMapper.readValue(content, PersonVO.class);

		person = persistedPerson;
		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("André", persistedPerson.getFirstName());
		assertEquals("Garcia",persistedPerson.getLastName());
		assertEquals("Rio de Janeiro",persistedPerson.getAddress());
		assertEquals("Masculino",persistedPerson.getGender());


	}

	@Test
	@Order(2)
	public void testCreateWithWrongOrigin() {
		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_CURSO)
						.body(person)
						.when()
						.post()
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();


		assertNotNull(content);
		assertEquals("Invalid CORS request", content);


	}

	@Test
	@Order(3)
	public void testFindById() throws JsonProcessingException {
		mockPerson();


		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_REST)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.asString();

		PersonVO persistedPerson;
			persistedPerson = objectMapper.readValue(content, PersonVO.class);

		person = persistedPerson;
		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("André", persistedPerson.getFirstName());
		assertEquals("Garcia",persistedPerson.getLastName());
		assertEquals("Rio de Janeiro",persistedPerson.getAddress());
		assertEquals("Masculino",persistedPerson.getGender());


	}


	@Test
	@Order(4)
	public void testeFindByIdWithWrongOrigin() {
		mockPerson();

		var content =
				given().spec(specification)
						.contentType(TestConfigs.CONTENT_TYPE_JSON)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_CURSO)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(403)
						.extract()
						.body()
						.asString();

		assertNotNull(content);
		assertEquals("Invalid CORS request", content);



	}

	private void mockPerson() {
		person.setFirstName("André");
		person.setLastName("Garcia");
		person.setAddress("Rio de Janeiro");
		person.setGender("Masculino");
	}

}