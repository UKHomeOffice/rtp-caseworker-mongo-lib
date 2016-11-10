package uk.gov.homeoffice.mongo.salat

import org.specs2.execute.{AsResult, Result}
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification
import uk.gov.homeoffice.mongo.casbah.{CaseworkerMongo, EmbeddedMongoSpecification}

class CaseworkerMongoSpec extends Specification with EmbeddedMongoSpecification {
  trait Context extends Scope {
    val repository = new Repository[Test] with CaseworkerMongo {
      val collectionName = "tests"
    }

    def withMongoConfiguration[R: AsResult](r: => R): Result = try {
      System.setProperty("caseworker.mongodb", s"mongodb://${mongoClient.connectPoint}/$database")
      AsResult(r)
    } finally {
      System.clearProperty("caseworker.mongodb")
    }
  }

  "Caseworker Mongo" should {
    "exist via the configuration 'caseworker.mongodb'" in new Context {
      withMongoConfiguration {
        val test = Test("test-me")
        repository save test

        repository.findAll.toSeq must contain(exactly(test))
      }
    }
  }
}

case class Test(id: String)