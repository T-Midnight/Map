package com.example.tatiana.map.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Place() : RealmObject() {

    constructor(id: Int, latitude: Double, longitude: Double) : this()

    @PrimaryKey
    var id: Int = 0
    var latitude: Double = 0.toDouble()
    var longitude: Double = 0.toDouble()

}