package com.podorozhniak.kotlinx.theory.variances;

import java.util.ArrayList;

// типи Parent і Child знаходяться в одній ієрархії класів
// інтуїтивно можна подумати, що і типи List<Parent> і List<Child> зв'язані
// але це не так. Всі контейнери в Java і більшість(?) в Kotlin інваріантні
// тип List<> в Kotlin біваріантний
public class VariancesJ {
    static class GrandParent  {
        String name = "Joshua";
    }
    static class Parent extends GrandParent { }
    static class Child extends Parent {    }
    static class GrandChild extends Child {    }

    public static void main(String[] args) {
        ArrayList<GrandParent> grandParentList = new ArrayList<>();
        ArrayList<Parent> parentList = new ArrayList<>();
        ArrayList<Child> childList = new ArrayList<>();
        ArrayList<GrandChild> grandChildList = new ArrayList<>();

        // без ключових слів в метод можна передати тільки конкретний тип
        // в даному випадку тільки ArrayList<Child>
        //getFirstNameOrAddChild(parentList); // помилка
        getFirstNameOrAddChild(childList);
        //getFirstNameOrAddChild(grandChildList); // помилка

        // якщо тип коваріантний, то в метод можна передавати і ще типи які є дочірніми вказаному типу
        // ArrayList<? extends Child>  => GrandChild extends Child => ArrayList<GrandChild>
        //getFirstNameCovariance(parentList); // помилка
        getFirstNameCovariance(childList);
        getFirstNameCovariance(grandChildList); // тепер так можна

        // якщо тип контраваріантний, то в метод можна передавати і ще типи які є базовими вказаному типу
        // ArrayList<? super Child> => Parent super (for) Child => ArrayList<Parent>
        // Parent is super for Child
        // Child is a Child
        addChildContravariance(grandParentList); // тепер так можна
        addChildContravariance(parentList); // тепер так можна
        addChildContravariance(childList);
        //addChildContravariance(grandChildList); // помилка
    }

    public static String getFirstNameOrAddChild(ArrayList<Child> childList) {
        //childList.add(new Parent()); // помилка. це не залежить від варіантності
        childList.add(new Child());
        childList.add(new GrandChild());
        return childList.get(0).name;
    }

    // Child is a Child
    // GrandChild extends Child
    // коваріантність не дозволяє додавати в список хоч щось
    public static String getFirstNameCovariance(ArrayList<? extends Child> childList) {
        //childList.add(new Parent()); // помилка. це не залежить від варіантності
        //childList.add(new Child()); // помилка через коваріанітність
        //childList.add(new GrandChild()); // помилка через коваріанітність

        return childList.get(0).name;
    }

    // Parent is super for Child
    // Child is a Child
    // можна додавати в список об'єкти вказаного типу і типи, які є базовими для нього
    // при контраваріантність можна отримати зі списку тільки об'єкти типу Object
    public static void addChildContravariance(ArrayList<? super Child> childList) {
        //childList.add(new Parent()); // помилка. це не залежить від варіантності
        childList.add(new Child());
        childList.add(new GrandChild());

        Object child = childList.get(0);
        String s = ((Child) child).name;
    }
}