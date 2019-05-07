package org.naruto.framework.hello;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Greeting {
    private final long id;

    private final String content;
}
