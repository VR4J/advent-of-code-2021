package be.vreijsen.aoc.day_12

import be.vreijsen.aoc.utils.*;

fun main(args: Array<String>) {
    var connections = getPuzzleInput(12, 1).map { 
        val left: String = it.split("-")[0]
        val right: String = it.split("-")[1]
 
        Connection(left, right) 
    }

    println(connections)

    var result = 0;

    getPaths("start", connections, listOf(), listOf()) { 
        result++
    }

    println(result)
}

fun getPaths(name: String, connections: List<Connection>, visited: List<String>, breadcrumb: List<String>, onReachEnd: () -> Unit) {
    var breadcrumbs = breadcrumb.plus(name)
    var visits = visited

    val neighbours = getConnecting(name, connections, visited)

    if(name.all { it.isLowerCase() }) {
        visits = visited.plus(name)
    }

    if(neighbours.contains("end")) {
        breadcrumbs = breadcrumbs.plus("end")
        println("Path: $breadcrumbs")
        onReachEnd()
    } 
    
    neighbours.forEach { connection ->             
        if(connection != "start" && connection != "end"){
            getPaths(connection, connections, visits, breadcrumbs, onReachEnd)
        }
    }  
}

fun getConnecting(name: String, connections: List<Connection>, visited: List<String>): List<String> {
    return connections.filter { (it.left == name || it.right == name) && ! visited.contains(name) }
                      .map { if(it.left == name) it.right else it.left }  
}

data class Connection(val left: String, val right: String)