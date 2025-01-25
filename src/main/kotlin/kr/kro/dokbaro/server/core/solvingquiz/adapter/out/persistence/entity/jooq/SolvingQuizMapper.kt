package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq.SolvingQuizRecordFieldName
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupTotalGradeResult
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JBook.BOOK
import org.jooq.generated.tables.JBookQuiz.BOOK_QUIZ
import org.jooq.generated.tables.JMember.MEMBER
import org.jooq.generated.tables.JSolvingQuiz.SOLVING_QUIZ
import org.jooq.generated.tables.JSolvingQuizSheet.SOLVING_QUIZ_SHEET
import org.jooq.generated.tables.records.SolvingQuizRecord

@Mapper
class SolvingQuizMapper {
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
														SolvingQuizRecordFieldName.CREATOR_ID,
														Long::class.java,
													]
												}.first(),
										nickname =
											etc
												.map {
													it[
														SolvingQuizRecordFieldName.CREATOR_NAME,
														String::class.java,
													]
												}.first(),
										profileImageUrl =
											etc
												.map {
													it[
														SolvingQuizRecordFieldName.CREATOR_IMAGE_URL,
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
										SolvingQuizRecordFieldName.CONTRIBUTOR_ID,
										Long::class.java,
									] != Constants.UNSAVED_ID
								}.map {
									StudyGroupSolveSummary.Contributor(
										id = it[SolvingQuizRecordFieldName.CONTRIBUTOR_ID, Long::class.java],
										nickname =
											it[
												SolvingQuizRecordFieldName.CONTRIBUTOR_NAME,
												String::class.java,
											],
										profileImageUrl =
											it[
												SolvingQuizRecordFieldName.CONTRIBUTOR_IMAGE_URL,
												String::class.java,
											],
									)
								},
					),
			)
		}

	fun toStudyGroupSolvingQuizSheets(record: Result<out Record>): Map<StudyGroupTotalGradeResult.Member, SolvingQuiz?> =
		record
			.groupBy {
				StudyGroupTotalGradeResult.Member(
					id = it[MEMBER.ID],
					nickname = it[MEMBER.NICKNAME],
					profileImageUrl = it[MEMBER.PROFILE_IMAGE_URL],
				)
			}.mapValues { (_, values) ->
				values
					.filter { it[SOLVING_QUIZ.ID] != null }
					.groupBy {
						SolvingQuizGroup(
							id = it[SOLVING_QUIZ.ID],
							playerId = it[MEMBER.ID],
							quizId = it[SOLVING_QUIZ.QUIZ_ID],
						)
					}.mapValues { (_, sheetRecords) ->
						sheetRecords
							.filter { it[SOLVING_QUIZ_SHEET.QUESTION_ID] != null }
							.groupBy { it[SOLVING_QUIZ_SHEET.QUESTION_ID] }
							.mapValues { (_, v) ->
								AnswerSheet(v.map { it[SOLVING_QUIZ_SHEET.CONTENT] })
							}.toMutableMap()
					}.map { (group, sheets) ->
						SolvingQuiz(
							id = group.id,
							playerId = group.playerId,
							quizId = group.quizId,
							sheets = sheets,
						)
					}.firstOrNull()
			}
}

private data class SolvingQuizGroup(
	val id: Long,
	val playerId: Long,
	val quizId: Long,
)