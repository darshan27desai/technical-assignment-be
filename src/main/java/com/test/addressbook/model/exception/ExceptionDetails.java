package com.test.addressbook.model.exception;

import lombok.*;


@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetails{
    private String code;
    private String message;
    private String cause;

}
