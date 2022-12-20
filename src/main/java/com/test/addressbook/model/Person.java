package com.test.addressbook.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class Person {
    @JsonProperty("name")
    private String name;
    /**
     * Person Gender
     */
    public enum GenderEnum {
        MALE("Male"),
        FEMALE("Female");

        private String value;

        GenderEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static GenderEnum fromValue(String value) {
            for (GenderEnum b : GenderEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

    }

    @JsonProperty("gender")
    private GenderEnum gender;
    @JsonProperty("dateOfBirth")
    @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;
}
