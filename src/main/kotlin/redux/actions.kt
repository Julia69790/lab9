package redux

import data.Lesson
import data.Student

class ChangePresent(val lesson: Int, val student: Int) : RAction
class ChangeLesson(val index:Int, val correctLesson:Lesson): RAction
class ChangeStudent(val index:Int, val correctStudent: Student): RAction
class AddLesson(val newLesson:Lesson): RAction
class AddStudent (val newStudent: Student): RAction
class DeleteLesson(var index:Int): RAction
class DeleteStudent(var index:Int): RAction