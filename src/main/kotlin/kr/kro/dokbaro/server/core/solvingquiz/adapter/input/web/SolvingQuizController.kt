package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.SolveQuestionRequest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.StartSolvingQuizRequest
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.SolveQuestionUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.StartSolvingQuizUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.SolveQuestionCommand
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.StartSolvingQuizCommand
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/solving-quiz")
class SolvingQuizController(
	private val startSolvingQuizUseCase: StartSolvingQuizUseCase,
	private val solveQuestionUseCase: SolveQuestionUseCase,
) {
	@PostMapping("/start")
	fun startSolvingQuiz(
		@RequestBody body: StartSolvingQuizRequest,
		auth: Authentication,
	): IdResponse<Long> =
		IdResponse(
			startSolvingQuizUseCase.start(
				StartSolvingQuizCommand(
					UUIDUtils.stringToUUID(auth.name),
					body.quizId,
				),
			),
		)

	@PostMapping("/{id}/sheets")
	fun solveQuestion(
		@PathVariable id: Long,
		@RequestBody body: SolveQuestionRequest,
	) {
		solveQuestionUseCase.solve(
			SolveQuestionCommand(
				id,
				body.questionId,
				body.answers,
			),
		)
	}
}