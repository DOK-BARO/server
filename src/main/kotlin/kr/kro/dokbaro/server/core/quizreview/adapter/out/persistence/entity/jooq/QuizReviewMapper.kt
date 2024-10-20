package kr.kro.dokbaro.server.core.quizreview.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import org.jooq.Result
import org.jooq.generated.tables.records.QuizReviewRecord

@Mapper
class QuizReviewMapper {
	fun recordToQuizReviewTotalScoreElement(record: Result<QuizReviewRecord>): Collection<QuizReviewTotalScoreElement> =
		record.map {
			QuizReviewTotalScoreElement(
				quizId = it.quizId,
				score = it.score,
				difficultyLevel = it.difficultyLevel,
			)
		}
}