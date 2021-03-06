package uk.gov.hmrc.uniform

import play.twirl.api.Html
import shapeless._
import shapeless.labelled._
import simulacrum._

import scala.language.implicitConversions

@typeclass trait HtmlShow[A] {
  def showHtml(in: A): Html
}

object HtmlShow {

  import ops._

  def instance[A](f: A => Html) = new HtmlShow[A] {
    def showHtml(in: A): Html = f(in)
  }

  implicit val showText = instance[String]{ Html(_) }

  implicit val showInt = instance[Int]{ i => Html(i.toString) }

  implicit def showOpt[A](implicit show: HtmlShow[A]) = instance[Option[A]] {
    case Some(x) => x.showHtml
    case None => Html("(empty)")
  }

  implicit def showList[A](implicit show: HtmlShow[A]) = instance[List[A]] { xs =>
    val inner = xs.map{ x => s"<li>${x.showHtml}</li>" }.mkString
    Html{ "<ul>" + inner + "</ul>" }
  }

  implicit def showHCons[K <: Symbol, H, T <: HList](
    implicit
      witness: Witness.Aux[K],
    hEncoder: Lazy[HtmlShow[H]],
    tEncoder: HtmlShow[T]
  ): HtmlShow[FieldType[K, H] :: T] = {
    val fieldName = witness.value.name
    instance { case (x::xs) =>
      Html(s"<dt>$fieldName</dt><dd>${hEncoder.value.showHtml(x)}</dd>${tEncoder.showHtml(xs)}")
    }
  }

  implicit val showHNil = instance[HNil] { _ => Html("") }

  implicit def genericShow[A, T](
    implicit generic: LabelledGeneric.Aux[A,T],
     hGenProvider: Lazy[HtmlShow[T]]
  ): HtmlShow[A] = instance { in =>
    Html (s"<dl>${hGenProvider.value.showHtml(generic.to(in))}</dl>")
  }

}
