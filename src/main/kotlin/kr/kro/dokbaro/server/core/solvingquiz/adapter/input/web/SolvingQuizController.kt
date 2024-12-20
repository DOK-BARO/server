package kr.kro.dokbaro.server.core.solvingquiz.adapter.input.web

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.dto.response.PageResponse
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
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
		@Login user: DokbaroUser,
	): IdResponse<Long> =
		IdResponse(
			startSolvingQuizUseCase.start(
				StartSolvingQuizCommand(
					memberId = user.id,
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
		@Login user: DokbaroUser,
		@RequestParam page: Long,
		@RequestParam size: Long,
		@RequestParam sort: MyStudyGroupSolveSummarySortKeyword,
		@RequestParam direction: SortDirection,
	): PageResponse<StudyGroupSolveSummary> =
		findAllMyStudyGroupSolveSummaryUseCase.findAllMyStudyGroupSolveSummary(
			memberId = user.id,
			studyGroupId = studyGroupId,
			PageOption.of(
				page = page,
				size = size,
				sort = sort,
				direction = direction,
			),
		)

	@GetMapping("/my")
	fun getMySolvingQuiz(
		@Login user: DokbaroUser,
		@RequestParam page: Long,
		@RequestParam size: Long,
		@RequestParam sort: MySolvingQuizSortKeyword,
		@RequestParam direction: SortDirection,
	): PageResponse<MySolveSummary> =
		findAllMySolveSummaryUseCase.findAllMySolveSummary(
			memberId = user.id,
			PageOption.of(
				page = page,
				size = size,
				sort = sort,
				direction = direction,
			),
		)
}