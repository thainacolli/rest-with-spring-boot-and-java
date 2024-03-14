package br.com.rest.unittests.mapper.mocks;

import br.com.rest.data.vo.v1.BookVO;
import br.com.rest.models.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookVO mockVO() {
        return mockVO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }


    public Book mockEntity(Integer number) {
        Book books = new Book();

        books.setId(number.longValue());
        books.setAuthor("Some Author" + number);
        books.setLaunchDate(new Date());
        books.setPrice(25D);
        books.setTitle("Some Title" + number);

        return books;

    }

    public BookVO mockVO(Integer number) {
        BookVO books = new BookVO();

        books.setKey(number.longValue());
        books.setAuthor("Some Author" + number);
        books.setLaunchDate(new Date());
        books.setPrice(25D);
        books.setTitle("Some Title" + number);

        return books;
    }
}
