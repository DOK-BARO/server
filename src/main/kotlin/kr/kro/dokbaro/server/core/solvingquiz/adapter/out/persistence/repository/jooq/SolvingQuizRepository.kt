package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import org.jooq.DSLContext
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JSolvingQuizSheet
import org.springframework.stereotype.Repository

@Repository
class SolvingQuizRepository(
	private val dslContext: DSLContext,
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

	fun update(solvingQuiz: SolvingQuiz) {
		dslContext
			.deleteFrom(SOLVING_QUIZ_SHEET)
			.where(SOLVING_QUIZ_SHEET.SOLVING_QUIZ_ID.eq(solvingQuiz.id))
			.execute()

		solvingQuiz.getSheets().forEach { (questionId, sheet) ->
			val query =
				sheet.answer.map { sheetContent ->
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
						)
				}
			dslContext.batch(query).execute()
		}
	}
}