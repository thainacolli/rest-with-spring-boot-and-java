package br.com.rest.integrationtests.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;
import jakarta.xml.bind.annotation.XmlRootElement;


import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@XmlRootElement
public class BookVO implements Serializable {




    private long id;

    public BookVO() {
    }

    private String author;

    private Date launchDate;

    private Double price;

    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(Date launchDate) {
        this.launchDate = launchDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookVO bookVO)) return false;
        if (!super.equals(o)) return false;
        return getId() == bookVO.getId() && getAuthor().equals(bookVO.getAuthor()) && getLaunchDate().equals(bookVO.getLaunchDate()) && getPrice().equals(bookVO.getPrice()) && getTitle().equals(bookVO.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId(), getAuthor(), getLaunchDate(), getPrice(), getTitle());
    }
}
