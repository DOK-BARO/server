package kr.kro.dokbaro.server.core.quizquestionreport.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.core.quizquestionreport.adapter.input.web.dto.CreateQuizQuestionReportRequest
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.CreateQuizQuestionReportUseCase
import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.dto.CreateQuizQuestionReportCommand
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quiz-question-reports")
class QuizQuestionReportController(
	private val createQuizQuestionReportUseCase: CreateQuizQuestionReportUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@Login user: DokbaroUser,
		@RequestBody body: CreateQuizQuestionReportRequest,
	): IdResponse<Long> =
		IdResponse(
			createQuizQuestionReportUseCase.create(
				CreateQuizQuestionReportCommand(
					questionId = body.questionId,
					reporterId = user.id,
					content = body.content,
				),
			),
		)
}