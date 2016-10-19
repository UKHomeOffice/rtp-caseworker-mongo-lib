package uk.gov.homeoffice.mongo.casbah

trait CaseworkerMongoConfiguration {
  def mongoConfiguration: Map[String, String]
}