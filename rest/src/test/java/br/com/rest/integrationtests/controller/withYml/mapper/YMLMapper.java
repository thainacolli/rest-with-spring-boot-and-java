package br.com.rest.integrationtests.controller.withYml.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.restassured.mapper.ObjectMapper;
import io.restassured.mapper.ObjectMapperDeserializationContext;
import io.restassured.mapper.ObjectMapperSerializationContext;

import java.util.logging.Logger;

public class YMLMapper implements ObjectMapper {



    private Logger log = Logger.getLogger(YMLMapper.class.getName());
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    protected TypeFactory typeFactory;

    public YMLMapper() {
        this.objectMapper = objectMapper = new com.fasterxml.jackson.databind.ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.typeFactory = TypeFactory.defaultInstance();
    }


    @SuppressWarnings("rawtypes")
    @Override
    public Object deserialize(ObjectMapperDeserializationContext context) {
        try {

            String dataToDeserialize = context.getDataToDeserialize().asString();
            Class type = (Class) context.getType();

            log.info("Trying deserialize object of type" + type);

            return objectMapper.readValue(dataToDeserialize, typeFactory.constructType(type));
        } catch (JsonMappingException e) {
            log.severe("Error deserializing object");
            e.printStackTrace();
        }  catch (JsonProcessingException e) {
            log.severe("Error deserializing object");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object serialize(ObjectMapperSerializationContext context) {
        try {
            return objectMapper.writeValueAsString(context.getObjectToSerialize());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;


    }
}
