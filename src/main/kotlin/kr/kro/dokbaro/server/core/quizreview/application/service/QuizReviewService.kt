package kr.kro.dokbaro.server.core.quizreview.application.service

import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.DeleteQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.UpdateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.UpdateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.DeleteQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.InsertQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.LoadQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.UpdateQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.service.exception.NotFoundQuizReviewException
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview
import kr.kro.dokbaro.server.core.quizreview.event.CreatedQuizReviewEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class QuizReviewService(
	private val insertQuizReviewPort: InsertQuizReviewPort,
	private val eventPublisher: ApplicationEventPublisher,
	private val loadQuizReviewPort: LoadQuizReviewPort,
	private val updateQuizReviewPort: UpdateQuizReviewPort,
	private val deleteQuizReviewPort: DeleteQuizReviewPort,
) : CreateQuizReviewUseCase,
	UpdateQuizReviewUseCase,
	DeleteQuizReviewUseCase {
	override fun create(command: CreateQuizReviewCommand): Long {
		val memberId = TODO()
		val savedReviewId =
			insertQuizReviewPort.insert(
				QuizReview(
					starRating = command.starRating,
					difficultyLevel = command.difficultyLevel,
					comment = command.comment,
					memberId = memberId,
					quizId = command.quizId,
				),
			)

		eventPublisher.publishEvent(
			CreatedQuizReviewEvent(
				quizId = command.quizId,
				reviewId = savedReviewId,
				quizCreatorId = memberId,
			),
		)

		return savedReviewId
	}

	override fun update(command: UpdateQuizReviewCommand) {
		val quizReview: QuizReview =
			loadQuizReviewPort.findBy(id = command.id) ?: throw NotFoundQuizReviewException(command.id)

		quizReview.changeReview(
			starRating = command.starRating,
			difficultyLevel = command.difficultyLevel,
			comment = command.comment,
		)

		updateQuizReviewPort.update(quizReview)
	}

	override fun delete(id: Long) {
		deleteQuizReviewPort.deleteBy(id)
	}
}