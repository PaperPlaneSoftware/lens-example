package software.paperplane

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.reflect.runtime.universe._

object Macros {
  def path[T, U](obj: T)(path: T => U): Unit = macro path_impl[T, U]

  def path_impl[T: c.WeakTypeTag, U: c.WeakTypeTag](c: blackbox.Context)(obj: c.Expr[T])(path: c.Expr[T => U]): c.Expr[Unit] = {
    import c.universe._

    def showInfo(s: String) =
      c.info(c.enclosingPosition, s.split("\n").mkString("\n |---macro info---\n |", "\n |", ""), true)

    // val Literal(Constant(s_format: String)) = format.tree
    // val evals = ListBuffer[ValDef]()
    //
    // def precompute(value: Tree, tpe: Type): Ident = {
    //   val freshName = TermName(c.fresh("eval$"))
    //   evals += ValDef(Modifiers(), freshName, TypeTree(tpe), value)
    //   Ident(freshName)
    // }
    //
    // val paramsStack = Stack[Tree]((params map (_.tree)): _*)
    // val refs = s_format.split("(?<=%[\\w%])|(?=%[\\w%])") map {
    //   case "%d" => precompute(paramsStack.pop, typeOf[Int])
    //   case "%s" => precompute(paramsStack.pop, typeOf[String])
    //   case "%%" => Literal(Constant("%"))
    //   case part => Literal(Constant(part))
    // }
    //
    // val stats = evals ++ refs.map(ref => reify(print(c.Expr[Any](ref).splice)).tree)
    // // c.Expr[Unit](Block(stats.toList, Literal(Constant(()))))

    def unravelPath(path: Tree): List[String] = path match {
      case Function(List(_), s @ Select(_)) => unravelPath(s)
      case Select(s @ Select(_), TermName(prop)) => prop :: unravelPath(s)
      case Select(Ident(TermName(obj)), TermName(prop)) => List(prop, obj)
    }

    val unraveledPath = unravelPath(path.tree).reverse

    c.Expr[Unit](
      q"""
      println(..$unraveledPath)
      """)
  }
}
