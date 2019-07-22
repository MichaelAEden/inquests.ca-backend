package db.slick

import db.spec.Db
import db.models.{User, Role}

import slick.lifted.ProvenShape

trait UserTable { this: Db =>

  import config.profile.api._

  class Users(tag: Tag) extends Table[User](tag, "user") {

    implicit val roleColumnType: BaseColumnType[Role] = MappedColumnType.base[Role, String](
      { role: Role => role.title },
      { title => Role.getRole(title) }
    )

    def id: Rep[Int] = column[Int]("user_id", O.AutoInc, O.PrimaryKey)
    def firebaseUid: Rep[String] = column[String]("firebase_uid", O.Unique)
    def email: Rep[String] = column[String]("email", O.Unique)
    def name: Rep[String] = column[String]("name")
    def jurisdictionId: Rep[String] = column[String]("jurisdiction_id")
    def role: Rep[Role] = column[Role]("role")

    def * : ProvenShape[User] = (
      id.?,
      firebaseUid,
      email,
      name,
      jurisdictionId,
      role
    ) <> (User.tupled, User.unapply)

  }

  val users = TableQuery[Users]

}
