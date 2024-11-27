package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRecordFieldName
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.BookSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolvingQuizSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.QuizContributor
import kr.kro.dokbaro.server.core.solvingquiz.query.QuizCreator
import kr.kro.dokbaro.server.core.solvingquiz.query.QuizSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook
import org.jooq.generated.tables.JBookQuiz
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JSolvingQuizSheet
import org.jooq.generated.tables.records.SolvingQuizRecord

@Mapper
class SolvingQuizMapper {
	companion object {
		private val SOLVING_QUIZ = JSolvingQuiz.SOLVING_QUIZ
		private val SOLVING_QUIZ_SHEET = JSolvingQuizSheet.SOLVING_QUIZ_SHEET
		private val BOOK = JBook.BOOK
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
	}

	fun toSolvingQuiz(record: Map<SolvingQuizRecord, Result<Record>>): SolvingQuiz? =
		record
			.map { (key, value) ->
				SolvingQuiz(
					playerId = key.memberId,
					quizId = key.quizId,
					id = key.id,
					sheets =
						value
							.groupBy { it.get(SOLVING_QUIZ_SHEET.QUESTION_ID) }
							.mapValues { (_, v) ->
								AnswerSheet(
									v.map {
										it.get(
											SOLVING_QUIZ_SHEET.CONTENT,
										)
									},
								)
							}.filterKeys { it != null }
							.toMutableMap(),
				)
			}.firstOrNull()

	fun toMySolveSummary(record: Result<out Record>): Collection<MySolveSummary> =
		record.map {
			MySolveSummary(
				id = it.get(SOLVING_QUIZ.ID),
				solvedAt = it.get(SOLVING_QUIZ.CREATED_AT),
				bookImageUrl = it.get(BOOK.IMAGE_URL),
				quiz =
					MySolvingQuizSummary(
						id = it.get(BOOK_QUIZ.ID),
						title = it.get(BOOK_QUIZ.TITLE),
					),
			)
		}

	fun toMyStudyGroupSolveSummary(
		record: Map<SolvingQuizRecord, Result<out Record>>,
	): Collection<StudyGroupSolveSummary> =
		record.map { (solvingQuiz, etc) ->
			StudyGroupSolveSummary(
				id = solvingQuiz.id,
				solvedAt = solvingQuiz.createdAt,
				book =
					BookSummary(
						id = etc.map { it.get(BOOK.ID) }.first(),
						title = etc.map { it.get(BOOK.TITLE) }.first(),
						imageUrl = etc.map { it.get(BOOK.IMAGE_URL) }.first(),
					),
				quiz =
					QuizSummary(
						id = etc.map { it.get(BOOK_QUIZ.ID) }.first(),
						title = etc.map { it.get(BOOK_QUIZ.TITLE) }.first(),
						creator =
							etc
								.map {
									QuizCreator(
										id =
											etc
												.map {
													it.get(
														SolvingQuizRecordFieldName.CREATOR_ID.name,
														Long::class.java,
													)
												}.first(),
										nickname =
											etc
												.map {
													it.get(
														SolvingQuizRecordFieldName.CREATOR_NAME.name,
														String::class.java,
													)
												}.first(),
										profileImageUrl =
											etc
												.map {
													it.get(
														SolvingQuizRecordFieldName.CREATOR_IMAGE_URL.name,
														String::class.java,
													)
												}.first(),
									)
								}.first(),
						createdAt = etc.map { it.get(BOOK_QUIZ.CREATED_AT) }.first(),
						contributors =
							etc
								.filter {
									it.get(
										SolvingQuizRecordFieldName.CONTRIBUTOR_ID.name,
										Long::class.java,
									) != Constants.UNSAVED_ID
								}.map {
									QuizContributor(
										id = it.get(SolvingQuizRecordFieldName.CONTRIBUTOR_ID.name, Long::class.java),
										nickname =
											it.get(
												SolvingQuizRecordFieldName.CONTRIBUTOR_NAME.name,
												String::class.java,
											),
										profileImageUrl =
											it.get(
												SolvingQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL.name,
												String::class.java,
											),
									)
								},
					),
			)
		}
}