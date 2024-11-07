package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailUseCase
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/study-groups")
class StudyGroupQueryController(
	private val findAllMyStudyGroupUseCase: FindAllMyStudyGroupUseCase,
	private val findStudyGroupDetailUseCase: FindStudyGroupDetailUseCase,
) {
	@GetMapping("/my")
	fun getMyStudyGroups(auth: Authentication): Collection<StudyGroupSummary> =
		findAllMyStudyGroupUseCase.findAll(UUIDUtils.stringToUUID(auth.name))

	@GetMapping("/{id}")
	fun findStudyGroup(
		@PathVariable id: Long,
	): StudyGroupDetail = findStudyGroupDetailUseCase.findStudyGroupDetailBy(id)
}