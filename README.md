# lab9
## Задание <br>
Используя код приложения из лекций переделайте приложения из последнего задания предыдущего модуля с использованием redux. 
Реализовать хранилище нужно простым способом, без использования функций комбинирования reducer'ов (их мы рассмотрим далее).
## Выполнение <br>
1. В actions прописаны события:<br>
        
        class ChangePresent(val lesson: Int, val student: Int) : RAction
        class ChangeLesson(val index:Int, val correctLesson:Lesson): RAction
        class ChangeStudent(val index:Int, val correctStudent: Student): RAction
        class AddLesson(val newLesson:Lesson): RAction
        class AddStudent (val newStudent: Student): RAction
        class DeleteLesson(var index:Int): RAction
        class DeleteStudent(var index:Int): RAction
        
2. В reducers прописаны действия по изменению состояния для каждого события:<br>
        
        fun changeReducer(state: State, action: RAction) =
        when (action) {
        is ChangePresent -> State(
            state.presents.mapIndexed { indexLesson, lesson ->
                if (indexLesson == action.lesson)
                    lesson.mapIndexed { indexStudent, student ->
                        if (indexStudent == action.student)
                            !student
                        else student
                    }.toTypedArray()
                else
                    lesson
            }.toTypedArray(),
            state.lessons,
            state.students
        )
        is ChangeLesson -> State(
            state.presents,
            state.lessons.mapIndexed { index, lesson ->
                if (index == action.index)
                    action.correctLesson
                else
                    lesson
            }.toTypedArray(),
            state.students
        )
        is ChangeStudent -> State(
            state.presents,
            state.lessons,
            state.students.mapIndexed { index, student ->
                if(index == action.index)
                    action.correctStudent
                else
                    student
            }.toTypedArray()
        )
        is AddLesson -> State(
            state.presents.plus(arrayOf(Array(state.students.size){false })),
            state.lessons.plus(action.newLesson),
            state.students
        )
        is AddStudent -> State(
            state.presents.map {
                it.plus(Array(1) { false })
            }.toTypedArray(),
                state.lessons,
                state.students.plus(action.newStudent)
                )
                is DeleteLesson -> State(
                state.presents.mapIndexedNotNull { index, booleans ->
                    if(index!=action.index)
                        booleans
                    else
                        null
                }.toTypedArray(),
                state.lessons.mapIndexedNotNull { index, lesson ->
                    if(index!=action.index)
                        lesson
                    else {
                        if (index == action.index && action.index != state.lessons.lastIndex) {
                            action.index++
                            state.lessons[action.index]
                        } else
                            null
                    }
                }.toTypedArray(),
                state.students
                )
                is DeleteStudent -> State(
                state.presents.mapIndexedNotNull { index, booleans ->
                    booleans.mapIndexedNotNull { index, b ->
                        if(index==action.index)
                            null
                        else
                            b
                    }.toTypedArray()
                }.toTypedArray(),
                state.lessons,
                state.students.mapIndexedNotNull { index, student ->
                    if(index!=action.index)
                        student
                    else {
                        if (index == action.index && action.index != state.students.lastIndex) {
                            action.index++
                            state.students[action.index]
                        }
                        else
                            null
                    }
                }.toTypedArray()
                )
                else -> state
            }

3. В app написаны функции в контексте AppProps<br>
        
        fun AppProps.editL(index:Int, lesson:Lesson): (Event) -> Unit =
        {
          val input1 = document.getElementById("${lesson.name}") !! as HTMLInputElement
          val correctLesson = Lesson("${input1.value}")
          store.dispatch(ChangeLesson(index, correctLesson))
        }
        
        fun AppProps.editS(index: Int, student:Student): (Event) -> Unit =
        {
          val input1 = document.getElementById("${student.firstname}") !! as HTMLInputElement
          val input2 = document.getElementById("${student.surname}") !! as HTMLInputElement
          val correctStudent = Student("${input1.value}","${input2.value}")
          store.dispatch(ChangeStudent(index,correctStudent))
        }
        
        fun AppProps.addL():(Event) -> Unit =
        {
          val myinput =  document.getElementById("add")!! as HTMLInputElement
          val newLesson = Lesson("${myinput.value}")
          store.dispatch(AddLesson(newLesson))
        }
        
        fun AppProps.addS():(Event) -> Unit =
        {
          val myinput1 =  document.getElementById("add")!! as HTMLInputElement
          val myinput2 = document.getElementById("addOnlyStudents")!! as HTMLInputElement
          val newStudent = Student("${myinput1.value}","${myinput2.value}")
          store.dispatch(AddStudent(newStudent))
        }
        
        fun AppProps.deleteL(index:Int):(Event) -> Unit =
        {
          store.dispatch(DeleteLesson(index))
        }
        
        fun AppProps.deleteS(index:Int):(Event) -> Unit =
        {
          store.dispatch(DeleteStudent(index))
        }
        
Добавили урок:<br>
![](/screen9/добавилиУрок.png)<br>
Отредактировали урок:<br>
![](/screen9/отредактировалиУрок.png)<br>
Удалили урок:<br>
![](/screen9/удалилиУрок.png)<br>
Отражение действий в консоли:<br>
![](/screen9/измененияУроков.png)<br>
Добавили студента:<br>
![](/screen9/добавилиСтудента.png)<br>
Отредактировали студента:<br>
![](/screen9/отредактировалиСтудента.png)<br>
Удалили студента:<br>
![](/screen9/удалилиСтудента.png)<br>
Отражение действий в консоли:<br>
![](/screen9/измененияСтудентов.png)<br>
