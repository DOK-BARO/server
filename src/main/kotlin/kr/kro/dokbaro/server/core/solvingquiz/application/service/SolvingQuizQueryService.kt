package kr.kro.dokbaro.server.core.solvingquiz.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeResult
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllMySolveSummaryUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllMyStudyGroupSolveSummaryUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllSolveResultUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.input.FindAllStudyGroupSolveResultUseCase
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.CountSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.LoadStudyGroupSolvingQuizPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMySolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.ReadMyStudyGroupSolveSummaryPort
import kr.kro.dokbaro.server.core.solvingquiz.application.port.out.dto.CountSolvingQuizCondition
import kr.kro.dokbaro.server.core.solvingquiz.application.service.exception.NotFoundSolvingQuizException
import kr.kro.dokbaro.server.core.solvingquiz.domain.SolvingQuiz
import kr.kro.dokbaro.server.core.solvingquiz.query.MySolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupSolveSummary
import kr.kro.dokbaro.server.core.solvingquiz.query.StudyGroupTotalGradeResult
import kr.kro.dokbaro.server.core.solvingquiz.query.TotalGradeResult
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MySolvingQuizSortKeyword
import kr.kro.dokbaro.server.core.solvingquiz.query.sort.MyStudyGroupSolveSummarySortKeyword
import org.springframework.stereotype.Service

@Service
class SolvingQuizQueryService(
	private val findBookQuizUseCase: FindBookQuizUseCase,
	private val loadSolvingQuizPort: LoadSolvingQuizPort,
	private val readMySolveSummaryPort: ReadMySolveSummaryPort,
	private val readMyStudyGroupSolveSummaryPort: ReadMyStudyGroupSolveSummaryPort,
	private val countSolvingQuizPort: CountSolvingQuizPort,
	private val loadStudyGroupSolvingQuizPort: LoadStudyGroupSolvingQuizPort,
) : FindAllSolveResultUseCase,
	FindAllMySolveSummaryUseCase,
	FindAllMyStudyGroupSolveSummaryUseCase,
	FindAllStudyGroupSolveResultUseCase {
	override fun findAllGradeResultBy(solvingQuizId: Long): TotalGradeResult {
		val solvingQuiz: SolvingQuiz =
			loadSolvingQuizPort.findById(solvingQuizId) ?: throw NotFoundSolvingQuizException(solvingQuizId)

		val quiz: BookQuiz = findBookQuizUseCase.findBy(solvingQuiz.quizId)

		val gradeResultMap: Map<Long, GradeResult> = quiz.gradeAll(solvingQuiz.getSheets())

		return TotalGradeResult(
			solvingQuizId = solvingQuizId,
			quizId = quiz.id,
			playerId = solvingQuiz.playerId,
			questionCount = quiz.getQuestionCount(),
			correctCount = gradeResultMap.values.count { it.correct },
		)
	}

	override fun findAllMySolveSummary(
		memberId: Long,
		pageOption: PageOption<MySolvingQuizSortKeyword>,
	): PageResponse<MySolveSummary> {
		val totalCount: Long = countSolvingQuizPort.countBy(CountSolvingQuizCondition(memberId = memberId))

		val data: Collection<MySolveSummary> =
			readMySolveSummaryPort.findAllMySolveSummary(
				memberId = memberId,
				pageOption = pageOption,
			)

		return PageResponse.of(
			totalElementCount = totalCount,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun findAllMyStudyGroupSolveSummary(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<MyStudyGroupSolveSummarySortKeyword>,
	): PageResponse<StudyGroupSolveSummary> {
		val totalCount =
			countSolvingQuizPort.countBy(
				CountSolvingQuizCondition(memberId = memberId, studyGroupId = studyGroupId),
			)
		val data: Collection<StudyGroupSolveSummary> =
			readMyStudyGroupSolveSummaryPort.findAllMyStudyGroupSolveSummary(
				memberId = memberId,
				studyGroupId = studyGroupId,
				pageOption = pageOption,
			)

		return PageResponse.of(
			totalElementCount = totalCount,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun findAllStudyGroupGradeResultBy(
		studyGroupId: Long,
		quizId: Long,
	): StudyGroupTotalGradeResult {
		val quiz: BookQuiz = findBookQuizUseCase.findBy(quizId)
		val memberSheets: Map<StudyGroupTotalGradeResult.Member, SolvingQuiz?> =
			loadStudyGroupSolvingQuizPort.findAllStudyGroupSolvingQuizSheets(studyGroupId, quizId)

		val solvedMembers: Collection<StudyGroupTotalGradeResult.SolvedMember> =
			memberSheets.filterValues { it != null }.map { (member, solvingQuiz) ->
				val correctCount: Int = quiz.gradeAll(solvingQuiz!!.getSheets()).count { (_, v) -> v.correct }
				StudyGroupTotalGradeResult.SolvedMember(
					member = member,
					solvingQuizId = solvingQuiz.id,
					correctCount = correctCount,
				)
			}

		return StudyGroupTotalGradeResult(
			quizId = quizId,
			studyGroupId = studyGroupId,
			totalQuestionCount = quiz.getQuestionCount(),
			solvedMember = solvedMembers.sortedByDescending { it.correctCount },
			unSolvedMember = memberSheets.filterValues { it == null }.keys,
		)
	}
}