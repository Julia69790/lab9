package component

import data.*
import hoc.withDisplayName
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*
import redux.*
import kotlin.browser.document

interface AppProps : RProps {
    var store: Store<State, RAction, WrapperAction>
}

interface RouteNumberResult : RProps {
    var number: String
}

fun fApp() =
    functionalComponent<AppProps> { props ->
        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li{ navLink("/editL"){+"Edit lesson"} }
                    li{ navLink("/editS"){+"Edit student"} }
                }
            }
        }

        switch {
            route("/lessons",
                exact = true,
                render = {
                    anyList(props.store.getState().lessons, "Lessons", "/lessons")
                }
            )
            route("/students",
                exact = true,
                render = {
                    anyList(props.store.getState().students, "Students", "/students")
                }
            )
            route("/lessons/:number",
                render = renderLesson(props)
            )
            route("/students/:number",
                render = renderStudent(props)
            )
            route("/editL",
                exact = true,
                render ={
                    editElement(
                        RBuilder::displayLesson,
                        RBuilder::editLesson,
                        props.store.getState().lessons,
                        props.store.getState().lessons.mapIndexed { index, lesson ->
                            props.editL(index,lesson)
                        }.toTypedArray(),
                        props.addL(),
                        props.store.getState().lessons.mapIndexed { index, lesson ->
                            props.deleteL(index)
                        }.toTypedArray()
                    )
                }
            )
            route("/editS",
                exact = true,
                render = {
                    editElement(
                        RBuilder::displayStudent,
                        RBuilder::editStudent,
                        props.store.getState().students,
                        props.store.getState().students.mapIndexed { index, student ->
                            props.editS(index,student)
                        }.toTypedArray(),
                        props.addS(),
                        props.store.getState().students.mapIndexed { index, student ->
                            props.deleteS(index)
                        }.toTypedArray()

                    )
                }

            )
        }
    }

fun AppProps.onClickStudent(num: Int): (Int) -> (Event) -> Unit =
    { index ->
        {
            store.dispatch(ChangePresent(index, num))
        }
    }

fun AppProps.onClickLesson(num: Int): (Int) -> (Event) -> Unit =
    { index ->
        {
            store.dispatch(ChangePresent(num, index))
        }
    }

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


fun RBuilder.renderLesson(props: AppProps) =
    { route_props: RouteResultProps<RouteNumberResult> ->
        val num = route_props.match.params.number.toIntOrNull() ?: -1
        val lesson = props.store.getState().lessons.getOrNull(num)
        if (lesson != null)
            anyFull(
                RBuilder::student,
                lesson,
                props.store.getState().students,
                props.store.getState().presents[num],
                props.onClickLesson(num)
            )
        else
            p { +"No such lesson" }
    }

fun RBuilder.renderStudent(props: AppProps) =
    { route_props: RouteResultProps<RouteNumberResult> ->
        val num = route_props.match.params.number.toIntOrNull() ?: -1
        val student = props.store.getState().students.getOrNull(num)
        if (student != null)
            anyFull(
                RBuilder::lesson,
                student,
                props.store.getState().lessons,
                props.store.getState().presents.map {
                    it[num]
                }.toTypedArray(),
                props.onClickStudent(num)
            )
        else
            p { +"No such student" }
    }


fun RBuilder.app(
    store: Store<State, RAction, WrapperAction>
) =
    child(
        withDisplayName("App", fApp())
    ) {
        attrs.store = store
    }





