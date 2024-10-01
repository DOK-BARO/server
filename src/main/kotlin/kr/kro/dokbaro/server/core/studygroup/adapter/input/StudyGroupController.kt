package kr.kro.dokbaro.server.core.studygroup.adapter.input

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.studygroup.adapter.input.dto.CreateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.dto.JoinStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.JoinStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/study-groups")
class StudyGroupController(
	private val createStudyGroupUseCase: CreateStudyGroupUseCase,
	private val joinStudyGroupUseCase: JoinStudyGroupUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@RequestBody body: CreateStudyGroupRequest,
		auth: Authentication,
	): IdResponse<Long> =
		IdResponse(
			createStudyGroupUseCase.create(
				CreateStudyGroupCommand(
					body.name,
					body.introduction,
					body.profileImageUrl,
					UUIDUtils.stringToUUID(auth.name),
				),
			),
		)

	@PostMapping("/join")
	fun joinStudyGroup(
		@RequestBody body: JoinStudyGroupRequest,
		auth: Authentication,
	) {
		joinStudyGroupUseCase.join(
			JoinStudyGroupCommand(
				body.inviteCode,
				UUIDUtils.stringToUUID(auth.name),
			),
		)
	}
}