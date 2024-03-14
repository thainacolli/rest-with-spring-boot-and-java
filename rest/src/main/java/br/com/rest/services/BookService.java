package br.com.rest.services;


import br.com.rest.controllers.BookController;
import br.com.rest.data.vo.v1.BookVO;
import br.com.rest.exceptions.RequiredObjectIsNullException;
import br.com.rest.exceptions.ResourceNotFoundException;
import br.com.rest.mapper.DozerMapper;
import br.com.rest.models.Book;
import br.com.rest.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    BookRepository repository;

    private Logger log = Logger.getLogger(BookService.class.getName());

    public BookVO findById(Long id) {

        log.info("Finding a book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());

        return vo;
    }

    public List<BookVO> findAll() {

        log.info("Finding all books!");

        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books
                .stream()
                .forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
        return books;
    }

    public BookVO create (BookVO book ) {
        if (book == null) throw new RequiredObjectIsNullException();
        log.info("Creating a book");

        var entity = DozerMapper.parseObject(book, Book.class);

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }


    public BookVO update (BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();
        log.info("Updating a book");

        var entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));


        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setTitle(book.getTitle());
        entity.setPrice(book.getPrice());

        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());

        return vo;
    }

    public void delete (Long id) {
        log.info("Deleting a book");

        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

        repository.delete(entity);
    }

}
