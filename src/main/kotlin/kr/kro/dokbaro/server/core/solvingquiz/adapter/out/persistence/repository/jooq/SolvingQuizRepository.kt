package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq.SolvingQuizMapper
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JSolvingQuizSheet
import org.jooq.generated.tables.records.SolvingQuizRecord
import org.springframework.stereotype.Repository

@Repository
class SolvingQuizRepository(
	private val dslContext: DSLContext,
	private val solvingQuizMapper: SolvingQuizMapper,
) {
	companion object {
		private val SOLVING_QUIZ = JSolvingQuiz.SOLVING_QUIZ
		private val SOLVING_QUIZ_SHEET = JSolvingQuizSheet.SOLVING_QUIZ_SHEET
	}

	fun insert(solvingQuiz: SolvingQuiz): Long =
		dslContext
			.insertInto(
				SOLVING_QUIZ,
				SOLVING_QUIZ.MEMBER_ID,
				SOLVING_QUIZ.QUIZ_ID,
			).values(
				solvingQuiz.playerId,
				solvingQuiz.quizId,
			).returningResult(SOLVING_QUIZ.ID)
			.fetchOneInto(Long::class.java)!!

	fun findById(solvingQuizId: Long): SolvingQuiz? {
		val record: Map<SolvingQuizRecord, Result<Record>> =
			dslContext
				.select()
				.from(SOLVING_QUIZ)
				.leftJoin(SOLVING_QUIZ_SHEET)
				.on(SOLVING_QUIZ_SHEET.SOLVING_QUIZ_ID.eq(SOLVING_QUIZ.ID))
				.where(SOLVING_QUIZ.ID.eq(solvingQuizId))
				.fetchGroups(SOLVING_QUIZ)

		return solvingQuizMapper.toSolvingQuiz(record)
	}

	fun update(solvingQuiz: SolvingQuiz) {
		dslContext
			.deleteFrom(SOLVING_QUIZ_SHEET)
			.where(SOLVING_QUIZ_SHEET.SOLVING_QUIZ_ID.eq(solvingQuiz.id))
			.execute()

		solvingQuiz.getSheets().forEach { (questionId, sheet) ->
			sheet.answer.forEach { sheetContent ->
				dslContext
					.insertInto(
						SOLVING_QUIZ_SHEET,
						SOLVING_QUIZ_SHEET.SOLVING_QUIZ_ID,
						SOLVING_QUIZ_SHEET.QUESTION_ID,
						SOLVING_QUIZ_SHEET.CONTENT,
					).values(
						solvingQuiz.id,
						questionId,
						sheetContent,
					).execute()
			}
		}
	}
}