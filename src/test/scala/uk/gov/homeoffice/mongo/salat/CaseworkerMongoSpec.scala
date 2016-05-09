package uk.gov.homeoffice.mongo.salat

import org.specs2.execute.{AsResult, Result}
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification
import uk.gov.homeoffice.mongo.casbah.{CaseworkerMongo, EmbeddedMongoSpecification}
import uk.gov.homeoffice.specs2.ComposableAround

class CaseworkerMongoSpec extends Specification with EmbeddedMongoSpecification {
  trait MongoPropertySetting extends ComposableAround {
    override def around[R: AsResult](r: => R): Result = try {
      System.setProperty("caseworker.mongodb", s"mongodb://${mongoClient.connectPoint}/$database")
      super.around(r)
    } finally {
      System.clearProperty("caseworker.mongodb")
    }
  }

  trait Context extends Scope with MongoPropertySetting {
    val repository = new Repository[Test] with CaseworkerMongo {
      val collectionName = "tests"
    }
  }

  "Caseworker Mongo" should {
    "exist via the configuration 'caseworker.mongodb'" in new Context {
      val test = Test("test-me")
      repository save test

      repository.findAll.toSeq must contain(exactly(test))
    }
  }
}

case class Test(id: String)