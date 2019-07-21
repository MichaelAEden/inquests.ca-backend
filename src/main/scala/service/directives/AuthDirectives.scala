package service.directives

import akka.http.scaladsl.server.{Directive1, Directives}
import akka.http.scaladsl.server.directives.Credentials

import clients.firebase.{FirebaseClient, FirebaseUser}

import scala.concurrent.Future

trait AuthDirectives extends Directives {

  private def authenticate(credentials: Credentials)(implicit firebaseClient: FirebaseClient): Future[Option[FirebaseUser]] = {
    credentials match {
      case Credentials.Provided(idToken) =>
        firebaseClient.getFirebaseUserFromToken(idToken)
      case _ =>
        Future.successful(None)
    }
  }

  def authenticateFirebaseUser(realm: String)(implicit firebaseClient: FirebaseClient): Directive1[FirebaseUser] = {
    authenticateOAuth2Async(realm, authenticate)
  }

  def authorizeAdmin(realm: String)(implicit firebaseClient: FirebaseClient): Directive1[FirebaseUser] = {
    authenticateFirebaseUser(realm) flatMap { user =>
      authorizeAsync(_ => firebaseClient.isAdmin(user)).tmap(_ => user)
    }
  }

}
