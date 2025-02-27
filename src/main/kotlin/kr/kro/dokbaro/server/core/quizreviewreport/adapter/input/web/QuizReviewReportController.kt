package kr.kro.dokbaro.server.core.quizreviewreport.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.core.quizreviewreport.adapter.input.web.dto.CreateQuizReviewReportRequest
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.CreateQuizReviewReportUseCase
import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto.CreateQuizReviewReportCommand
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quiz-review-reports")
class QuizReviewReportController(
	private val createQuizReviewReportUseCase: CreateQuizReviewReportUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createQuizReviewReport(
		@RequestBody body: CreateQuizReviewReportRequest,
		@Login user: DokbaroUser,
	): IdResponse<Long> =
		IdResponse(
			createQuizReviewReportUseCase.create(
				CreateQuizReviewReportCommand(
					reporterId = user.id,
					quizReviewId = body.quizReviewId,
					content = body.content,
				),
			),
		)
}