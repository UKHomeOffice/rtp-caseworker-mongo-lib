package uk.gov.homeoffice.mongo.casbah

import org.specs2.mutable.SpecificationLike

trait CaseworkerMongoSpecification extends MongoSpecification with CaseworkerMongoConfiguration {
  this: SpecificationLike =>

  lazy val mongoConfiguration: Map[String, String] = Map(
    "mongodb.uri" -> s"mongodb://$server:$port/$database",
    "caseworker.mongodb" -> s"mongodb://$server:$port/$database",
    "memcachedplugin" -> "disabled")
}