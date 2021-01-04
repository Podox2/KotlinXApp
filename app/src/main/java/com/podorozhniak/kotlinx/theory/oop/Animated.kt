package com.podorozhniak.kotlinx.theory.oop

abstract class Animated {
    //абстрактні методи по дефолту відкриті
    abstract fun animate()

    //не абстрактний по дефолту закритий
    open fun stopAnimating(){}

}