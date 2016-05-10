package uk.gov.homeoffice.mongo.casbah

import org.specs2.mutable.SpecificationLike

trait CaseworkerMongoSpecification extends MongoSpecification {
  this: SpecificationLike =>

  lazy val mongoConfiguration: Map[String, String] = Map(
    "mongodb.uri" -> s"mongodb://localhost:$port/$database",
    "caseworker.mongodb" -> s"mongodb://localhost:$port/$database",
    "memcachedplugin" -> "disabled")
}