package br.com.rest.integrationtests.controller.withYml;

import br.com.rest.configs.TestConfigs;
import br.com.rest.integrationtests.controller.withYml.mapper.YMLMapper;
import br.com.rest.integrationtests.testcontainer.AbstractIntegrationTest;
import br.com.rest.integrationtests.vo.AccountCredentialsVO;
import br.com.rest.integrationtests.vo.BookVO;
import br.com.rest.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerYMLTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YMLMapper objectMapper;

	private static BookVO book;

	@BeforeAll
	public static void setup(){
		objectMapper = new YMLMapper();
		book = new BookVO();

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
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonProcessingException {
		 mockBook();

		book = given()
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(book, objectMapper)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(BookVO.class, objectMapper);

		assertNotNull(book.getId());
		assertNotNull(book.getTitle());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());
		assertTrue(book.getId() > 0);
		assertEquals("Dan Brown", book.getAuthor());
		assertEquals(54.10,book.getPrice());
		assertEquals("Inferno",book.getTitle());


	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonProcessingException {
		book.setAuthor("Dan Gerhard Brown");


		var bookUpdated =
				given().spec(specification)
						.config(RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
						.contentType(TestConfigs.CONTENT_TYPE_YML)
						.accept(TestConfigs.CONTENT_TYPE_YML)
						.body(book, objectMapper)
						.when()
						.post()
						.then()
						.statusCode(200)
						.extract()
						.body()
						.as(BookVO.class, objectMapper);




		assertNotNull(book);

		assertNotNull(book.getId());
		assertNotNull(book.getAuthor());
		assertNotNull(book.getPrice());
		assertNotNull(book.getTitle());

		assertEquals(book.getId(), book.getId());

		assertEquals("Dan Gerhard Brown", book.getAuthor());
		assertEquals(54.10,book.getPrice());
		assertEquals("Inferno",book.getTitle());


	}


	@Test
	@Order(3)
	public void testFindById() throws JsonProcessingException {
		mockBook();


		var foundBook = given()
				.config(
						RestAssuredConfig
								.config()
								.encoderConfig(EncoderConfig.encoderConfig()
										.encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", book.getId())
				.when()
				.get("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(BookVO.class, objectMapper);

		assertNotNull(foundBook.getId());
		assertNotNull(foundBook.getTitle());
		assertNotNull(foundBook.getAuthor());
		assertNotNull(foundBook.getPrice());
		assertEquals(foundBook.getId(), book.getId());
		assertEquals("Dan Brown", book.getAuthor());
		assertEquals(54.10,book.getPrice());
		assertEquals("Inferno",book.getTitle());

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
						.pathParam("id", book.getId())
						.when()
						.delete("{id}")
						.then()
						.statusCode(204);

	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonProcessingException {

		var response =
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
						.as(BookVO[].class, objectMapper);




		List<BookVO> content = Arrays.asList(response);

		BookVO foundBookOne = content.get(0);

		assertNotNull(foundBookOne.getId());
		assertNotNull(foundBookOne.getTitle());
		assertNotNull(foundBookOne.getAuthor());
		assertNotNull(foundBookOne.getPrice());
		assertTrue(foundBookOne.getId() > 0);
		assertEquals("Working effectively with legacy code", foundBookOne.getTitle());
		assertEquals("Michael C. Feathers", foundBookOne.getAuthor());
		assertEquals(49.00, foundBookOne.getPrice());

		BookVO foundBookFive = content.get(4);

		assertNotNull(foundBookFive.getId());
		assertNotNull(foundBookFive.getTitle());
		assertNotNull(foundBookFive.getAuthor());
		assertNotNull(foundBookFive.getPrice());
		assertTrue(foundBookFive.getId() > 0);
		assertEquals("Code complete", foundBookFive.getTitle());
		assertEquals("Steve McConnell", foundBookFive.getAuthor());
		assertEquals(58.0, foundBookFive.getPrice());

	}





	private void mockBook() {
		book.setAuthor("Dan Brown");
		book.setLaunchDate(new Date());
		book.setPrice(54.10);
		book.setTitle("Inferno");
	}

}
