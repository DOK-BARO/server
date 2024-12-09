package kr.kro.dokbaro.server.core.member.adapter.input.web

import kr.kro.dokbaro.server.core.member.adapter.input.web.dto.ModifyMemberRequest
import kr.kro.dokbaro.server.core.member.application.port.input.command.ModifyMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.WithdrawMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.ModifyMemberCommand
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindMyAvatarUseCase
import kr.kro.dokbaro.server.core.member.query.MyAvatar
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/members")
class MemberController(
	private val modifyMemberUseCase: ModifyMemberUseCase,
	private val withdrawMemberUseCase: WithdrawMemberUseCase,
	private val findMyAvatarUseCase: FindMyAvatarUseCase,
) {
	@PutMapping("/login-user")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun modifyMember(
		@Login user: DokbaroUser,
		@RequestBody request: ModifyMemberRequest,
	) {
		modifyMemberUseCase.modify(
			ModifyMemberCommand(
				certificationId = user.certificationId,
				nickname = request.nickname,
				email = request.email,
				profileImage = request.profileImage,
			),
		)
	}

	@GetMapping("/login-user")
	fun getLoginUser(
		@Login user: DokbaroUser,
	): MyAvatar = findMyAvatarUseCase.findMyAvatar(user.certificationId)

	@PostMapping("/withdraw")
	fun withdraw(
		@Login user: DokbaroUser,
	) {
		withdrawMemberUseCase.withdrawBy(user.certificationId)
	}
}