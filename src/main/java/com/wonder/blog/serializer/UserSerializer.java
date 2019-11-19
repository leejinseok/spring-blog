package com.wonder.blog.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.wonder.blog.domain.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {

  @Override
  public void serialize(User user, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    gen.writeStartObject();
    gen.writeNumberField("id", user.getId());
    gen.writeStringField("name", user.getName());
    gen.writeEndObject();
  }
}
