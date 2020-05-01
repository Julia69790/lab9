package redux

import data.State


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

