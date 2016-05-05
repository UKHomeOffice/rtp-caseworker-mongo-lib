package uk.gov.homeoffice.mongo.casbah

import org.specs2.mutable.SpecificationLike

trait CaseworkerEmbeddedMongoSpecification extends EmbeddedMongoSpecification {
  this: SpecificationLike =>

  lazy val mongoConfiguration: Map[String, String] = Map(
    "mongodb.uri" -> s"mongodb://localhost:${network.getPort}/$database",
    "caseworker.mongodb" -> s"mongodb://localhost:${network.getPort}/$database",
    "memcachedplugin" -> "disabled")
}