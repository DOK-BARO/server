package kr.kro.dokbaro.server.core.solvingquiz.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JSolvingQuiz
import org.jooq.generated.tables.JSolvingQuizSheet
import org.jooq.generated.tables.records.SolvingQuizRecord

@Mapper
class SolvingQuizMapper {
	companion object {
		private val SOLVING_QUIZ = JSolvingQuiz.SOLVING_QUIZ
		private val SOLVING_QUIZ_SHEET = JSolvingQuizSheet.SOLVING_QUIZ_SHEET
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
}