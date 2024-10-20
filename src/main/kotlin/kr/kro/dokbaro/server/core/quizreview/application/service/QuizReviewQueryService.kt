package kr.kro.dokbaro.server.core.quizreview.application.service

import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import kr.kro.dokbaro.server.core.quizreview.query.TotalDifficultyScore
import org.springframework.stereotype.Service

@Service
class QuizReviewQueryService(
	private val readQuizReviewTotalScorePort: ReadQuizReviewTotalScorePort,
) : FindQuizReviewTotalScoreUseCase {
	override fun findTotalScoreBy(quizId: Long): QuizReviewTotalScore {
		val elements: Collection<QuizReviewTotalScoreElement> = readQuizReviewTotalScorePort.findBy(quizId)

		if (elements.isEmpty()) {
			throw NotFoundQuizException(quizId)
		}

		val difficultyGroup: Map<Int, Int> =
			elements
				.map {
					it.difficultyLevel
				}.groupBy { it }
				.mapValues { (_, v) -> v.count() }

		return QuizReviewTotalScore(
			quizId = elements.map { it.quizId }.first(),
			averageScore = elements.map { it.score }.average(),
			difficulty =
				difficultyGroup.mapValues { (_, v) ->
					TotalDifficultyScore(v, v.toDouble() / difficultyGroup.values.sum())
				},
		)
	}
}