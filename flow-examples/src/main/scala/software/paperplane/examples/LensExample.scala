package software.paperplane.examples

import software.paperplane.lens._

case class Student(name: String)
case class Department(student: Student)
case class University(dept: Department)

object LensExample {
  def main(args: Array[String]): Unit = {
    val uni = University(Department(Student("Oli")))

    val Qname       = Lens[Student, String](_.name, (s: Student, n: String) => s.copy(name = n))
    val Qstudent    = Lens[Department, Student](_.student, (d: Department, s: Student) => d.copy(student = s))
    val Qdepartment = Lens[University, Department](_.dept, (u: University, d: Department) => u.copy(dept = d))
    val Q = Qdepartment compose Qstudent compose Qname

    println(uni)
    println(Q.set(uni, "Joff"))
    println(uni)
  }
}
