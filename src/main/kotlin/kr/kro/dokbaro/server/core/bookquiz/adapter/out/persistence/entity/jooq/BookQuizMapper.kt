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
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JBookQuizAnswer
import org.jooq.generated.tables.JBookQuizAnswerExplainImage
import org.jooq.generated.tables.JBookQuizQuestion
import org.jooq.generated.tables.JBookQuizSelectOption
import org.jooq.generated.tables.JMember
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
		private val STUDY_GROUP_QUIZ = JStudyGroupQuiz.STUDY_GROUP_QUIZ
		private val BOOK = JBook.BOOK
	}

	fun toBookQuizQuestions(record: Result<out Record>): BookQuizQuestions? =
		record
			.groupBy {
				Triple(
					it[BOOK_QUIZ.ID],
					it[BOOK_QUIZ.TITLE],
					it[BOOK_QUIZ.TIME_LIMIT_SECOND],
				)
			}.map { (book, questions) ->
				BookQuizQuestions(
					id = book.first,
					title = book.second,
					timeLimitSecond = book.third,
					questions =
						questions
							.groupBy {
								Triple(
									it[BOOK_QUIZ_QUESTION.ID],
									it[BOOK_QUIZ_QUESTION.QUESTION_CONTENT],
									QuizType.valueOf(it[BOOK_QUIZ_QUESTION.QUESTION_TYPE]),
								)
							}.map { (question, options) ->
								BookQuizQuestions.Question(
									question.first,
									question.second,
									question.third,
									options.distinctBy { v -> v[BOOK_QUIZ_SELECT_OPTION.ID] }.map { v ->
										SelectOption(
											v[BOOK_QUIZ_SELECT_OPTION.CONTENT],
										)
									},
								)
							},
				)
			}.firstOrNull()

	fun toBookQuiz(record: Map<BookQuizRecord, Result<Record>>): BookQuiz? =
		record
			.map { (quiz, etc) ->
				BookQuiz(
					title = quiz.title,
					description = quiz.description,
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
						id = it[BOOK_QUIZ_QUESTION.ID],
						content = it[BOOK_QUIZ_QUESTION.QUESTION_CONTENT],
						active = it[BOOK_QUIZ_QUESTION.ACTIVE],
						answerExplanation = it[BOOK_QUIZ_QUESTION.EXPLANATION],
						quizType = QuizType.valueOf(it[BOOK_QUIZ_QUESTION.QUESTION_TYPE]),
					)
				}.map { (questionBasic, elements) ->
					QuizQuestion(
						content = questionBasic.content,
						selectOptions =
							elements
								.map {
									SelectOption(it[BOOK_QUIZ_SELECT_OPTION.CONTENT])
								}.distinct(),
						answer =
							QuestionAnswer(
								gradeSheet =
									GradeSheetFactory.create(
										questionBasic.quizType,
										AnswerSheet(elements.map { it[BOOK_QUIZ_ANSWER.CONTENT] }.distinct()),
									),
								explanationContent = questionBasic.answerExplanation,
								explanationImages =
									elements
										.map { it[BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL] }
										.distinct(),
							),
						active = questionBasic.active,
						id = questionBasic.id,
					)
				}.toMutableList(),
		)

	fun toBookQuizAnswer(record: Result<out Record>): BookQuizAnswer? =
		record
			.groupBy { it[BOOK_QUIZ_QUESTION.EXPLANATION] }
			.map { (explanation, record) ->
				BookQuizAnswer(
					correctAnswer = record.map { it[BOOK_QUIZ_ANSWER.CONTENT] },
					explanation = explanation,
					explanationImages = record.map { it[BOOK_QUIZ_ANSWER_EXPLAIN_IMAGE.IMAGE_URL] },
				)
			}.firstOrNull()

	fun toBookQuizSummary(record: Result<out Record>): Collection<BookQuizSummary> =
		record.map {
			BookQuizSummary(
				id = it[BOOK_QUIZ.ID],
				title = it[BOOK_QUIZ.TITLE],
				averageStarRating = it[BookQuizRecordFieldName.AVERAGE_STAR_RATING.name, Double::class.java],
				averageDifficultyLevel = it[BookQuizRecordFieldName.AVERAGE_DIFFICULTY_LEVEL.name, Double::class.java],
				questionCount = it[BookQuizRecordFieldName.BOOK_QUIZ_QUESTION_COUNT.name, Int::class.java],
				creator =
					BookQuizSummary.Creator(
						id = it[MEMBER.ID],
						nickname = it[MEMBER.NICKNAME],
						profileUrl = it[MEMBER.PROFILE_IMAGE_URL],
					),
			)
		}

	fun toUnsolvedGroupBookQuizSummary(
		record: Map<BookQuizRecord, Result<out Record>>,
	): Collection<UnsolvedGroupBookQuizSummary> =
		record.map { (quiz, other) ->
			UnsolvedGroupBookQuizSummary(
				book =
					UnsolvedGroupBookQuizSummary.Book(
						id = other.map { it[BOOK.ID] }.first(),
						title = other.map { it[BOOK.TITLE] }.first(),
						imageUrl = other.map { it[BOOK.IMAGE_URL] }.first(),
					),
				quiz =
					UnsolvedGroupBookQuizSummary.Quiz(
						id = quiz.id,
						title = quiz.title,
						creator =
							UnsolvedGroupBookQuizSummary.Creator(
								id =
									other
										.map { it[BookQuizRecordFieldName.CREATOR_ID.name, Long::class.java] }
										.first(),
								nickname =
									other
										.map { it[BookQuizRecordFieldName.CREATOR_NAME.name, String::class.java] }
										.first(),
								profileImageUrl =
									other
										.map {
											it[
												BookQuizRecordFieldName.CREATOR_IMAGE_URL.name,
												String::class.java,
											]
										}.first(),
							),
						createdAt = quiz.createdAt,
						contributors =
							other
								.filter {
									it[
										BookQuizRecordFieldName.CONTRIBUTOR_ID.name,
										Long::class.java,
									] != Constants.UNSAVED_ID
								}.map {
									UnsolvedGroupBookQuizSummary.Contributor(
										id = it[BookQuizRecordFieldName.CONTRIBUTOR_ID.name, Long::class.java],
										nickname =
											it[
												BookQuizRecordFieldName.CONTRIBUTOR_NAME.name,
												String::class.java,
											],
										profileImageUrl =
											it[
												BookQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL.name,
												String::class.java,
											],
									)
								},
					),
			)
		}

	fun toMyBookQuiz(record: Result<out Record>): Collection<MyBookQuizSummary> =
		record.map {
			MyBookQuizSummary(
				id = it[BOOK_QUIZ.ID],
				bookImageUrl = it[BOOK.IMAGE_URL],
				title = it[BOOK_QUIZ.TITLE],
				updatedAt = it[BOOK_QUIZ.UPDATED_AT],
			)
		}

	fun toBookQuizExplanation(record: Result<out Record>): BookQuizExplanation? =
		record
			.map {
				BookQuizExplanation(
					id = it[BOOK_QUIZ.ID],
					title = it[BOOK_QUIZ.TITLE],
					description = it[BOOK_QUIZ.DESCRIPTION],
					createdAt = it[BOOK_QUIZ.CREATED_AT],
					creator =
						BookQuizExplanation.Creator(
							id = it[MEMBER.ID],
							nickname = it[MEMBER.NICKNAME],
							profileImageUrl = it[MEMBER.PROFILE_IMAGE_URL],
						),
					book =
						BookQuizExplanation.Book(
							id = it[BOOK.ID],
							title = it[BOOK.TITLE],
							imageUrl = it[BOOK.IMAGE_URL],
						),
				)
			}.firstOrNull()

	data class QuestionBasic(
		val id: Long,
		val content: String,
		val active: Boolean,
		val answerExplanation: String,
		val quizType: QuizType,
	)
}