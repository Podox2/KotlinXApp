package com.podorozhniak.kotlinx.theory.oop;

public class JPerson {

    private String name;
    private int age;
    private int height;
    private int width;

    public JPerson(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!name.isEmpty())
            this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        if (!name.isEmpty())
            return height;
        else return 0;
    }

    public void setHeight(int height) {
        if (height != 0)
            this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        if (width != 0)
            this.width = width;
    }
}
