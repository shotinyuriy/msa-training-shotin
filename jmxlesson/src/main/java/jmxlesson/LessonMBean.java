package jmxlesson;

public interface LessonMBean {
    void sayHello();
    int addIntegers(int a, int b);
    Person returnPerson();
    String getName();
    void setName(String name);
}
