package com.hdsam.message.spring;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * TestMain
 *
 * @author Yeo
 * @date 2023/7/24
 */
public class SpELTestMain {

    public static void main(String[] args) {
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("'hello world'.concat('!')");
//        String value = (String) expression.getValue();
//        System.out.println(value);

//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("'hello world'.bytes");
//        byte[] value = (byte[]) expression.getValue();
//        System.out.println(Arrays.toString(value));

//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("'hello world'.bytes.length");
//        int value = (Integer) expression.getValue();
//        System.out.println(value);

//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("new String('hello world').toUpperCase()");
//        String value = expression.getValue(String.class);
//        System.out.println(value);

//        Person person = new Person("John", "12");
//        ExpressionParser parser = new SpelExpressionParser();
//        Expression expression = parser.parseExpression("name");
//        EvaluationContext context = new StandardEvaluationContext(person);
//        String value = expression.getValue(context, String.class);
//        System.out.println(value);


        Person person = new Person("John", "12");
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("name");

        String value = expression.getValue(person, String.class);
        System.out.println(value);

        person.setName("Mike");
        String value2 = expression.getValue(person, String.class);
        System.out.println(value2);

        Expression expression2 = parser.parseExpression("name == 'Mike'");
        Boolean value3 = expression2.getValue(person, Boolean.class);
        System.out.println(value3);

    }

    static class Person {
        private String name;
        private String age;


        public Person(String name, String age) {
            this.name = name;
            this.age = age;
        }

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
    }


}
