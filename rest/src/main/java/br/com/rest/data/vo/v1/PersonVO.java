package br.com.rest.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;


@JsonPropertyOrder({"id", "firstName", "lastName", "address", "gender"})
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {




    @Mapping("id")
    @JsonProperty("id")
    private long key;
    private String firstName;
    private String lastName;
    private String address;
    private String gender;

    public PersonVO() {

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonVO personVO)) return false;
        if (!super.equals(o)) return false;
        return getKey() == personVO.getKey() && getFirstName().equals(personVO.getFirstName()) && getLastName().equals(personVO.getLastName()) && getAddress().equals(personVO.getAddress()) && getGender().equals(personVO.getGender());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getKey(), getFirstName(), getLastName(), getAddress(), getGender());
    }
}
