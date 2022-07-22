package com.podorozhniak.kotlinx.theory.variances

class VariancesKt {

    open class Parent {
        val name = "name"
    }

    open class Child : Parent()

    class GrandChild : Child()

    fun main() {
        val parentList = mutableListOf<Parent>()
        val childList = mutableListOf<Child>()
        val grandChildList = mutableListOf<GrandChild>()

        //getFirstNameAndAdd(parentList)
        getFirstNameAndAdd(childList)
        //getFirstNameAndAdd(grandChildList)

        //getFirstNameKt(parentList)
        getFirstNameKt(childList)
        getFirstNameKt(grandChildList)

        //getFirstNameCovariance(parentList)
        getFirstNameCovariance(childList)
        getFirstNameCovariance(grandChildList)

        addChild(parentList)
        addChild(childList)
        //addChild(grandChildList)
    }

    private fun getFirstNameAndAdd(list: MutableList<Child>): String {
        //list.add(Parent())
        list.add(Child())
        list.add(GrandChild())
        return list[0].name
    }

    // в котліні тип List<> вже коваріантний. якщо дописати out, IDE підкаже, що це слово redundant
    private fun getFirstNameKt(list: List<Child>): String {
        //list.add(Parent())
        //list.add(Child())
        //list.add(GrandChild())
        return list[0].name

    }

    //а ось MutableList<> інваріантний. треба вказувати out
    private fun getFirstNameCovariance(list: MutableList<out Child>): String {
        //list.add(Parent())
        //list.add(Child())
        //list.add(GrandChild())
        return list[0].name
    }

    private fun addChild(list: MutableList<in Child>) {
        //list.add(Parent())
        list.add(Child())
        list.add(GrandChild())
    }
}