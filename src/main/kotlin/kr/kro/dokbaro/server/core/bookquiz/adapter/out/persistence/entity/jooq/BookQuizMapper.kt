package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizRecordFieldName
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuestionAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookSummary
import kr.kro.dokbaro.server.core.bookquiz.query.Creator
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.Question
import kr.kro.dokbaro.server.core.bookquiz.query.QuizContributor
import kr.kro.dokbaro.server.core.bookquiz.query.QuizCreator
import kr.kro.dokbaro.server.core.bookquiz.query.QuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import org.jooq.Record
import org.jooq.Record3
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizAnswerExplainImage
import org.jooq.generated.tables.JBookQuizContributor
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JQuizReview
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JStudyGroupQuiz
import org.jooq.generated.tables.records.BookQuizRecord

@Mapper
class BookQuizMapper {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
		private val BOOK_QUIZ_QUESTION = JBookQuizQuestion.BOOK_QUIZ_QUESTION
		private val BOOK_QUIZ_SELECT_OPTION = JBookQuizSelectOption.BOOK_QUIZ_SELECT_OPTION
		private val BOOK_QUIZ_ANSWER = JBookQuizAnswer.BOOK_QUIZ_ANSWER
		private val BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE = JBookQuizAnswerExplainImage.BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE
		private val MEMBER = JMember.MEMBER
		private val QUIZ_REVIEW = JQuizReview.QUIZ_REVIEW
		private val BOOK_QUIZ_CONTRIBUTOR = JBookQuizContributor.BOOK_QUIZ_CONTRIBUTOR
		private val STUDY_GROUP_QUIZ = JStudyGroupQuiz.STUDY_GROUP_QUIZ
		private val BOOK = JBook.BOOK
		private val SOLVING_QUIZ = JSolvingQuiz.SOLVING_QUIZ
	}

	fun recordToBookQuizQuestions(record: Result<out Record>): BookQuizQuestions? =
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
							QuestionAnswer(
								gradeSheet =
									GradeSheetFactory.create(
										questionBasic.quizType,
										AnswerSheet(elements.map { it.get(BOOK_QUIZ_ANSWER.CONTENT) }.distinct()),
									),
								explanationContent = questionBasic.answerExplanation,
								explanationImages =
									elements
										.map { it.get(BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL) }
										.distinct(),
							),
						active = questionBasic.active,
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

	fun toBookQuizSummary(record: Result<out Record>): Collection<BookQuizSummary> =
		record.map {
			BookQuizSummary(
				it.get(BOOK_QUIZ.ID),
				it.get(BOOK_QUIZ.TITLE),
				it.get(BookQuizRecordFieldName.AVERAGE_STAR_RATING.name, Double::class.java),
				it.get(BookQuizRecordFieldName.AVERAGE_DIFFICULTY_LEVEL.name, Double::class.java),
				it.get(BookQuizRecordFieldName.BOOK_QUIZ_QUESTION_COUNT.name, Int::class.java),
				Creator(
					it.get(MEMBER.ID),
					it.get(MEMBER.NICKNAME),
					it.get(MEMBER.PROFILE_IMAGE_URL),
				),
			)
		}

	fun toUnsolvedGroupBookQuizSummary(
		record: Map<BookQuizRecord, Result<out Record>>,
	): Collection<UnsolvedGroupBookQuizSummary> =
		record.map { (quiz, other) ->
			UnsolvedGroupBookQuizSummary(
				book =
					BookSummary(
						id = other.map { it.get(BOOK.ID) }.first(),
						title = other.map { it.get(BOOK.TITLE) }.first(),
						imageUrl = other.map { it.get(BOOK.IMAGE_URL) }.first(),
					),
				quiz =
					QuizSummary(
						id = quiz.id,
						title = quiz.title,
						creator =
							QuizCreator(
								id = other.map { it.get(BookQuizRecordFieldName.CREATOR_ID.name, Long::class.java) }.first(),
								nickname =
									other
										.map { it.get(BookQuizRecordFieldName.CREATOR_NAME.name, String::class.java) }
										.first(),
								profileImageUrl =
									other
										.map {
											it.get(
												BookQuizRecordFieldName.CREATOR_IMAGE_URL.name,
												String::class.java,
											)
										}.first(),
							),
						createdAt = quiz.createdAt,
						contributors =
							other
								.filter {
									it.get(
										BookQuizRecordFieldName.CONTRIBUTOR_ID.name,
										Long::class.java,
									) != Constants.UNSAVED_ID
								}.map {
									QuizContributor(
										id = it.get(BookQuizRecordFieldName.CONTRIBUTOR_ID.name, Long::class.java),
										nickname = it.get(BookQuizRecordFieldName.CONTRIBUTOR_NAME.name, String::class.java),
										profileImageUrl =
											it.get(
												BookQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL.name,
												String::class.java,
											),
									)
								},
					),
			)
		}

	fun toMyBookQuiz(record: Result<out Record>): Collection<MyBookQuizSummary> =
		record.map {
			MyBookQuizSummary(
				id = it.get(BOOK_QUIZ.ID),
				bookImageUrl = it.get(BOOK.IMAGE_URL),
				title = it.get(BOOK_QUIZ.TITLE),
				updatedAt = it.get(BOOK_QUIZ.UPDATED_AT),
			)
		}
}

data class QuestionBasic(
	val id: Long,
	val content: String,
	val active: Boolean,
	val answerExplanation: String,
	val quizType: QuizType,
)