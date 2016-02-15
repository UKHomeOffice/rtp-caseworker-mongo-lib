package uk.gov.homeoffice.mongo.salat

import org.specs2.execute.{AsResult, Result}
import org.specs2.matcher.Scope
import org.specs2.mutable.Specification
import uk.gov.homeoffice.mongo.casbah.{CaseworkerMongo, EmbeddedMongoSpec}

class CaseworkerMongoSpec extends Specification with EmbeddedMongoSpec {
  override def around[T: AsResult](t: => T): Result = try {
    System.setProperty("caseworker.mongodb", s"mongodb://${mongoClient.connectPoint}/$database")
    super.around(t)
  } finally {
    System.clearProperty("caseworker.mongodb")
  }

  trait Context extends Scope {
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