package br.com.rest.unittests.mockito.services;

import br.com.rest.data.vo.v1.BookVO;
import br.com.rest.exceptions.RequiredObjectIsNullException;
import br.com.rest.models.Book;
import br.com.rest.repositories.BookRepository;
import br.com.rest.services.BookService;
import br.com.rest.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BooksServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testFindById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));


        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());


        assertTrue(result.toString().contains("links: [</api/book/${app.version}/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }


    @Test
    void testFindAll() {
        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        var books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        var bookOne = books.get(1);


        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());

        assertTrue(bookOne.toString().contains("links: [</api/book/${app.version}/1>;rel=\"self\"]"));
        assertEquals("Some Author1", bookOne.getAuthor());
        assertEquals("Some Title1", bookOne.getTitle());
        assertEquals(25D, bookOne.getPrice());
        assertNotNull( bookOne.getLaunchDate());


        var bookFour = books.get(4);


        assertNotNull(bookFour);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getLinks());


        assertTrue(bookFour.toString().contains("links: [</api/book/${app.version}/4>;rel=\"self\"]"));
        assertEquals("Some Author4", bookFour.getAuthor());
        assertEquals("Some Title4", bookFour.getTitle());
        assertEquals(25D, bookOne.getPrice());
        assertNotNull( bookOne.getLaunchDate());

    }




    @Test
    void testCreate() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(persisted);
        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());




        assertTrue(result.toString().contains("links: [</api/book/${app.version}/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {

        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1l);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        System.out.println(result.toString());


        assertTrue(result.toString().contains("links: [</api/book/${app.version}/1>;rel=\"self\"]"));
        assertEquals("Some Author1", result.getAuthor());
        assertEquals("Some Title1", result.getTitle());
        assertEquals(25D, result.getPrice());
        assertNotNull( result.getLaunchDate());
    }

    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {

        Book entity = input.mockEntity(1);
        entity.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

}
