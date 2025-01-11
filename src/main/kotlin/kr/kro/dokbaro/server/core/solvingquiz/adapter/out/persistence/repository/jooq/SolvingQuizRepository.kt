package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import org.jooq.DSLContext
import org.jooq.generated.tables.JSolvingQuiz.SOLVING_QUIZ
import org.jooq.generated.tables.JSolvingQuizSheet.SOLVING_QUIZ_SHEET
import org.springframework.stereotype.Repository

@Repository
class SolvingQuizRepository(
	private val dslContext: DSLContext,
) {
	fun insert(solvingQuiz: SolvingQuiz): Long {
		val savedSolvingQuizId =
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
							savedSolvingQuizId,
							questionId,
							sheetContent,
						)
				}
			dslContext.batch(query).execute()
		}

		return savedSolvingQuizId
	}

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