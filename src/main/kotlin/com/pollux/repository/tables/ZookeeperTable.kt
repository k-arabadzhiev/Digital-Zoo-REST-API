package com.pollux.repository.tables

import org.jetbrains.exposed.sql.Table

object ZookeeperTable : Table("zookeeper"){
    val zookeeperId = integer("id").autoIncrement()
    val firstName = varchar("Fname", 40)
    val lastName = varchar("Lname", 40)
    val userName = varchar("username", 40).uniqueIndex("USERNAME")
    val password = varchar("password", 64)
    override val primaryKey = PrimaryKey(zookeeperId, name = "PK_ZOOKEEPER")
}