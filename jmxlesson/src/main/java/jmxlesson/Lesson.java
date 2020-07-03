package jmxlesson;

public class Lesson implements LessonMBean {

    private String name;

    @Override
    public void sayHello() {
        System.out.println("Hello from JMX!");
    }

    @Override
    public int addIntegers(int a, int b) {
        return a + b;
    }

    @Override
    public Person returnPerson() {
        return new Person("Max");
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
