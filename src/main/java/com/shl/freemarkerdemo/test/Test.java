package com.shl.freemarkerdemo.test;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

public class Test {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Person person = new Person();
        person.setName("morris");
        person.setAge("22");
        person.setSex("男");
        String result = mapper.writeValueAsString(person);
        System.out.println(">>>result = " + result
        );

        result = "{\"name\":\"郭老师\",\"age\":\"22\",\"sex\":\"\"}";
        Person p = mapper.readValue(result, Person.class);
        System.out.println(p.getSex());
        System.out.println(p.getName());
        System.out.println(p.getAge());

    }
}

class Person implements Serializable {

    private static final long serialVersionUID = 1032143110760020278L;

    private String name;

    private String age;

    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
