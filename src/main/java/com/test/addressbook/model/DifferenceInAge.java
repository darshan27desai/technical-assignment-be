package com.test.addressbook.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class DifferenceInAge {
    @JsonProperty("count")
    private Long count;

    @JsonProperty("unit")
    private UnitEnum unit;

    @JsonProperty("criteria")
    private CriteriaEnum criteria;
    /**
     * Selected criteria of comparison
     */
    public enum UnitEnum {
        DAYS("Day(s)");

        private String value;

        UnitEnum(String value) {
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
        public static CriteriaEnum fromValue(String value) {
            for (CriteriaEnum b : CriteriaEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    /**
     * Selected criteria of comparison
     */
    public enum CriteriaEnum {
        OLDER("Older");

        private String value;

        CriteriaEnum(String value) {
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
        public static CriteriaEnum fromValue(String value) {
            for (CriteriaEnum b : CriteriaEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }
}