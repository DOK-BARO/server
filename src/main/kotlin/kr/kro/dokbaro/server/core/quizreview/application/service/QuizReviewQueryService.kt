package kr.kro.dokbaro.server.core.quizreview.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewSummaryUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.CountQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewSummaryPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import kr.kro.dokbaro.server.core.quizreview.query.TotalDifficultyScore
import org.springframework.stereotype.Service

@Service
class QuizReviewQueryService(
	private val readQuizReviewTotalScorePort: ReadQuizReviewTotalScorePort,
	private val countQuizReviewPort: CountQuizReviewPort,
	private val readQuizReviewSummaryPort: ReadQuizReviewSummaryPort,
) : FindQuizReviewTotalScoreUseCase,
	FindQuizReviewSummaryUseCase {
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
			averageStarRating = elements.map { it.starRating }.average(),
			difficulty =
				difficultyGroup.mapValues { (_, v) ->
					TotalDifficultyScore(v, v.toDouble() / difficultyGroup.values.sum())
				},
		)
	}

	override fun findAllQuizReviewSummaryBy(command: FindQuizReviewSummaryCommand): PageResponse<QuizReviewSummary> {
		val totalCount: Long = countQuizReviewPort.countBy(CountQuizReviewCondition(command.quizId))

		return PageResponse.of(
			totalCount,
			command.size,
			readQuizReviewSummaryPort.findAllQuizReviewSummaryBy(
				ReadQuizReviewSummaryCondition(command.quizId),
				PageOption.of(command.page, command.size),
				SortOption(command.sort, command.direction),
			),
		)
	}
}