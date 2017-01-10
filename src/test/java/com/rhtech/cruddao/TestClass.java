package com.rhtech.cruddao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by rozaydin on 1/2/17.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class TestClass {

    private int id;
    private String name;
    private String surname;
    private long time;
    private float money;

    public TestClass() {
    }
}