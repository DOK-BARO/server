package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.SolveQuestionRequest
import kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web.dto.StartSolvingQuizRequest
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllMySolveSummaryUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllMyStudyGroupSolveSummaryUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllSolveResultUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.SolveQuestionUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.StartSolvingQuizUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.SolveQuestionCommand
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.dto.StartSolvingQuizCommand
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.SolveResult
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
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
	private val findAllSolveResultUseCase: FindAllSolveResultUseCase,
	private val findAllMySolveSummaryUseCase: FindAllMySolveSummaryUseCase,
	private val findAllMyStudyGroupSolveSummaryUseCase: FindAllMyStudyGroupSolveSummaryUseCase,
) {
	@PostMapping("/start")
	fun startSolvingQuiz(
		@RequestBody body: StartSolvingQuizRequest,
		auth: Authentication,
	): IdResponse<Long> =
		IdResponse(
			startSolvingQuizUseCase.start(
				StartSolvingQuizCommand(
					authId = UUIDUtils.stringToUUID(auth.name),
					quizId = body.quizId,
				),
			),
		)

	@PostMapping("/{id}/sheets")
	fun solveQuestion(
		@PathVariable id: Long,
		@RequestBody body: SolveQuestionRequest,
	): SolveResult =
		solveQuestionUseCase.solve(
			SolveQuestionCommand(
				solvingQuizId = id,
				questionId = body.questionId,
				answers = body.answers,
			),
		)

	@GetMapping("/{id}/grade-result")
	fun getGradeResult(
		@PathVariable id: Long,
	): TotalGradeResult = findAllSolveResultUseCase.findAllBy(id)

	@GetMapping("/study-groups/{studyGroupId}/my")
	fun getStudyGroupsMySolvingQuiz(
		@PathVariable studyGroupId: Long,
		auth: Authentication,
	): Collection<StudyGroupSolveSummary> =
		findAllMyStudyGroupSolveSummaryUseCase.findAllMyStudyGroupSolveSummary(
			UUIDUtils.stringToUUID(auth.name),
			studyGroupId,
		)

	@GetMapping("/my")
	fun getMySolvingQuiz(auth: Authentication): Collection<MySolveSummary> =
		findAllMySolveSummaryUseCase.findAllMySolveSummary(
			UUIDUtils.stringToUUID(auth.name),
		)
}