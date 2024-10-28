package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.Gradable
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuestionAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer

fun bookQuizFixture(
	title: String = "title",
	description: String = "description",
	bookId: Long = 0,
	creatorId: Long = 0,
	questions: Collection<QuizQuestion> = listOf(quizQuestionFixture()),
	studyGroupId: Long? = null,
	id: Long = 0,
) = BookQuiz(
	title = title,
	description = description,
	bookId = bookId,
	creatorId = creatorId,
	questions = QuizQuestions(questions.toMutableList()),
	studyGroupId = studyGroupId,
	id = id,
)

fun quizQuestionFixture(
	content: String = "content",
	selectOptions: Collection<SelectOption> = listOf(SelectOption("selectOption1"), SelectOption("selectOption1")),
	answerExplanation: String = "answer_explanation",
	answerExplanationImages: Collection<String> = listOf("hello.png"),
	answer: Gradable =
		GradeSheetFactory.create(
			QuizType.MULTIPLE_CHOICE,
			AnswerSheet(listOf("2", "4")),
		),
	id: Long = Constants.UNSAVED_ID,
) = QuizQuestion(
	content = content,
	selectOptions = selectOptions,
	answer =
		QuestionAnswer(
			explanationContent = answerExplanation,
			explanationImages = answerExplanationImages,
			gradeSheet = answer,
		),
	id = id,
)

fun bookQuizAnswerFixture(
	correctAnswer: Collection<String> = listOf("1", "4"),
	explanation: String = "설명입니다",
	explanationImages: Collection<String> = listOf("hello.png"),
) = BookQuizAnswer(correctAnswer, explanation, explanationImages)