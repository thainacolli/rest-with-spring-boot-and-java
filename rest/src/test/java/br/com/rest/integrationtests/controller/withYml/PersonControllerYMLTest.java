package br.com.rest.integrationtests.controller.withYml;

import br.com.rest.configs.TestConfigs;
import br.com.rest.integrationtests.controller.withYml.mapper.YMLMapper;
import br.com.rest.integrationtests.testcontainer.AbstractIntegrationTest;
import br.com.rest.integrationtests.vo.AccountCredentialsVO;
import br.com.rest.integrationtests.vo.PersonVO;
import br.com.rest.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYMLTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;

	private static PersonVO person;

	@BeforeAll
	public static void setup(){
		objectMapper = new YMLMapper();
		person = new PersonVO();

	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {

		AccountCredentialsVO user = new AccountCredentialsVO("thaina", "admin123");

		var accessToken = given()
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
				.port(TestConfigs.SERVER_PORT)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenVO.class, objectMapper)
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

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig
						.config()
						.encoderConfig(EncoderConfig.encoderConfig()
								.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVO.class, objectMapper);



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
	public void testUpdate() throws JsonProcessingException {
		person.setLastName("Garcia Souza");


		var persistedPerson =
				given().spec(specification)
						.config(RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.body(person, objectMapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);


		person = persistedPerson;
		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("André", persistedPerson.getFirstName());
		assertEquals("Garcia Souza",persistedPerson.getLastName());
		assertEquals("Rio de Janeiro",persistedPerson.getAddress());
		assertEquals("Masculino",persistedPerson.getGender());


	}


	@Test
	@Order(3)
	public void testFindById() throws JsonProcessingException {
		mockPerson();


		var persistedPerson =
				given().spec(specification)
						.config(RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_REST)
						.pathParam("id", person.getId())
						.when()
						.get("{id}")
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO.class, objectMapper);



		person = persistedPerson;
		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("André", persistedPerson.getFirstName());
		assertEquals("Garcia Souza",persistedPerson.getLastName());
		assertEquals("Rio de Janeiro",persistedPerson.getAddress());
		assertEquals("Masculino",persistedPerson.getGender());


	}


	@Test
	@Order(4)
	public void testDelete() throws JsonProcessingException {

				given().spec(specification)
						.config(RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.pathParam("id", person.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(204);

	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonProcessingException {

		var content =
				given().spec(specification)
						.config(RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.when()
						.get()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(PersonVO[].class, objectMapper);



		List<PersonVO> people = Arrays.asList(content);

		PersonVO foundPersonOne = people.get(0);

		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());

		assertEquals(1, foundPersonOne.getId());

		assertEquals("Maria", foundPersonOne.getFirstName());
		assertEquals("Silva",foundPersonOne.getLastName());
		assertEquals("São Paulo",foundPersonOne.getAddress());
		assertEquals("Feminino",foundPersonOne.getGender());

		PersonVO foundPersonSix = people.get(5);

		assertNotNull(foundPersonSix.getId());
		assertNotNull(foundPersonSix.getFirstName());
		assertNotNull(foundPersonSix.getLastName());
		assertNotNull(foundPersonSix.getAddress());
		assertNotNull(foundPersonSix.getGender());

		assertEquals(6, foundPersonSix.getId());

		assertEquals("Rogerio", foundPersonSix.getFirstName());
		assertEquals("Amaral",foundPersonSix.getLastName());
		assertEquals("São Paulo",foundPersonSix.getAddress());
		assertEquals("Masculino",foundPersonSix.getGender());

	}

	@Test
	@Order(6)
	public void testFindAllWithoutToken() throws JsonProcessingException {

		RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();


				given().spec(specificationWithoutToken)
						.config(RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.when()
						.get()
						.then()
						.statusCode(403);


	}



	private void mockPerson() {
		person.setFirstName("André");
		person.setLastName("Garcia");
		person.setAddress("Rio de Janeiro");
		person.setGender("Masculino");
	}

}
