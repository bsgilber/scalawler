package Scalakka

import scala.collection.mutable

/**
  * Created by bgilber on 4/30/2017.
  */
trait Tree[+A]
case class Leaf[A](value: A) extends Tree[A]
case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

object BFS{

  def bfs (root: Tree[Any]): Unit = {
    val list = mutable.ListBuffer[Any]()
    val q = mutable.Queue[Tree[Any]]()
    q.enqueue(root)

    def loop(queue: mutable.Queue[Tree[Any]]): Unit ={
      if(q.isEmpty) println(list.toList)
      else {
        val current = q.dequeue
        current match {
          case Leaf(_) => list += current
          case Branch(l,r) =>
            q.enqueue(l)
            q.enqueue(r)
        }
        loop(q)
      }
    }
    loop(q)
  }
}
