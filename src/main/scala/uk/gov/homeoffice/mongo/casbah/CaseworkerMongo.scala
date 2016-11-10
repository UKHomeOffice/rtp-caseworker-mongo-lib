package uk.gov.homeoffice.mongo.casbah

import com.mongodb.casbah.MongoClientURI
import uk.gov.homeoffice.configuration.HasConfig

/**
  * Mixin to a repository needing to connect to the "caseworker" Mongo
  */
trait CaseworkerMongo extends Mongo {
  lazy val mongoDB = CaseworkerMongo.mongoDB
}

/**
  * Configuration example:
  * caseworker.mongodb = "mongodb://localhost:27017/caseworker"
  * Or for a replica set (including user and password)
  * caseworker.mongodb = "mongodb://user:pass@host:27011,host2:27012,host3:27013/caseworker"
  */
object CaseworkerMongo extends HasConfig {
  lazy val mongoDB = Mongo.mongoDB(MongoClientURI(config getString "caseworker.mongodb"))
}