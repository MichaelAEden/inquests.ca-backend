package db.slick

import slick.lifted.ProvenShape

import db.spec.Db
import db.models.Jurisdiction

trait JurisdictionTable { this: Db =>

  import config.profile.api._

  class Jurisdictions(tag: Tag) extends Table[Jurisdiction](tag, "jurisdiction") {

    def id: Rep[String] = column[String]("jurisdiction_id", O.PrimaryKey)
    def name: Rep[String] = column[String]("name", O.Unique)

    def * : ProvenShape[Jurisdiction] = (
      id,
      name,
    ) <> (Jurisdiction.tupled, Jurisdiction.unapply)

  }

  val jurisdictions = TableQuery[Jurisdictions]

}
