package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.Question
import org.jooq.Record
import org.jooq.Record3
import org.jooq.Record9
import org.jooq.Result
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizAnswerExplainImage
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.jooq.generated.tables.JStudyGroupQuiz
import org.jooq.generated.tables.records.BookQuizRecord

@Mapper
class BookQuizMapper {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_ANSWER = JBookQuizAnswer.BOOK_QUIZ_ANSWER
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
		private val STUDY_GROUP_QUIZ = JStudyGroupQuiz.STUDY_GROUP_QUIZ
		private val BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE = JBookQuizAnswerExplainImage.BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE
	}

	fun recordToBookQuizQuestions(
		record: Result<Record9<Long, String, Int, Long, ByteArray, String, Long, ByteArray, Int>>,
	): BookQuizQuestions? =
		record
			.groupBy {
				Triple(it.get(BOOK_QUIZ.ID), it.get(BOOK_QUIZ.TITLE), it.get(BOOK_QUIZ.TIME_LIMIT_SECOND))
			}.mapValues { entry ->
				entry.value
					.groupBy {
						Triple(
							it.get(BOOK_QUIZ_QUESTION.ID),
							it.get(BOOK_QUIZ_QUESTION.QUESTION_CONTENT),
							it.get(BOOK_QUIZ_QUESTION.QUESTION_TYPE),
						)
					}.map {
						Question(
							it.key.first,
							it.key.second.toString(Charsets.UTF_8),
							QuizType.valueOf(it.key.third),
							it.value.map { v ->
								SelectOption(
									v.get(BOOK_QUIZ_SELECT_OPTION.CONTENT).toString(Charsets.UTF_8),
								)
							},
						)
					}
			}.map {
				BookQuizQuestions(it.key.first, it.key.second, it.key.third, it.value)
			}.firstOrNull()

	fun toBookQuiz(record: Map<BookQuizRecord, Result<Record>>): BookQuiz? =
		record
			.map { (quiz, etc) ->
				BookQuiz(
					title = quiz.title,
					description = quiz.description.toString(Charsets.UTF_8),
					bookId = quiz.bookId,
					creatorId = quiz.creatorId,
					questions = toQuestions(etc),
					studyGroupId = etc.getValues(STUDY_GROUP_QUIZ.STUDY_GROUP_ID).filterNotNull().firstOrNull(),
					timeLimitSecond = quiz.timeLimitSecond,
					viewScope = AccessScope.valueOf(quiz.viewScope),
					editScope = AccessScope.valueOf(quiz.editScope),
					id = quiz.id,
				)
			}.firstOrNull()

	private fun toQuestions(etc: Result<Record>): QuizQuestions =
		QuizQuestions(
			etc
				.groupBy {
					QuestionBasic(
						it.get(BOOK_QUIZ_QUESTION.ID),
						it.get(BOOK_QUIZ_QUESTION.QUESTION_CONTENT).toString(Charsets.UTF_8),
						it.get(BOOK_QUIZ_QUESTION.ACTIVE),
						it.get(BOOK_QUIZ_QUESTION.EXPLANATION).toString(Charsets.UTF_8),
						QuizType.valueOf(it.get(BOOK_QUIZ_QUESTION.QUESTION_TYPE)),
					)
				}.map { (questionBasic, elements) ->
					QuizQuestion(
						content = questionBasic.content,
						selectOptions =
							elements
								.map {
									SelectOption(it.get(BOOK_QUIZ_SELECT_OPTION.CONTENT).toString(Charsets.UTF_8))
								}.distinct(),
						answer =
							AnswerFactory.create(
								questionBasic.quizType,
								AnswerSheet(elements.map { it.get(BOOK_QUIZ_ANSWER.CONTENT) }.distinct()),
							),
						active = questionBasic.active,
						answerExplanation = questionBasic.answerExplanation,
						id = questionBasic.id,
					)
				}.toMutableList(),
		)

	fun toBookQuizAnswer(record: Result<Record3<String, ByteArray, String>>): BookQuizAnswer? =
		record
			.groupBy { it.get(BOOK_QUIZ_QUESTION.EXPLANATION).toString(Charsets.UTF_8) }
			.map { (explanation, record) ->
				BookQuizAnswer(
					record.map { it.get(BOOK_QUIZ_ANSWER.CONTENT) },
					explanation,
					record.map { it.get(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL) },
				)
			}.firstOrNull()
}

data class QuestionBasic(
	val id: Long,
	val content: String,
	val active: Boolean,
	val answerExplanation: String,
	val quizType: QuizType,
)