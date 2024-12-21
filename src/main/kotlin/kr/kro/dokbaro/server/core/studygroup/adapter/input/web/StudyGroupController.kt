package kr.kro.dokbaro.server.core.studygroup.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.CreateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.JoinStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.adapter.input.web.dto.UpdateStudyGroupRequest
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.DeleteStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.JoinStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.UpdateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.UpdateStudyGroupCommand
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/study-groups")
class StudyGroupController(
	private val createStudyGroupUseCase: CreateStudyGroupUseCase,
	private val joinStudyGroupUseCase: JoinStudyGroupUseCase,
	private val deleteStudyGroupUseCase: DeleteStudyGroupUseCase,
	private val updateStudyGroupUseCase: UpdateStudyGroupUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@RequestBody body: CreateStudyGroupRequest,
		@Login user: DokbaroUser,
	): IdResponse<Long> =
		IdResponse(
			createStudyGroupUseCase.create(
				CreateStudyGroupCommand(
					name = body.name,
					introduction = body.introduction,
					profileImageUrl = body.profileImageUrl,
					creatorId = user.id,
				),
			),
		)

	@PostMapping("/join")
	fun joinStudyGroup(
		@RequestBody body: JoinStudyGroupRequest,
		@Login user: DokbaroUser,
	) {
		joinStudyGroupUseCase.join(
			JoinStudyGroupCommand(
				inviteCode = body.inviteCode,
				memberId = user.id,
				memberNickname = user.nickname,
			),
		)
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun update(
		@PathVariable id: Long,
		@Login user: DokbaroUser,
		@RequestBody body: UpdateStudyGroupRequest,
	) {
		updateStudyGroupUseCase.update(
			UpdateStudyGroupCommand(
				id = id,
				name = body.name,
				introduction = body.introduction,
				profileImageUrl = body.profileImageUrl,
			),
			user,
		)
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(
		@PathVariable id: Long,
		@Login user: DokbaroUser,
	) {
		deleteStudyGroupUseCase.deleteStudyGroup(id, user)
	}
}