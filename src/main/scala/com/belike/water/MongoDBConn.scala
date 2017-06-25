package com.belike.water

import com.mongodb.async.client.Observable
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{Completed, MongoClient, MongoCollection, MongoDatabase, Observer}

/**
  * Created by sergeon on 6/24/17.
  */
class MongoDBConn(db: String = "scalawler") {
  val mongoClient: MongoClient= MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase(db)

  def collectionHandle(collection: String): MongoCollection[Document] = {
    database.getCollection(collection)
  }

  def commitInsert(insert: Observable[Completed]): Unit = {
    insert.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")
      override def onError(e: Throwable): Unit = println(s"onError: $e")
      override def onComplete(): Unit = println("onComplete")
    })
  }
}
