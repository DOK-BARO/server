package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRecordFieldName
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
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
							.groupBy { it[SOLVING_QUIZ_SHEET.QUESTION_ID] }
							.mapValues { (_, v) ->
								AnswerSheet(v.map { it[SOLVING_QUIZ_SHEET.CONTENT] })
							}.filterKeys { it != null }
							.toMutableMap(),
				)
			}.firstOrNull()

	fun toMySolveSummary(record: Result<out Record>): Collection<MySolveSummary> =
		record.map {
			MySolveSummary(
				id = it[SOLVING_QUIZ.ID],
				solvedAt = it[SOLVING_QUIZ.CREATED_AT],
				bookImageUrl = it[BOOK.IMAGE_URL],
				quiz =
					MySolveSummary.Quiz(
						id = it[BOOK_QUIZ.ID],
						title = it[BOOK_QUIZ.TITLE],
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
					StudyGroupSolveSummary.Book(
						id = etc.map { it[BOOK.ID] }.first(),
						title = etc.map { it[BOOK.TITLE] }.first(),
						imageUrl = etc.map { it[BOOK.IMAGE_URL] }.first(),
					),
				quiz =
					StudyGroupSolveSummary.Quiz(
						id = etc.map { it[BOOK_QUIZ.ID] }.first(),
						title = etc.map { it[BOOK_QUIZ.TITLE] }.first(),
						creator =
							etc
								.map {
									StudyGroupSolveSummary.Creator(
										id =
											etc
												.map {
													it[
														SolvingQuizRecordFieldName.CREATOR_ID.name,
														Long::class.java,
													]
												}.first(),
										nickname =
											etc
												.map {
													it[
														SolvingQuizRecordFieldName.CREATOR_NAME.name,
														String::class.java,
													]
												}.first(),
										profileImageUrl =
											etc
												.map {
													it[
														SolvingQuizRecordFieldName.CREATOR_IMAGE_URL.name,
														String::class.java,
													]
												}.first(),
									)
								}.first(),
						createdAt = etc.map { it[BOOK_QUIZ.CREATED_AT] }.first(),
						contributors =
							etc
								.filter {
									it[
										SolvingQuizRecordFieldName.CONTRIBUTOR_ID.name,
										Long::class.java,
									] != Constants.UNSAVED_ID
								}.map {
									StudyGroupSolveSummary.Contributor(
										id = it[SolvingQuizRecordFieldName.CONTRIBUTOR_ID.name, Long::class.java],
										nickname =
											it[
												SolvingQuizRecordFieldName.CONTRIBUTOR_NAME.name,
												String::class.java,
											],
										profileImageUrl =
											it[
												SolvingQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL.name,
												String::class.java,
											],
									)
								},
					),
			)
		}
}