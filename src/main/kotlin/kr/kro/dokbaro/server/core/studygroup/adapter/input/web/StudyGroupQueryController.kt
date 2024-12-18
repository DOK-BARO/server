package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailUseCase
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import kr.kro.dokbaro.server.core.studygroup.query.sort.MyStudyGroupSortKeyword
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/study-groups")
class StudyGroupQueryController(
	private val findAllMyStudyGroupUseCase: FindAllMyStudyGroupUseCase,
	private val findStudyGroupDetailUseCase: FindStudyGroupDetailUseCase,
) {
	@GetMapping("/my")
	fun getMyStudyGroups(
		@Login user: DokbaroUser,
		@RequestParam page: Long,
		@RequestParam size: Long,
		@RequestParam sort: MyStudyGroupSortKeyword,
		@RequestParam direction: SortDirection,
	): PageResponse<StudyGroupSummary> =
		findAllMyStudyGroupUseCase.findAll(
			memberId = user.id,
			pageOption =
				PageOption.of(
					page = page,
					size = size,
					sort = sort,
					direction = direction,
				),
		)

	@GetMapping("/{id}")
	fun findStudyGroup(
		@PathVariable id: Long,
	): StudyGroupDetail = findStudyGroupDetailUseCase.findStudyGroupDetailBy(id)
}