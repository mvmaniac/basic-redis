package io.devfactory.basic._03_cache;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserProfile(@JsonProperty String name, @JsonProperty int age) {

}
