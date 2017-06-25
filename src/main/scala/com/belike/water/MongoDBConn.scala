package com.belike.water

import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase, ServerAddress}
import org.mongodb.scala.connection.ClusterSettings

/**
  * Created by sergeon on 6/24/17.
  */
class MongoDBConn(db: String = "scalawler") {
  val mongoClient: MongoClient= MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase(db)

  def collectionHandle(collection: String): MongoCollection[Document] = {
    database.getCollection(collection)
  }
}
